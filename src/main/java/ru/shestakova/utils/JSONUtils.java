package ru.shestakova.utils;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import ru.shestakova.model.Book;
import ru.shestakova.model.BookGenre;

public class JSONUtils {

  public static Book parseBook(String json) throws JSONException {
    if (json == null) {
      return null;
    }

    JSONObject obj;
    try {
      obj = new JSONObject(json);
    } catch (JSONException e) {
      System.err.println("Could not create JSON-object from " + json);
      e.printStackTrace();
      return null;
    }

    String name, author;
    int height, width, pages, rgb;
    BookGenre genre;
    LocalDateTime creationTime;
    try {
      name = obj.getString("name");
      author = obj.getString("author");
      height = obj.getInt("height");
      width = obj.getInt("width");
      pages = obj.getInt("pages");
      rgb = obj.getInt("rgb");
      genre = BookGenre.fromOrdinal (obj.getInt("genre"));
      creationTime = LocalDateTime.now ();

    } catch (JSONException e) {
      System.err.println("Could not find all required fields in given JSON");
      e.printStackTrace();
      return null;
    }

    return new Book(name, author, pages, height, width, new Color(rgb), genre,creationTime);
  }

  public static String toJson(Book book) {
    if (book == null) {
      return "{}";
    }

    JSONObject obj;
    obj = new JSONObject();

    try {
      obj.put("name", book.getName());
      obj.put("author", book.getAuthor());
      obj.put("height", book.getHeight());
      obj.put("width", book.getWidth());
      obj.put("pages", book.getPages());
      obj.put("rgb", book.getColor ().getRGB ());
      obj.put("genre", book.getGenre().getOrdinal());
      obj.put("creationTime", book.getCreationTime ());
    } catch (JSONException e) {
      System.err.println("Could not serialize {" + book + "} to JSON");
      e.printStackTrace();
      return null;
    }

    return obj.toString();
  }
}
