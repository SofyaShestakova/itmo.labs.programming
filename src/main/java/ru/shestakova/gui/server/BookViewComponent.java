package ru.shestakova.gui.server;

import java.util.Objects;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;

public class BookViewComponent extends GridPane {
  private Collection collection;
  private int width;

  private Consumer<BookComponent> onBookComponentClickConsumer;

  public BookViewComponent(Collection collection, int width, Consumer<BookComponent> onBookComponentClickConsumer) {
    Objects.requireNonNull(collection, "Collection should not be null");
    Objects.requireNonNull(onBookComponentClickConsumer, "Book on click consumer should not be null");

    if(width <= 0) {
      throw new IllegalArgumentException("Width should be a natural number");
    }

    this.collection = collection;
    this.width = width;
    this.onBookComponentClickConsumer = onBookComponentClickConsumer;

    setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    setMinWidth(330);
    setMinHeight(375);
    setMaxHeight(375);
  }

  public Collection getCollection() {
    return collection;
  }

  void setWidth(int width) {
    if(width <= 0) {
      return;
    }

    this.width = width;
    updateComponent();
  }

  void setCollection(Collection collection) {
    if(collection == null) {
      this.collection = new Collection();
    }

    this.collection = collection;
  }

  public void updateComponent() {
    getChildren().clear();
    int column = 0, row = -1;
    for (Book book : collection.getBooks()) {
      if (column % width == 0) {
        row++;
      }

      BookComponent bookComponent = new BookComponent(book, onBookComponentClickConsumer);
      add(bookComponent, (column % width), row );
      GridPane.setMargin(bookComponent, new Insets(20, 0, 0, 10));

      column++;
    }
  }
}
