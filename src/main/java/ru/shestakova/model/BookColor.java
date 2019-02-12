package ru.shestakova.model;

import java.awt.Color;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum BookColor {

  RED(0,() -> {return  LabLocale.getInstance().getString("Красненький");}, Color.RED),
  BLUE(1,() -> {return  LabLocale.getInstance().getString("Синий");}, Color.BLUE),
  GREEN(2,() -> {return  LabLocale.getInstance().getString("Зеленый");}, Color.GREEN),
  YELLOW(3,() -> {return  LabLocale.getInstance().getString("Желтый");}, Color.YELLOW),
  PINK(4,() -> {return  LabLocale.getInstance().getString("Розовый");}, Color.PINK),
  CYAN(5,() -> {return  LabLocale.getInstance().getString("Магический");}, Color.CYAN),
  MAGENTA(6,() -> {return  LabLocale.getInstance().getString("Загадочный");}, Color.MAGENTA);
  private Supplier<String> colorName;
  private Color color;
  private static int ordinal1;


  BookColor(int ordinal1,Supplier<String> colorName, Color color) {
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

  public  static int getOrdinal1() {
    return ordinal1;
  }
}
