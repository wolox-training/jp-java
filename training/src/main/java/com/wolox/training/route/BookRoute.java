package com.wolox.training.route;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.IllegalArgumentBookException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.processor.BookResponseProcessor;
import com.wolox.training.processor.ErrorProcessorBook;
import com.wolox.training.processor.ErrorProcessorUser;
import com.wolox.training.processor.SaveBookprocessor;
import com.wolox.training.processor.BookOpenLibraryProcessor;
import com.wolox.training.security.SpringSecurityContextLoader;
import com.wolox.training.security.UserAuthentication;
import com.wolox.training.services.BookService;
import com.wolox.training.services.UserService;
import com.wolox.training.utils.AppConstants;
import org.apache.camel.CamelAuthorizationException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@ContextName("books")
public class BookRoute extends RouteBuilder {

  @Autowired
  private BookService bookService;

  @Autowired
  private UserService userService;

  @Autowired
  private BookOpenLibraryProcessor bookOpenLibraryProcessor;

  @Autowired
  private SaveBookprocessor saveBookprocessor;

  @Autowired
  private BookResponseProcessor bookResponseProcessor;

  @Override
  public void configure() throws Exception {

    onException(BookNotFoundException.class).handled(true)
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).process(new ErrorProcessorBook());

    onException(UserNotFoundException.class).handled(true)
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).process(new ErrorProcessorUser());

    onException(IllegalArgumentBookException.class)
        .handled(true).transform()
        .simple("${exception.message}")
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("500"));

    onException(CamelAuthorizationException.class).handled(true).transform()
        .simple("access denied for security reasons ${exception.policyId}")
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("401"));

    onException(BadCredentialsException.class).handled(true).transform()
        .simple("Problems with Request Authorization Credentials ${exception.message}")
        .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant("401"));

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
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.bookService, "getAllBook")
            .endRest()

        .get("/books/{id}")
            .description("allows you to search for a book according to id")
            .param().name("id").type(RestParamType.path).description("Id Book")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no books found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.bookService, "findBookById")
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
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.bookService, "deleteBook")
            .endRest()

        .put("/books/{id}")
            .description("allows you to update a book")
            .type(Book.class)
            .param().name("id").type(RestParamType.path).description("Id book")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.bookService, "updateBook")
            .endRest()

        .get("/users")
            .description("allows you to consult all the users")
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no users found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.userService, "getAllUser")
            .endRest()

        .get("/users/{id}")
            .description("allows you to search for an user according to id")
            .param().name("id").type(RestParamType.path).description("Id User")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("no user found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage().route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.userService, "findUserById")
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
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.userService, "deleteUser")
            .endRest()

        .put("/users/{id}")
            .description("allows you to update an user")
            .type(User.class)
            .param().name("id").type(RestParamType.path).description("Id User")
            .dataType("long").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.userService, "updateUser")
            .endRest()

        .put("/users/{iduser}/books/{idbook}")
            .description("allows you to add a book to a user's collection")
            .param().name("iduser").type(RestParamType.path).description("Id User")
            .dataType("integer").endParam()
            .param().name("idbook").type(RestParamType.path).description("Id Book")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.userService, "addBook")
            .endRest()

        .delete("/users/{iduser}/books/{idbook}")
            .description("allows you to delete a book from a user's collection")
            .param().name("iduser").type(RestParamType.path).description("Id User")
            .dataType("integer").endParam()
            .param().name("idbook").type(RestParamType.path).description("Id Book")
            .dataType("integer").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.userService, "deleteBook")
            .endRest()
            
        .get("/books/{isbn}")
            .description("allows you to search for a book according to ISBN")
            .param().name("isbn").type(RestParamType.path).description("ISBN of the book")
            .dataType("string").endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(201).message("created").endResponseMessage()
            .responseMessage().code(404).message("Not found").endResponseMessage()
            .route()
            .bean(SpringSecurityContextLoader.class)
            .policy("authenticated_admin")
            .bean(this.bookService, "findBookByIsbn")
            .choice()
                .when(simple("${body?.isbn} == null"))
                    .to("direct:createbook")
                .otherwise()
                    .to("direct:resultsearchbook")
            .end().endRest()

        .get("/username")
            .description("allows you to obtain the name of the authorized user")
            .responseMessage().code(200).message("OK").endResponseMessage()
            .route().streamCaching().bean(this.userService, "getUserAuthenticated").endRest();

        .get("/users/search/")
            .description("allows you to search between a range of birth dates and a name")
            .param().name("before").type(RestParamType.query).description("date before")
            .dataType("string").required(false).endParam()
            .param().name("after").type(RestParamType.query).description("date after")
            .dataType("string").required(false).endParam()
            .param().name("name").type(RestParamType.query).description("name user")
            .dataType("string").required(false).endParam()
            .param().name("page").type(RestParamType.query).description("number page")
            .defaultValue("0").dataType("integer").required(false).endParam()
            .param().name("sort").type(RestParamType.query).description("order param")
            .defaultValue("id").dataType("string").required(false).endParam()
            .param().name("pagesize").type(RestParamType.query).description("page size")
            .defaultValue("2").dataType("string").required(false).endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("user not found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.userService, "getUserByBirthdateAndName")
            .endRest()

        .get("/books/search")
            .description("allows you to search for a book by publisher, genre and year")
            .param().name("publisher").type(RestParamType.query).description("publisher book")
            .dataType("string").required(false).endParam()
            .param().name("genre").type(RestParamType.query).description("genre book")
            .dataType("string").required(false).endParam()
            .param().name("year").type(RestParamType.query).description("year book")
            .dataType("string").required(false).endParam()
            .responseMessage().code(200).message("OK").endResponseMessage()
            .responseMessage().code(404).message("book not found").endResponseMessage()
            .responseMessage().code(500).message("error generating query").endResponseMessage()
            .route().streamCaching().bean(this.bookService, "findBookByPublisherAndGenreAndYear")
            .endRest();

        from("direct:resultsearchbook").streamCaching().process(bookResponseProcessor).end();

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
            .process(bookOpenLibraryProcessor).to("direct:savebookfromlibrary").end();

        from("direct:savebookfromlibrary").streamCaching().bean(this.bookService, "saveBook")
            .process(saveBookprocessor).end();

        from("direct:booknotfound").process(new Processor() {
          @Override
          public void process(Exchange exchange) throws Exception {
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
          }
        }).end();
    
        from("direct:users").bean(this.userService, "getAllUser");
        from("direct:books").bean(this.bookService, "getAllBook");
        from("direct:booksearch").bean(this.bookService, "findBookById");
        from("direct:updatebook").bean(this.bookService, "updateBook");
        from("direct:savebook").bean(this.bookService, "saveBook");
        from("direct:saveUser").bean(this.userService, "saveUser");
        from("direct:usersearch").bean(this.userService, "findUserById");
  }
}
