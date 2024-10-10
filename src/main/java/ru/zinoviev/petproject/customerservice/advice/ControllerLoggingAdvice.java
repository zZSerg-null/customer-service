package ru.zinovievbank.customerservice.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.util.MaskData;
import ru.zinovievbank.customerservice.util.enums.DataTypeToMask;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAdvice {

    private final MaskData maskData;
    private final ObjectMapper mapper;

    public ControllerLoggingAdvice(MaskData maskData, ObjectMapper mapper) {
        this.maskData = maskData;
        this.mapper = mapper;
    }

    @Pointcut("execution(* ru.zinovievbank.customerservice.controller.*.*(..))")
    public void controllerPointCut() {
        //Pointcut methods should have empty body
    }

    @SneakyThrows
    @Around("controllerPointCut()")
    public Object log(ProceedingJoinPoint pjp) {
        HttpServletRequest request =
            ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        Collection<String> requestParams = extractRequestParameters(request);
        List<String> requestBody = new ArrayList<>();
        Object[] pjpArgs = pjp.getArgs();
        for (Object object : pjpArgs) {
            String extractedBody = extractBody(object);
            if (extractedBody != null) {
                requestBody.add(extractedBody);
            }
        }
        log.info( "\033[1;97mRequest: \033[4;34m{}\033[0m, "
                + "\033[1;97mURI: \033[4;37m{}\033[0m, "
                + "\033[1;97mParameters: \033[1;33m{}\033[0m, "
                + "\033[1;97mBody: \033[1;32m{}\033[0m",
            httpMethod, uri, requestParams, requestBody);
        Object proceed = pjp.proceed();
        if (proceed != null) {
            String responseBody = extractBody(((ResponseEntity<?>) proceed).getBody());
            log.info( "\033[1;97mResponse URI: \033[4;37m{}\u001B[0m, "
                    + "\033[1;97mBody: \033[1;32m{}\u001B[0m",
              uri, responseBody);
            return proceed;
        }
        log.info( "\033[1;97mResponse URI: \033[4;37m{}\u001B[0m, "
                + "\033[1;97mBody: \033[1;32m{}\u001B[0m",
          uri, "proceed is null");
        return null;
    }

    @SneakyThrows
    private String extractBody(Object o) {
        if (o instanceof EmailDto emailDto) {
            String email = emailDto.email();
            EmailDto dto = new EmailDto(maskData.mask(email, DataTypeToMask.EMAIL));
            return mapper.writeValueAsString(dto);
        }
        return mapper.writeValueAsString(o);
    }

    private Collection<String> extractRequestParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> pm = new HashMap<>();
        DataTypeToMask[] dataTypeToMasks = DataTypeToMask.values();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            DataTypeToMask dataType = Arrays.stream(dataTypeToMasks)
                .filter(t -> t.getValue().equalsIgnoreCase(entry.getKey()))
                .findAny()
                .orElse(null);
            if (dataType != null) {
                pm.put(entry.getKey(), maskData.mask(entry.getValue()[0], dataType));
            } else {
                pm.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        return pm.values();
    }
}
