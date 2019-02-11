package ru.shestakova.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.awt.Color;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


public class Book implements Serializable {
  private String name;
  private String author;

  private int height;
  private int  width;
  private  int pages;


  private Color color;
  private ZonedDateTime creationTime;
  private BookGenre genre;


  public Book (String name, String author, int pages, BookSize size, BookColor color, BookGenre genre, ZonedDateTime creationTime) {
    this(name, author, pages, size.getHeight(), size.getWidth(), color.getColor(), genre,creationTime);
  }

  public Book(String name, String author, int pages, int height, int width,
      Color color, BookGenre genre, ZonedDateTime creationTime) {
    this.name = name;
    this.author = author;
    this.height = height;
    this.width = width;
    this.pages = pages;
    this.color = color;
    this.genre = genre;
    this.creationTime = creationTime;
  }
  Book(){
    creationTime = ZonedDateTime.now ();
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

  public ZonedDateTime getCreationTime() {
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

