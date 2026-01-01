package ru.greenatom.atsdb.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import ru.greenatom.atsdb.security.service.CookieService;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CookieService cookieService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    //private final JwtTimestampValidator jwtTimestampValidator;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**","/js/**","/images/**","/webjars/**").permitAll()
                    .requestMatchers("/", "/auth/**").permitAll()
                    //.requestMatchers("/token/refresh").permitAll()
                    //.requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                    //.requestMatchers("/user").hasAuthority("ROLE_USER")
                    //.requestMatchers("/test").hasAuthority("ROLE_USER")
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(Customizer.withDefaults())
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    /**
     * Бин который изменяет поиск токена для oauth2ResourceServer.
     * Заменяет поиск по умолчанию в заголовке "Authorization: Bearer ...." 
     * на поиск в куке
     * @return Jwt Token
     * Бин работает уже только по тому, что он создан с таким именем и помечен аннотацией @Bean
     * прописывать его отдельно в securityFilterChain не нужно
     */
    @Bean
    public BearerTokenResolver cookieBasedBearerTokenResolver() {
        return (request) -> {
            return cookieService.getAccessTokenFromCookies(request);
        };
    }

    /**
     * Бин в котором настраивается базовый JwtGrantedAuthoritiesConverter
     * Изменяется AuthorityPrefix по умолчанию "SCOPE_" на ""
     * В результате роли в GrantedAuthority записываются в формате ROLE_USER
     * Бин работает уже только по тому, что он создан с таким именем и помечен аннотацией @Bean
     * прописывать его отдельно в securityFilterChain не нужно
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        /*
        Этот код тоже работает. Он полностью заменяет JwtGrantedAuthoritiesConverter
        converter.setJwtGrantedAuthoritiesConverter(
            jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            // Извлекаем из токена scope и преобразуем содержимое в GrantedAuthority
            // Роли в scope должны лежать в формате ROLE_USER через пробел
            if (jwt.getClaim("scope") != null) {
                String scopes = jwt.getClaim("scope");
                Arrays.stream(scopes.split(" "))
                    .forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            }
            log.info(authorities);
            return authorities;
        });*/
        return converter;
    }
}
