package ru.shestakova.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;

public class StorageWriter {

  private String fileName;

  public StorageWriter(String fileName) {
    this.fileName = fileName;
  }

  public void writeCollection(Collection collection) throws IOException {
    File file = new File(this.fileName);

    if (!file.exists()) {
      file.createNewFile();
    }

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element root = doc.createElement("books");

      for (Book book : collection.getBooks()) {
        Element e = doc.createElement("book");
        e.setAttribute("name", book.getName());
        e.setAttribute("author", book.getAuthor());
        e.setAttribute("height", String.valueOf(book.getHeight()));
        e.setAttribute("width", String.valueOf(book.getWidth()));
        e.setAttribute("pages", String.valueOf(book.getPages()));
        e.setAttribute("rgb", String.valueOf(book.getColor().getRGB()));
        e.setAttribute("genre", String.valueOf(book.getGenre().getOrdinal ()));
        e.setAttribute ("creationTime", String.valueOf (book.getCreationTime ()));

        root.appendChild(e);
      }
      doc.appendChild(root);

      try {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        // send DOM to file
        tr.transform(new DOMSource(doc),
            new StreamResult(new FileOutputStream(file)));

      } catch (TransformerException | IOException te) {
        System.out.println(te.getMessage());
      }
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }

  }
}
