package com.wolox.training.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wolox.training.dto.BookDto;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.processor.ErrorProcessorBook;
import com.wolox.training.processor.ErrorProcessorUser;
import com.wolox.training.services.BookService;
import com.wolox.training.services.UserService;
import com.wolox.training.utils.AppConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ContextName("books")
public class BookRoute extends RouteBuilder {

  @Autowired
  private BookService bookService;

  @Autowired
  private UserService userService;

  @Override
  public void configure() throws Exception {

    onException(BookNotFoundException.class).handled(true)
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).process(new ErrorProcessorBook());

    onException(UserNotFoundException.class).handled(true)
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).process(new ErrorProcessorUser());

    ObjectMapper maper = new ObjectMapper();
    maper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


    restConfiguration()

        .bindingMode(RestBindingMode.json)
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
            .endRest()

        .get("/users")
            .description("allows you to consult all the users")
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no users found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .streamCaching().bean(this.userService, "getAllUser")
            .endRest()

        .get("/users/{id}")
            .description("allows you to search for an user according to id")
            .param().name("id").type(RestParamType.path).description("Id User")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no user found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .streamCaching().bean(this.userService, "findUserById")
            .endRest()

        .post("/users")
            .description("allows you to create an user ")
            .type(User.class)
            .responseMessage().code(200).message("OK").endResponseMessage()
            .route().streamCaching().bean(this.userService, "saveUser")
            .endRest()

        .delete("/users/{id}")
            .description("allows you to delete an user")
            .param().name("id").type(RestParamType.path).description("Id User")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.userService, "deleteUser")
            .endRest()

        .put("/users/{id}")
            .description("allows you to update an user")
            .type(User.class)
            .param().name("id").type(RestParamType.path).description("Id User")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.userService, "updateUser")
            .endRest()

        .put("/users/{iduser}/books/{idbook}")
            .description("allows you to add a book to a user's collection")
            .param().name("iduser").type(RestParamType.path).description("Id User")
            .dataType("integer").endParam()
            .param().name("idbook").type(RestParamType.path).description("Id Book")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.userService, "addBook")
            .endRest()

        .delete("/users/{iduser}/books/{idbook}")
            .description("allows you to delete a book from a user's collection")
            .param().name("iduser").type(RestParamType.path).description("Id User")
            .dataType("integer").endParam()
            .param().name("idbook").type(RestParamType.path).description("Id Book")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.userService, "deleteBook")
            .endRest()

        .get("/books/{isbn}")
            .description("allows you to search for a book according to ISBN")
            .param().name("isbn").type(RestParamType.path).description("ISBN of the book")
            .dataType("string").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(201).message("created").endResponseMessage()
            .responseMessage().code(404).message("Not found").endResponseMessage()
            .route()
            .streamCaching().bean(this.bookService, "findBookByIsbn")
            .choice()
                .when(simple("${body?.isbn} == null"))
                    .to("direct:createbook")
                .otherwise()
                    .to("direct:resultsearchbook")
            .end().endRest();

        from("direct:resultsearchbook").streamCaching().process(new Processor() {
          @Override
          public void process(Exchange exchange) throws Exception {
            Book book = (Book) exchange.getIn().getBody();
            BookDto bookInput = new BookDto(book);
            bookInput.setAuthors(Arrays.asList(book.getAuthor().split(",")));
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
            exchange.getIn().setBody(bookInput);
          }
        }).end();

        from("direct:createbook")
            .setHeader(Exchange.HTTP_QUERY, simple("bibkeys=ISBN:${header.isbn}&format=json&jscmd=data"))
            .setProperty("isbnbook", simple("${header.isbn}"))
            .removeHeader(Exchange.HTTP_PATH).streamCaching()
            .to(AppConstants.OPEN_LIBRARY_URL)
            .convertBodyTo(String.class)
            .choice()
            .when(body().isEqualTo("{}"))
                .to("direct:booknotfound")
            .otherwise()
                .to("direct:saveopenlibrary")
            .end().endRest();

        from("direct:saveopenlibrary")
            .process(new Processor() {
              @Override
              public void process(Exchange exchange) throws Exception {
                String isbn = (String)exchange.getProperty("isbnbook");
                JsonNode jsonNode = maper.readTree((String)exchange.getIn().getBody());
                JsonNode bookOpen = jsonNode.get("ISBN:" + isbn);
                Book book = new Book();
                book.setIsbn(isbn);
                book.setTitle(bookOpen.get("title").textValue());
                if (bookOpen.findValue("subtitle") != null)
                {
                  book.setSubtitle(bookOpen.get("subtitle").textValue());
                }
                else {
                  book.setSubtitle(bookOpen.get("title").textValue());
                }
                book.setYear(bookOpen.get("publish_date").textValue());
                book.setPages(bookOpen.get("number_of_pages").intValue());
                JsonNode authors = bookOpen.get("authors");
                List<String> authorsOpen = new ArrayList<>();
                for (JsonNode publisher: authors
                ) {
                  authorsOpen.add(publisher.get("name").textValue());
                }
                book.setAuthor(String.join(",", authorsOpen));
                JsonNode publishers = bookOpen.get("publishers");
                String publish = "";
                for (JsonNode publisher: publishers
                ) {
                  publish += (publisher.get("name").textValue() + "-");
                }
                JsonNode jsonNodeImage = bookOpen.get("cover");
                book.setImage(jsonNodeImage.get("small").textValue());
                book.setPublisher(publish);
                exchange.getIn().setBody(simple("${null}"));
                exchange.getIn().setBody(book);
                exchange.setProperty("authors", authorsOpen);
              }
            }).to("direct:savebook").end();

        from("direct:savebook").streamCaching().bean(this.bookService, "saveBook").process(
            new Processor() {
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
            }).end();

        from("direct:booknotfound").process(new Processor() {
          @Override
          public void process(Exchange exchange) throws Exception {
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
            exchange.getIn().setBody("Not found");
          }
        }).end();
  }
}
