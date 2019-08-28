package com.wolox.training.security;

import com.wolox.training.exceptions.IllegalArgumentBookException;
import com.wolox.training.utils.HttpUtil;
import javax.security.auth.Subject;
import org.apache.camel.Exchange;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AbstractSpringSecurityContextLoader {

  protected void handledRequest(String authHeader, Exchange exchange, AuthenticationProvider authProvider)
      throws IllegalArgumentBookException {

    if (authHeader == null) {
      throw new IllegalArgumentBookException("an error occurred when decoding the request token", 500);
    }

   if (authProvider == null) {
     throw new IllegalArgumentBookException("An error occurred while trying to authenticate the requesting user with the service", 500);
   }
    String[] usernameAndPassword = HttpUtil.authorizationHeader(authHeader);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
    try {
      usernamePasswordAuthenticationToken = handleAuthentication(usernameAndPassword, exchange, authProvider);
    } catch (Exception e) {
      throw new IllegalArgumentBookException("an error occurred authenticating the user",500);
    }

    Subject subject = new Subject();
    subject.getPrincipals().add(usernamePasswordAuthenticationToken);

    exchange.getIn().setHeader(Exchange.AUTHENTICATION, subject);
  }

  protected UsernamePasswordAuthenticationToken handleAuthentication(String[] usernameAndPassword,
      Exchange exchange,
      AuthenticationProvider authProvider) {
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(usernameAndPassword[0], usernameAndPassword[1]);
    return (UsernamePasswordAuthenticationToken) authProvider.authenticate(token);
  }
}
