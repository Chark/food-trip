package io.chark.food.app.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth
            .userDetailsService(accountService)
            .passwordEncoder(passwordEncoder);
        // @formatter:on
    }
}