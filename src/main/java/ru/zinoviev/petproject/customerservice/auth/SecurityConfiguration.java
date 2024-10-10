package ru.zinovievbank.customerservice.auth;

import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.zinovievbank.customerservice.util.CustomPasswordEncoder;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private JwtFilter jwtFilter;

    @Bean
    public CustomPasswordEncoder passwordEncoderUtil() {
        return new CustomPasswordEncoder();
    }


    /**
     * обрабатываются все методы контроллеров
     * CustomerController - "/user" и NotificationController - "/notification/settings"
     * любые запросы на все остальные эндпоинты пропускаются без валидации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/notification").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return httpSecurity.build();
    }

    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        return new BasicGrpcAuthenticationReader();
    }

}
