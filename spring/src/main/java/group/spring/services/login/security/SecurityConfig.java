package group.spring.services.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import group.spring.services.login.security.token.Filter;
import group.spring.services.login.security.token.Verify;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private CustomUserDS customUserDS;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .cors().disable()
        .csrf().disable();
        
        http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/login-service/user").permitAll()
        .antMatchers("/api/v1/login-service").permitAll()
        .antMatchers("/api/v1/login-service/*").hasRole("ADMIN")
        .antMatchers("/api/v1/person-service/*").hasRole("USER")
        .anyRequest().authenticated()
        .and()
        .addFilter(new Verify("/api/v1/login-service", authenticationManager()))
        .addFilterBefore(new Filter(), Verify.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDS).passwordEncoder(new BCryptPasswordEncoder());
    }

}
