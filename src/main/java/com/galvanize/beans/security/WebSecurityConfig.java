package com.galvanize.beans.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableWebSecurity    // Enable security config. This annotation denotes config for spring security.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final
    JwtProperties jwtProperties;

    public WebSecurityConfig(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterBefore(new JwtTokenAuthenticationFilter(jwtProperties), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // Landing page open to all
//                .antMatchers(HttpMethod.GET, "/hello").permitAll()
                .antMatchers( "/api/users").permitAll()
//                .antMatchers(HttpMethod.GET, "/user").hasRole("USER")
                .antMatchers( "/api/users/{alias}").permitAll()
                // Everything else requires authentication
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Modify these lines for any origins/methods you need to allow CORS for
        //     configuration.setAllowedOrigins(Arrays.asList("http://localhost"));
        configuration.addAllowedOrigin("*");
        // Add the VERBs that the app will need to support
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
