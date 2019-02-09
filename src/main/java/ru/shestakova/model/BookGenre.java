package ru.shestakova.model;

import java.util.stream.Stream;

public enum BookGenre {
  NONE(-1, ""),
  HORROR(0, "Хоррор"),
  ADVENTURE(1, "Путешествие"),
  SCIENCE_FICTION(2, "Научная фантастика");

  private int ordinal;
  private String gengeName;

  BookGenre(int ordinal, String gengeName) {
    this.ordinal = ordinal;
    this.gengeName = gengeName;
  }

  public static BookGenre fromOrdinal(int ordinal) {
    return Stream.of (values () ).filter (genre -> genre.ordinal == ordinal).findFirst ().get ();
  }

  public int getOrdinal() {
    return ordinal;
  }

  public String getGengeName() {
    return gengeName;
  }

  @Override
  public String toString() {
    return gengeName;
  }
}
