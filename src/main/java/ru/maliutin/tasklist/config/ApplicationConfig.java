package ru.maliutin.tasklist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.maliutin.tasklist.web.security.JwtTokenFilter;
import ru.maliutin.tasklist.web.security.JwtTokenProvider;

/**
 * Класс конфигурации Spring Security.
 */

@Configuration  // Аннотация Spring - отмечающая класс, как конфигурационный.
@EnableWebSecurity  // Аннотация Spring Security - отмечающая класс, как конфигурационный для Spring Security.
@RequiredArgsConstructor(onConstructor = @__(@Lazy))  // Аннотация lombok - предоставляющая классу конструкторы.
/*
    Благодаря @Lazy, эти аргументы будут созданы только при первом обращении к ним.
    Это может быть полезно, если создание аргументов потребует затратных ресурсов
    или если вы хотите отложить их инициализацию.
    В данном случае используется для предотвращения циклической зависимости между компонентами Spring.
 */
public class ApplicationConfig {

    /**
     * Поле с объектом класса отвечающим за создание и проверку токенов.
     */
    private final JwtTokenProvider tokenProvider;
    /**
     * Поле с классом отвечающих за аутентификацию пользователей.
     */
    private final ApplicationContext applicationContext;

    /**
     * Бин отвечающий за хэширование паролей при прохождении аутентификации.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Бин отвечающий за аутентификацию пользователя.
     * @param configuration конфигурацию аутентификации.
     * @return менеджер аутентификации.
     * @throws Exception исключение???
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Бин с настройками фильтров Spring Security.
     * @param httpSecurity настройку Spring Security.
     * @return
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(except -> except.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Unauthorized.");
                }).accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Unauthorized.");
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
