package ru.shestakova.gui.client;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import ru.shestakova.gui.AbstractBookComponent;
import ru.shestakova.model.Book;
import ru.shestakova.model.BookColor;

public class BookComponent extends AbstractBookComponent {

  public BookComponent(Book book) {
    super(book);
    updateComponent();
  }

  @Override
  public void updateComponent() {
    setSize(((double) book.getWidth()) / 5, ((double) book.getHeight()) / 5);
    setColor(book.getColor());

    Pane pane = new Pane();
    pane.setPrefSize(getPrefWidth(), getPrefHeight());
    Tooltip tooltip = new Tooltip(
        book.getAuthor()
            + " \"" + book.getName() + "\""
            + " | Цвет: " + BookColor.getByColor(book.getColor())
            + " | Жанр: " + book.getGenre().getGengeName()
            + " | Дата создания: " + book.getCreationTime ()
    );
    Tooltip.install(pane, tooltip);

    getChildren().add(pane);
  }

  @Override
  public int hashCode() {
    return book.hashCode();
  }
}
