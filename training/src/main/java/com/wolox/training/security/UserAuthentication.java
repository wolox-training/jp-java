package com.wolox.training.security;

import org.springframework.security.core.Authentication;

public interface UserAuthentication {

  Authentication getAuthentication();
}
