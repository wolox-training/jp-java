package com.wolox.training.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookInputDto {

  private String author;
  private String genre;
  private String image;
  private String isbn;
  private Integer pages;
  private String publisher;
  private String subtitle;
  private String title;
  private String year;
}
