package com.wolox.training;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class TrainingApplication {

  public static void main(String[] args) {
    SpringApplication.run(TrainingApplication.class, args);
  }

  @Bean
  public ServletRegistrationBean servletRegistrationBean() {
    ServletRegistrationBean register = new ServletRegistrationBean(new CamelHttpTransportServlet(),
        "/rest/*");
    register.setName("CamelServlet");
    return register;
  }
}
