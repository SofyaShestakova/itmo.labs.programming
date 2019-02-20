package ru.shestakova.gui.client;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import ru.shestakova.gui.AbstractBookComponent;
import ru.shestakova.model.Book;
import ru.shestakova.model.BookColor;
import ru.shestakova.model.BookGenre;
import ru.shestakova.model.LabLocale;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public class BookComponent extends AbstractBookComponent {

  public BookComponent(Book book) {
    super(book);
    updateComponent();
  }

  @Override
  public void updateComponent() {
    LabLocale locale = new LabLocale();
  getFormattedDate (locale.getLocale ());
  }
  public void getFormattedDate(Locale currentLocale){
    setSize (((double) book.getWidth ( )) / 5, ((double) book.getHeight ( )) / 5);
    setColor (book.getColor ( ));
    LocalDateTime localDateTime = LocalDateTime.parse (book.getCreationTime ( ).toString ( ));
    DateTimeFormatter formatter = null;

    try {
      formatter = DateTimeFormatter.ofLocalizedDateTime (FormatStyle.SHORT).withLocale (currentLocale);
    } catch (NullPointerException e) {
      formatter = DateTimeFormatter.ofLocalizedDateTime (FormatStyle.SHORT);
    }
    Pane pane = new Pane ( );
    pane.setPrefSize (getPrefWidth ( ), getPrefHeight ( ));
    Tooltip tooltip = new Tooltip (
            book.getAuthor ( )
                    + " \"" + book.getName ( ) + "\""
                    + " | " + LabLocale.getInstance ( ).getString ("Цвет") + " " + BookColor.getByColor (book.getColor ( ))
                    + " | " + LabLocale.getInstance ( ).getString ("Жанр") + " " + book.getGenre ( ).getGengeName ( )
                    + " | " + LabLocale.getInstance ( ).getString ("Дата создания:") + " " + formatter.format (localDateTime)
    );
    Tooltip.install (pane, tooltip);
    getChildren ( ).add (pane);
  }

  @Override
  public int hashCode() {
    return book.hashCode();
  }
}
