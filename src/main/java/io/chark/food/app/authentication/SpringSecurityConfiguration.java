package io.chark.food.app.authentication;

import io.chark.food.app.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_ADMIN;
import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_MODERATOR;
import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation()
                    .migrateSession()
                    .maximumSessions(10)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry)
                    .and()
                .and()
            .authorizeRequests()
            .antMatchers(
                    "/",
                    "/home",
                    "/register",
                    "/fonts/**",
                    "/images/**",
                    "/articles/**",
                    "/restaurant/**",
                    "/threads/**",
                    "/compiled/**")
                .permitAll()
                .antMatchers("/articles/new").hasRole(ROLE_USER.getName())
                .antMatchers("/administrate/**").hasRole(ROLE_ADMIN.getName())
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