package ru.shestakova.model;

import java.awt.Color;
import java.util.stream.Stream;

public enum BookColor {
  RED("Красненький", Color.RED),
  GREEN("Зеленый", Color.GREEN),
  BLUE("Синий", Color.BLUE),
  YELLOW("Желтый", Color.YELLOW),
  PINK("Розовый", Color.PINK),
  CYAN("Магический", Color.CYAN),
  MAGENTA("Загадочный", Color.MAGENTA);

  private String colorName;
  private Color color;

  BookColor(String colorName, Color color) {
    this.colorName = colorName;
    this.color = color;
  }

  public static BookColor getByColor(Color color) {
    return Stream.of(values())
        .filter(bookColor -> bookColor.getColor().equals(color))
        .findFirst().orElse(null);
  }

  public String getColorName() {
    return colorName;
  }

  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return colorName;
  }
}
