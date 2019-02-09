package ru.shestakova.gui.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import ru.shestakova.gui.AbstractBookViewComponent;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;

public class BookViewComponent extends AbstractBookViewComponent<BookComponent> {

  private Map<Book, BookComponent> components;

  private int width;

  private int currentColumn;
  private int currentRow;

  private GridPane grid;

  public BookViewComponent(Collection collection, int width) {
    Objects.requireNonNull(collection, "Collection should not be null");

    if (width <= 0) {
      throw new IllegalArgumentException("Width should be a natural number");
    }

    this.components = new HashMap<>(collection.getSize());
    this.collection = collection;
    this.width = width;

    grid = new GridPane();
    grid.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

    setMinWidth(330);
    setMinHeight(375);
    setMaxHeight(375);
    grid.setMinWidth(330);
    grid.setMinHeight(375);
    grid.setMaxHeight(375);

    getChildren().add(grid);

    updateComponent();
  }

  public void updateComponent() {
    if (collectionChanged) {
      grid.getChildren().clear();

      currentRow = 0;
      currentColumn = 0;
      collection.getBooks().forEach(this::addBook);
      collectionChanged = false;
    } else {
      getBookComponents().forEach(BookComponent::updateComponent);
    }
  }

  @Override
  public BookComponent getBookComponentFor(Book book) {
    return components.get(book);
  }

  @Override
  public java.util.Collection<BookComponent> getBookComponents() {
    return components.values();
  }

  @Override
  protected void addBook(Book book) {
    if (currentColumn % width == 0) {
      currentRow++;
    }

    BookComponent bookComponent = new BookComponent(book);
    components.put(bookComponent.getBook(), bookComponent);

    grid.add(bookComponent, (currentColumn % width), currentRow);
    GridPane.setMargin(bookComponent, DEFAULT_BOOK_COMPONENTS_MARGIN);

    currentColumn++;
  }
}
