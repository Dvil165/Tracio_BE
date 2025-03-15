package com.dvil.tracio.configuration;

import com.dvil.tracio.filter.JwtAuthenticationFilter;
import com.dvil.tracio.service.implementation.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class Security {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final UserDetailsServiceImp userDetailsServiceImp;

    public Security(JwtAuthenticationFilter jwtAuthenticationFilter, OAuth2LoginSuccessHandler oauth2LoginSuccessHandler, UserDetailsServiceImp userDetailsServiceImp) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .authorizeHttpRequests( req ->
                        req.requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated())
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfiguration()))
//                .authorizeHttpRequests(
//                        req -> {
//                            req
//                                    .requestMatchers("/api/auth/**").permitAll()
//                                    .requestMatchers("/api/user/**").hasAuthority("ADMIN")
//                                    .requestMatchers("api/admin/**").hasAuthority("ADMIN")
//                                    .requestMatchers("api/shopowner/**").hasAuthority("SHOP_OWNER")
//                                    .anyRequest()
//                                    .authenticated();
//                        })
//                .oauth2Login(oauth2 -> {
//                    oauth2.successHandler(oauth2LoginSuccessHandler);
//                })
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfiguration()))
//                .authorizeHttpRequests(req -> {
//                    req
//                            .requestMatchers("/login").permitAll()
//                            .requestMatchers("/api/auth/**").permitAll()
//                            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
//                            .requestMatchers("/api/shopowner/**").hasAuthority("SHOP_OWNER")
//                            .anyRequest().authenticated();
//                })
//                .oauth2Login(oauth2 -> oauth2.successHandler(oauth2LoginSuccessHandler))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                        })
//                )
//                .build();
//    }



    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration googleClient = ClientRegistration.withRegistrationId("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .scope("email", "profile")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("email")
                .clientName("Google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/google")
                .build();
        return new InMemoryClientRegistrationRepository(googleClient);
    }

}

