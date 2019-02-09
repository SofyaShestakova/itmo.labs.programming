package ru.shestakova.model;

import java.awt.Color;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum BookColor {

  RED(() -> {return  LabLocale.getInstance().getString("Красненький");}, Color.RED),
  BLUE(() -> {return  LabLocale.getInstance().getString("Синий");}, Color.BLUE),
  GREEN(() -> {return  LabLocale.getInstance().getString("Зеленый");}, Color.GREEN),
  YELLOW(() -> {return  LabLocale.getInstance().getString("Желтый");}, Color.YELLOW),
  PINK(() -> {return  LabLocale.getInstance().getString("Розовый");}, Color.PINK),
  CYAN(() -> {return  LabLocale.getInstance().getString("Магический");}, Color.CYAN),
  MAGENTA(() -> {return  LabLocale.getInstance().getString("Загадочный");}, Color.MAGENTA);
  private Supplier<String> colorName;
  private Color color;


  BookColor(Supplier<String> colorName, Color color) {
    this.colorName = colorName;
    this.color = color;
  }


  @Override
  public String toString() {
    return getColorName ();
  }


  public static BookColor getByColor(Color color) {
    return Stream.of(values())
        .filter(bookColor -> bookColor.getColor().equals(color))
        .findFirst().orElse(null);
  }

  public String getColorName() {
    return colorName.get ();
  }

  public Color getColor() {
    return color;
  }


}
