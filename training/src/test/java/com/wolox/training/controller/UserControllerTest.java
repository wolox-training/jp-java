package com.wolox.training.controller;

import com.wolox.training.models.User;
import java.time.LocalDate;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class UserControllerTest {

  @Autowired
  private CamelContext camelContext;

  private User user;

  @Test
  public void testUser() {
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(user);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:saveUser", exchange);
    Message message = exchange.getIn();
    User userSaved = (User) message.getBody();
    in.setHeader("id", userSaved.getId());
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:usersearch", exchange);
    Message messageSearch = exchange.getIn();
    assertThat("Jhovanny").isEqualTo(((User)messageSearch.getBody()).getName());
  }

  @Test
  public void saveUser() {
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(user);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:saveUser", exchange);
    Message message = exchange.getIn();
    User userSaved = (User)message.getBody();
    assertThat(userSaved.getId()).isNotNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void userNotSave() {
    user.setUsername("test");
    user.setName(null);
    user.setBirthdate(null);
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(user);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:saveUser", exchange);
  }

  @Before
  public void getUser() {
    user = new User();
    user.setName("Jhovanny");
    user.setUsername("jhovannywolox");
    user.setBirthdate(LocalDate.of(1986, 3, 19));
  }
}
