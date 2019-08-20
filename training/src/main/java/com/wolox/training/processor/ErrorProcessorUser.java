package com.wolox.training.processor;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ErrorProcessorUser implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    UserNotFoundException error = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, UserNotFoundException.class);
    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, error.getCode());
    exchange.getIn().setBody(error.getMessage());
  }
}
