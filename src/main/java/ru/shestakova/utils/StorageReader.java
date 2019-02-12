package ru.shestakova.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.shestakova.model.Book;
import ru.shestakova.model.BookGenre;
import ru.shestakova.model.Collection;

public class StorageReader {

  private String fileName;

  public StorageReader(String fileName) {
    this.fileName = fileName;
  }

  public Collection readCollection() throws IOException, SAXException {
    Collection collection = new Collection();

    File file = new File(fileName);
    if (!file.exists()) {
      file.createNewFile();
      return collection;
    }

    FileReader reader = new FileReader(file);
    Scanner scanner = new Scanner(reader);

    StringBuilder xml = new StringBuilder();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      xml.append(line);
    }

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = null;
    try {
      dBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      scanner.close();
      reader.close();
    }

    InputSource is = new InputSource(new StringReader(xml.toString()));
    Document doc = Objects.requireNonNull(dBuilder).parse(is);
    doc.getDocumentElement().normalize();

    NodeList child = doc.getDocumentElement().getChildNodes();
    for (int i = 0; i < child.getLength(); i++) {
      Node node = child.item(i);
      NamedNodeMap attrs = node.getAttributes();
      if (attrs == null) {
        continue;
      }

      BookGenre genre = BookGenre.fromOrdinal(Integer.parseInt(attrs.getNamedItem("genre").getTextContent ()));

      String name = attrs.getNamedItem("name").getTextContent();
      String author = attrs.getNamedItem("author").getTextContent();

      int height = Integer.parseInt(attrs.getNamedItem("height").getTextContent());
      int width = Integer.parseInt(attrs.getNamedItem("width").getTextContent());
      int pages = Integer.parseInt(attrs.getNamedItem("pages").getTextContent());
      int rgb = Integer.parseInt(attrs.getNamedItem("rgb").getTextContent());

      Book book = new Book(name, author, pages, height, width, new Color(rgb), genre, LocalDateTime.now ());
      collection.addBook(book);
    }
    scanner.close();
    reader.close();

    return collection;
  }
}
