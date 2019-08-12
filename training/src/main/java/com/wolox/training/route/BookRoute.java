package com.wolox.training.route;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.processor.ErrorProcessor;
import com.wolox.training.services.BookService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ContextName("books")
public class BookRoute extends RouteBuilder {

  @Autowired
  private BookService bookService;

  @Override
  public void configure() throws Exception {

    onException(BookNotFoundException.class).handled(true)
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).process(new ErrorProcessor());

    restConfiguration()
        .bindingMode(RestBindingMode.json)
        .apiContextPath("/api")
        .contextPath("/rest")
        .apiContextPath("/api")
        .contextPath("/rest")
        .apiProperty("host", "")
        .dataFormatProperty("prettyPrint", "true")
        .enableCORS(true)

        .apiProperty("api.title", "API Book")
        .apiProperty("api.contact.name", "Jhovanny Canas").apiProperty("api.version", "0.0.1");

    rest().description("Wolox Training Book Service")
        .consumes("application/json")
        .produces("application/json")

        .get("/books")
            .description("allows you to consult all the books")
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no books found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .streamCaching().bean(this.bookService, "getAllBook")
            .endRest()

        .get("/books/{id}")
            .description("allows you to search for a book according to id")
            .param().name("id").type(RestParamType.path).description("Id Book")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no books found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .streamCaching().bean(this.bookService, "findBookById")
            .endRest()

        .post("/books")
            .description("allows you to create a book")
            .type(Book.class)
            .responseMessage().code(200).message("OK").endResponseMessage()
            .route().streamCaching().bean(this.bookService, "saveBook")
            .endRest()

        .delete("/books/{id}")
            .description("allows you to delete a book")
            .param().name("id").type(RestParamType.path).description("Id Book")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.bookService, "deleteBook")
            .endRest()

        .put("/books/{id}")
            .description("allows you to update a book")
            .type(Book.class)
            .param().name("id").type(RestParamType.path).description("Id book")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.bookService, "updateBook")
            .endRest();
  }
}
