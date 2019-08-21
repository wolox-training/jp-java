package com.wolox.training.controller;

import com.wolox.training.models.User;
import java.time.LocalDate;
import java.util.List;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringBootRunner;
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

  @Test
  public void testAllUser() {
    Exchange exchange = new DefaultExchange(camelContext);
    exchange.setIn(new DefaultMessage(camelContext));
    exchange = camelContext.createProducerTemplate().send("direct:users", exchange);
    Message  message = exchange.getIn();
    System.out.println(message.getBody());
    assertThat(4).isEqualTo(((List<User>)message.getBody()).size());
  }

  @Test
  public void testUser() {
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setHeader("id", 4);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:usersearch", exchange);
    Message message = exchange.getIn();
    assertThat(4l).isEqualTo(((User)message.getBody()).getId());
  }

  @Test
  public void saveUser() {
    User user = new User();
    user.setName("Jhovanny");
    user.setUsername("jhovannywolox");
    user.setBirthdate(LocalDate.of(1986, 3, 19));
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(user);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:saveUser", exchange);
    Message message = exchange.getIn();
    User UserSaved = (User)message.getBody();
    Message inDb = new DefaultMessage(camelContext);
    inDb.setHeader("id", user.getId());
    exchange.setIn(inDb);
    exchange = camelContext.createProducerTemplate().send("direct:usersearch", exchange);
    Message messageDb = exchange.getIn();
    assertThat(user.getId()).isEqualTo(((User)messageDb.getBody()).getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void userNotSave() {
    User user = new User();
    user.setUsername("test");
    user.setName(null);
    user.setBirthdate(null);
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(user);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:saveUser", exchange);
  }
}
