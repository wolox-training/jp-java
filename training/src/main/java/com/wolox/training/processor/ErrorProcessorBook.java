package com.wolox.training.processor;

import com.wolox.training.exceptions.BookNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    BookNotFoundException error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BookNotFoundException.class);
    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, error.getCode());
    exchange.getIn().setBody(error.getMessage());
  }
}
