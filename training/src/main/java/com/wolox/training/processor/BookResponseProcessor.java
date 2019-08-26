package com.wolox.training.processor;

import com.wolox.training.dto.BookDto;
import com.wolox.training.models.Book;
import java.util.Arrays;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class BookResponseProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Book book = (Book) exchange.getIn().getBody();
    BookDto bookInput = new BookDto(book);
    bookInput.setAuthors(Arrays.asList(book.getAuthor().split(",")));
    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
    exchange.getIn().setBody(bookInput);
  }
}
