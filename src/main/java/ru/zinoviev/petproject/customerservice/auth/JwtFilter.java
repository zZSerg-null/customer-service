package ru.zinovievbank.customerservice.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.zinovievbank.customerservice.service.CustomerService;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomerService customerService;

    private static final String HEADER_NAME = "Authorization";
    private static final String HEADER_BODY_PREFIX = "Bearer ";

    public JwtFilter(JwtProvider jwtProvider, CustomerService customerService) {
        this.jwtProvider = jwtProvider;
        this.customerService = customerService;
    }

    /**
     * Фильтр получает хэдер, забирает из него токен, если он есть.
     * Проверяет не протух ли токен
     * Вытаскивает из токена UUID юзера и проверяет, есть ли такой в базе данных
     *
     * @param request - запрос от клиента
     * @param response - ответ клиенту
     * @param filterChain - фильтр, испольщуемый при валидации
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HEADER_NAME);

        if (header != null && header.startsWith(HEADER_BODY_PREFIX)) {
            String token = header.substring(HEADER_BODY_PREFIX.length());
            try {
                jwtProvider.checkTokenExpired(token);
                String userId = jwtProvider.extractCustomerId(token).toString();

                if (!customerService.isCustomerPresent(userId)) {
                    throw new AppException(EnumException.NOT_FOUND, "Customer not found");
                }

            } catch (Exception e) {
                throw new AppException(EnumException.UNAUTHORIZED, "token is invalid");
            }

        }

        filterChain.doFilter(request, response);
    }

}
