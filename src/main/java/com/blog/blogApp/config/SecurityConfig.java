package com.blog.blogApp.config;

import com.blog.blogApp.security.CustomUserDetailService;
import com.blog.blogApp.security.JwtAuthenticationEntryPoint;
import com.blog.blogApp.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig {

    public static final String[] PUBLIC_URLS={
            "/api/v1/auth/**",
            "/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/bus/v3/api-docs/**",
            "/swagger-ui.html",
            "/api-docs/**"

    };
    @Autowired
    CustomUserDetailService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
         httpSecurity.
                csrf(c-> c.disable())
                .authorizeHttpRequests(
                        req ->{
                            req.requestMatchers(PUBLIC_URLS).permitAll()

                                    .requestMatchers("/users/deleteUser/**").hasAuthority("ADMIN")
                                    .anyRequest().authenticated();
                            ;
                           // req.requestMatchers("/v3/api-docs","/swagger-ui/**").permitAll();

                          // req.requestMatchers("/v3/api-docs").permitAll();



                        }
                )
                .exceptionHandling(e->{
                    e.authenticationEntryPoint(this.jwtAuthenticationEntryPoint);
                })
                .sessionManagement(s->{
                    s.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
                httpSecurity.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
               return httpSecurity.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
    ) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


}
