package ru.shestakova.gui.server;

import static ru.shestakova.utils.GUIUtils.createDialogWindow;

import java.util.function.Consumer;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ru.shestakova.gui.AbstractBookComponent;
import ru.shestakova.model.Book;
import ru.shestakova.model.BookColor;

public class BookComponent extends AbstractBookComponent {

  public BookComponent(Book book, Consumer<BookComponent> onClickConsumer) {
    super(book);
    setPrefSize(book.getWidth(), book.getHeight());
    setOnMouseClicked(event -> {
          onClickConsumer.accept(this);
          createDialogWindow("Книга \"" + book.getName() + "\"",
              String.format("Вы выбрали следующую книгу:\n"
                      + "Название: %s\n"
                      + "Автор: %s\n"
                      + "Цвет: %s\n"
                      + "Жанр: %s\n"
                      + "Ширина: %s | "
                      + "Высота: %s\n"
                      + "Кол-во страниц: %s\n"
                      + "Дата создания: %s",
                  book.getName(), book.getAuthor(), BookColor.getByColor(book.getColor()).getColorName(), book.getGenre(), book.getWidth(),
                  book.getHeight(), book.getPages(),book.getCreationTime ())
          ).showAndWait();
        }
    );

    updateComponent();
  }

  @Override
  public void updateComponent() {
    setSize(100, 50);
    setColor(book.getColor());

    Pane pane = new Pane();
    pane.setPrefSize(getPrefWidth(), getPrefHeight());
    Tooltip tooltip = new Tooltip(
        book.getAuthor()
            + " \"" + book.getName() + "\""
            + " | Цвет: " + BookColor.getByColor(book.getColor())
            + " | Жанр: " + book.getGenre().getGengeName()
            + " | Дата создания:" + book.getCreationTime ()
    );
    Tooltip.install(pane, tooltip);

    Text text = new Text();
    text.setFont(Font.font("Arial", 16));
    pane.getChildren().add(text);

    getChildren().add(pane);
  }
}
