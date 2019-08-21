package com.wolox.training.controller;

import static org.assertj.core.api.Assertions.*;

import com.wolox.training.models.User;
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

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class BookControllerTest {

  @Autowired
  private CamelContext camelContext;

  @Test
  public void testAllBook() {
    Exchange exchange = new DefaultExchange(camelContext);
    exchange.setIn(new DefaultMessage(camelContext));
    exchange = camelContext.createProducerTemplate().send("direct:books", exchange);
    Message message = exchange.getIn();
    System.out.println(message.getBody());
    assertThat(6).isEqualTo(((List<User>)message.getBody()).size());
  }
}
