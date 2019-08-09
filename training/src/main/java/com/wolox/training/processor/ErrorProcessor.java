package com.wolox.training.processor;

import com.wolox.training.exceptions.BookException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    BookException error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BookException.class);
    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, error.getCode());
    exchange.getIn().setBody(error.getMessage());
  }
}
