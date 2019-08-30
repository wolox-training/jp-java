package com.wolox.training.security;

import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BookAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userName = authentication.getName();
    String pass = authentication.getCredentials().toString();

    User user = userRepository.findFirstByUsername(userName);
    if (user == null || !passwordEncoder.matches(pass, user.getPassword())) {
      throw new
          BadCredentialsException("Invalid credentials");
    } else {
      List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
      grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
      return new UsernamePasswordAuthenticationToken(userName, pass, grantedAuthorities);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
