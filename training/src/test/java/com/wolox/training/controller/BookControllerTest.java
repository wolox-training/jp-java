package com.wolox.training.controller;

import static org.assertj.core.api.Assertions.*;
import com.wolox.training.models.Book;
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

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class BookControllerTest {

  @Autowired
  private CamelContext camelContext;

  private Book book;

  @Test
  public void testBook() {
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(book);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:savebook", exchange);
    Message message = exchange.getIn();
    Book bookSaved = (Book)message.getBody();
    in.setHeader("id", bookSaved.getId());
    exchange = camelContext.createProducerTemplate().send("direct:searchbook", exchange);
    Message messageSearch = exchange.getIn();
    assertThat("Front end basics").isEqualTo(((Book)messageSearch.getBody()).getTitle());
  }

  @Test
  public void saveBook() {
    Exchange exchange = new DefaultExchange(camelContext);
    Message in = new DefaultMessage(camelContext);
    in.setBody(book);
    exchange.setIn(in);
    exchange = camelContext.createProducerTemplate().send("direct:savebook", exchange);
    Message message = exchange.getIn();
    Book bookSaved = (Book)message.getBody();
    assertThat(bookSaved.getId()).isNotNull();
  }

  @Before
  public void getBook(){
    book = new Book();
    book.setGenre("thriller");
    book.setIsbn("1245454");
    book.setPages(200);
    book.setPublisher("Amazon");
    book.setSubtitle("Amazon sub title");
    book.setYear("2019");
    book.setAuthor("wolox");
    book.setTitle("Front end basics");
    book.setImage("image/frotn.png");
  }
}
