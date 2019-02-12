package ru.shestakova.model;

import java.awt.Color;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class Book implements Serializable {
  private String name;
  private String author;

  private Integer height;
  private Integer width;
  private  Integer pages;


  private Color color;
  private LocalDateTime creationTime;
  private BookGenre genre;


  public Book (String name, String author, Integer pages, BookSize size, BookColor color, BookGenre genre, LocalDateTime creationTime) {
    this(name, author, pages, size.getHeight(), size.getWidth(), color.getColor (), genre,creationTime);
  }

  public Book(String name, String author, Integer pages, Integer height, Integer width,
      Color color, BookGenre genre, LocalDateTime creationTime) {
    this.name = name;
    this.author = author;
    this.height = height;
    this.width = width;
    this.pages = pages;
    this.color= color;
    this.genre = genre;
    this.creationTime = creationTime;
  }

 public  Book(){
    creationTime = LocalDateTime.now ();
  }

  public String getName() {
    return name;
  }

  public String getAuthor() {
    return author;
  }

  public int getPages() {
    return pages;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public Color getColor() {
    return color;
  }

  public BookGenre getGenre() {
    return genre;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Book)) {
      return false;
    }
    Book book = (Book) o;
    return getHeight() == book.getHeight() &&
        getWidth() == book.getWidth() &&
        Objects.equals(getName(), book.getName()) &&
        Objects.equals(getAuthor(), book.getAuthor()) &&
        Objects.equals(getColor(), book.getColor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getAuthor(), getHeight(), getWidth(), getColor(),getCreationTime ());
  }

  /*@Override
  public String toString() {
    return "Книга: " + name + " " + " Автор: " + author + " Кол-во страниц: " + pages;
  }*/

  @Override
  public String toString() {
    return "Book{" +
            "name='" + name + '\'' +
            ", author='" + author + '\'' +
            ", height=" + height +
            ", width=" + width +
            ", pages=" + pages +
            ", color=" + color +
            ", genre=" + genre +
            ", creationTime=" + creationTime +
            '}';
  }
}

