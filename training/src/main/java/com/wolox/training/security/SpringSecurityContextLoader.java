package com.wolox.training.security;

import com.wolox.training.exceptions.IllegalArgumentBookException;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityContextLoader extends AbstractSpringSecurityContextLoader {

  @Autowired
  private BookAuthenticationProvider authenticationProvider;

  public void process(@Header("Authorization") String authHeader, Exchange exchange)
      throws IllegalArgumentBookException {
    super.handledRequest(authHeader, exchange, authenticationProvider);
  }

  @Override
  protected UsernamePasswordAuthenticationToken handleAuthentication(String[] usernameAndPassword,
      Exchange exchange, AuthenticationProvider authProvider) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
        = super.handleAuthentication(usernameAndPassword, exchange, authProvider);
    return usernamePasswordAuthenticationToken;
  }
}
