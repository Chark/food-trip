package io.chark.food.app.authentication;

import io.chark.food.app.account.AccountService;
import io.chark.food.domain.authentication.permission.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_MODERATOR;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
            .antMatchers(
                    "/",
                    "/home",
                    "/register",
                    "/images/**",
                    "/articles/**",
                    "/compiled/**")
                .permitAll()
                .antMatchers("/moderate/**").hasRole(ROLE_MODERATOR.getName())
            .anyRequest()
            .authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
        // @formatter:on
    }

    @Override
    protected AccountService userDetailsService() {
        return accountService;
    }
}