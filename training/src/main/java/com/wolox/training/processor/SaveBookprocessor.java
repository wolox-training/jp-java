package com.wolox.training.processor;

import com.wolox.training.dto.BookDto;
import com.wolox.training.models.Book;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class SaveBookprocessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Book book = (Book)exchange.getIn().getBody();
    BookDto bookInput = new BookDto(book);
    List<String> authors = (List<String>) exchange.getProperty("authors");
    bookInput.setAuthors(authors);
    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
    exchange.getIn().setBody(bookInput);
  }
}
