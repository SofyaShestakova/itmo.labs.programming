package ru.shestakova.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Collection implements Serializable {

  private Deque<Book> storage;

  public Collection() {
    this.storage = new ConcurrentLinkedDeque<>();
  }

  public Collection(Deque<Book> deque) {
    this.storage = deque;
  }

  /**
   * Возвращает коллекцию книг
   *
   * @return Коллекция книг
   */
  public Deque<Book> getBooks() {
    return storage;
  }


  /**
   * Добавляет книгу в колелкцию
   *
   * @param book Объект книги
   */
  public void addBook(Book book) {
    this.storage.add(book);
  }

  /**
   * Добавляет книгу, если количество страниц в ней больше, чем в остальных в коллекции
   *
   * @param book Объект книги
   * @return Статус добавления
   */
  public boolean addBookIfMax(Book book) {
    int max = this.storage.getFirst().getPages();
    for (Book b : this.storage) {
      if (b.getPages() > max) {
        max = b.getPages();
      }
    }

    if (book.getPages() > max) {
      this.storage.add(book);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Добавляет книгу, если количество страниц в ней меньше, чем в остальных в коллекции
   *
   * @param book Объект книги
   * @return Статус добавления
   */
  public boolean addBookIfMin(Book book) {
    int min = this.storage.getFirst().getPages();
    for (Book b : this.storage) {
      if (b.getPages() < min) {
        min = b.getPages();
      }
    }

    if (book.getPages() < min) {
      this.storage.add(book);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Удаляет переданную книгу из коллекции
   *
   * @param book Объект книиг
   */
  public void remove(Book book) {
    this.storage.remove(book);
  }

  /**
   * Удаляет первый элемент из коллекции
   */
  public void removeFirst() {
    this.storage.removeFirst();
  }


  /**
   * Удаляет последний элемент из коллекции
   */
  public void removeLast() {
    this.storage.removeLast();
  }

  /**
   * Удаляет книги с количеством страниц меньшим, чем в преданной книге
   *
   * @param book Объект книги
   */
  public void removeLower(Book book) {
    for (Book b : this.storage) {
      if (b.getPages() < book.getPages()) {
        this.storage.remove(b);
      }
    }
  }

  /**
   * Удаляет книги с количеством страниц большим, чем в преданной книге
   *
   * @param book Объект книги
   */
  public void removeGreater(Book book) {
    for (Book b : this.storage) {
      if (b.getPages() > book.getPages()) {
        this.storage.remove(b);
      }
    }
  }

  /**
   * Удаляет из коллекции книги, эквивалентные переданной
   *
   * @param book Объект книги
   */
  public void removeAllEquivalent(Book book) {
    this.storage.removeAll(Collections.singleton(book));
  }

  /**
   * Ощищает коллекцию книг
   */
  public void clearBooks() {
    this.storage.clear();
  }

  /**
   * Возвращает количество книг в коллекции
   *
   * @return Количество книг
   */
  public int getSize() {
    return this.storage.size();
  }


}
