package ru.shestakova.model;

import java.awt.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum BookGenre {
  NONE(-1,() -> {return LabLocale.getInstance().getString("");}),
  HORROR(0,() -> {return LabLocale.getInstance().getString("Хоррор");}),
  ADVENTURE(1,() -> {return LabLocale.getInstance().getString("Путешествие");}),
  SCIENCE_FICTION(2,() -> {return LabLocale.getInstance().getString("Научная фантастика");});

  private Supplier<String> gengeName;
  private int ordinal;


  BookGenre(int ordinal,Supplier<String> gengeName) {
    this.gengeName = gengeName;
    this.ordinal= ordinal;
  }


  @Override
  public String toString() {
    return getGengeName ();
  }


  public static BookGenre fromOrdinal(int ordinal) {
    return Stream.of (values () ).filter (genre -> genre.ordinal == ordinal).findFirst ().get ();
  }

  public int getOrdinal() {
    return ordinal;
  }

  public String getGengeName() {
    return gengeName.get ();
  }


}
