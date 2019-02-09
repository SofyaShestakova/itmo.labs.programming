package ru.shestakova.server;

import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;

public interface ApplicationAPI {

  Collection getCollection();

  void add(Book book);

  void addIfMax(Book book);

  void addIfMin(Book book);

  void save(String fileName);

  void clear();

  void importOne(String path);

  Collection load(String fileName);

  void removeOne(Book book);

  void removeAll(Book book);

  void removeGreater(Book book);

  void removeLower(Book book);

  void removeFirst();

  void removeLast();

  void exit(int status);
}
