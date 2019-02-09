package ru.shestakova.gui;

import java.awt.Color;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import ru.shestakova.model.Book;

public abstract class AbstractBookComponent extends Pane implements UpdatableComponent {

  protected Book book;
  protected Color color;

  public AbstractBookComponent(Book book) {
    this.book = book;
  }

  @Override
  public abstract void updateComponent();

  public void setSize(double width, double height) {
    setPrefSize(width, height);
//    setMinSize(width, height);
//    setMaxSize(width, height);
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
    setBackground(new Background(new BackgroundFill(
            javafx.scene.paint.Color.rgb(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                1.0
            ),
            CornerRadii.EMPTY,
            Insets.EMPTY)
        )
    );
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    Objects.requireNonNull(book, "UpdatableComponent's book should not be null");

    this.book = book;
  }
}
