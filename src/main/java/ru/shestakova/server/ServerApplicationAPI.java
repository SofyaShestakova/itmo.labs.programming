package ru.shestakova.server;

import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.xml.sax.SAXException;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;
import ru.shestakova.utils.StorageReader;
import ru.shestakova.utils.StorageWriter;

public class ServerApplicationAPI implements ApplicationAPI {

  private final String DEFAULT_COLLECTION_FILE_NAME = "book.xml";

  private Collection collection;

  public ServerApplicationAPI() {
    this(new ConcurrentLinkedDeque<>());
  }

  public ServerApplicationAPI(Deque<Book> deque) {
    this.collection = new Collection(deque);
  }

  @Override
  public void clear() {
    this.collection.clearBooks();
  }

  @Override
  public void save(String fileName) {
    if (fileName == null) {
      fileName = DEFAULT_COLLECTION_FILE_NAME;
    }

    StorageWriter storageWriter = new StorageWriter(fileName);
    try {
      storageWriter.writeCollection(this.collection);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  @Override
  public void removeFirst() {
    this.collection.removeFirst();
//    System.out.println("first element removed");
  }

  @Override
  public void removeLower(Book book) {
    this.collection.removeLower(book);
//    System.out.println("the lowerst element removed");
  }

  @Override
  public void removeAll(Book book) {
    this.collection.removeAllEquivalent(book);
//    System.out.println("elements  removed!");
  }

  @Override
  public void removeOne(Book book) {
    this.collection.remove(book);
//    System.out.println("element removed");
  }

  @Override
  public Collection load(String fileName) {
    if (fileName == null) {
      fileName = DEFAULT_COLLECTION_FILE_NAME;
    }

    StorageReader storageReader = new StorageReader(fileName);
    try {
      collection = storageReader.readCollection();
      return collection;
    } catch (IOException | SAXException e) {
      e.printStackTrace();
      System.exit(0);
    }

    return null;
  }

  @Override
  public void addIfMax(Book book) {
    if (this.collection.addBookIfMax(book)) {
//      System.out.println("Book added!");
    } else {
//      System.out.println("Incorrect form");
    }
  }

  @Override
  public void removeGreater(Book book) {
    this.collection.removeGreater(book);
//    System.out.println("the greatest book removed");
  }

  @Override
  public void removeLast() {
    this.collection.removeLast();
//    System.out.println("last element removed");
  }

  @Override
  public void addIfMin(Book book) {
    if (this.collection.addBookIfMin(book)) {
//      System.out.println("Book added!");
    } else {
//      System.out.println("incorrect form");
    }
  }

  @Override
  public void importOne(String fileName) {
    if (fileName == null) {
      fileName = DEFAULT_COLLECTION_FILE_NAME;
    }

    StorageReader reader = new StorageReader(fileName);

    try {
      Collection collection = reader.readCollection();
      this.collection.getBooks().addAll(collection.getBooks());
//      System.out.println(collection.getSize() + " elements imported");
    } catch (IOException | SAXException e) {
      System.exit(0);
    }
  }

  @Override
  public Collection getCollection() {
    return collection;
  }

  @Override
  public void add(Book book) {
    this.collection.addBook(book);
//    System.out.println("Element added!");
  }

  @Override
  public void exit(int status) {
    System.exit(status);
  }
}
