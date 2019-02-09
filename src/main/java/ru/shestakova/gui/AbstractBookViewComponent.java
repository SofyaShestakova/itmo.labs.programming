package ru.shestakova.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;

public abstract class AbstractBookViewComponent<T extends AbstractBookComponent> extends Pane implements UpdatableComponent {
  protected static final Insets DEFAULT_BOOK_COMPONENTS_MARGIN = new Insets(20, 0, 0, 10);

  protected Collection collection;
  protected boolean collectionChanged = true;

  protected AbstractBookViewComponent() {
    this(new Collection());
  }

  protected AbstractBookViewComponent(Collection collection) {
    this.collection = collection;
  }

  @Override
  public abstract void updateComponent();

  public abstract T getBookComponentFor(Book book);

  public abstract java.util.Collection<T> getBookComponents();

  protected abstract void addBook(Book book);

  public Collection getCollection() {
    return collection;
  }

  public void setCollection(Collection collection) {
    this.collection = collection;
    collectionChanged = true;
  }
}
