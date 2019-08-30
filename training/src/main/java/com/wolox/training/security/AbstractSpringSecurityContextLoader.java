package com.wolox.training.security;

import com.wolox.training.exceptions.AuthenticationException;
import com.wolox.training.utils.HttpUtil;
import javax.security.auth.Subject;
import org.apache.camel.Exchange;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AbstractSpringSecurityContextLoader {

  protected void handledRequest(String authHeader, Exchange exchange, AuthenticationProvider authProvider)
      throws AuthenticationException {

    if (authHeader == null) {
      throw new AuthenticationException("no authorization data was found in the request", 500);
    }

   if (authProvider == null) {
     throw new AuthenticationException("no validation provider found", 500);
   }
    String[] usernameAndPassword = HttpUtil.authorizationHeader(authHeader);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
    try {
      usernamePasswordAuthenticationToken = handleAuthentication(usernameAndPassword, exchange, authProvider);
    } catch (Exception e) {
      throw new AuthenticationException("problems with user authentication", 401);
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
