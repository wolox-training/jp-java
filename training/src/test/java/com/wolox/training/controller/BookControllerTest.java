package com.wolox.training.controller;

import static org.assertj.core.api.Assertions.*;
import com.wolox.training.models.Book;
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

  @Test
  public void testBook() {
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setHeader("id", 5);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:booksearch", exchange);
    Message message = exchange.getIn();
    assertThat(5l).isEqualTo(((Book)message.getBody()).getId());
  }

  @Test
  public void saveBook() {
    Book book = new Book();
    book.setGenre("thriller");
    book.setIsbn("1245454");
    book.setPages(200);
    book.setPublisher("Amazon");
    book.setSubtitle("Amazon sub title");
    book.setYear("2019");
    book.setAuthor("wolox");
    book.setTitle("Front end basics");
    book.setImage("image/frotn.png");
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(book);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:savebook", exchange);
    Message message = exchange.getIn();
    Book bookSaved = (Book)message.getBody();
    Message inDb = new DefaultMessage(camelContext);
    inDb.setHeader("id", book.getId());
    exchange.setIn(inDb);
    exchange = camelContext.createProducerTemplate().send("direct:booksearch", exchange);
    Message messageDb = exchange.getIn();
    assertThat(book.getId()).isEqualTo(((Book)messageDb.getBody()).getId());
  }
}
