package com.wolox.training.security;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.component.spring.security.SpringSecurityAccessPolicy;
import org.apache.camel.component.spring.security.SpringSecurityAuthorizationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig  extends WebSecurityConfigurerAdapter{

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
  }

  @Bean
  public AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
    decisionVoters.add(new RoleVoter());
    return new AffirmativeBased(decisionVoters);
  }

  @Bean(name = "authenticated_admin")
  public SpringSecurityAuthorizationPolicy admin_access() throws Exception {
    SpringSecurityAuthorizationPolicy policy = new SpringSecurityAuthorizationPolicy();
    policy.setId("admin_access");
    policy.setAuthenticationManager(authenticationManagerBean());
    policy.setAccessDecisionManager(accessDecisionManager());
    policy.setSpringSecurityAccessPolicy(new SpringSecurityAccessPolicy("ROLE_ADMIN"));
    return policy;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

