package ru.shestakova.model;

public enum BookSize {
  A4(210, 290),
  A5(145, 210),
  A6(105, 145);

  private int width, height;

  BookSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}