package com.wolox.training.processor;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.models.Book;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class BookOpenLibraryProcessor implements Processor {

  private ObjectMapper maper = new ObjectMapper();

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
    exchange.getIn().setBody("");
    exchange.getIn().setBody(book);
    exchange.setProperty("authors", authorsOpen);
  }
}
