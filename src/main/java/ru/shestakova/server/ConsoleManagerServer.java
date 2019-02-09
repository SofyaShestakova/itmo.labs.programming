package ru.shestakova.server;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.codehaus.jettison.json.JSONException;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;
import ru.shestakova.utils.JSONUtils;

public class ConsoleManagerServer {

  private ApplicationAPI api;
  private Socket socket;

  private boolean running = true;

  public ConsoleManagerServer(Socket socket, ConcurrentLinkedDeque<Book> deque) {
    this(socket, new ServerApplicationAPI(deque));
  }

  public ConsoleManagerServer(Socket socket, ApplicationAPI api) {
    this.api = api;
    this.socket = socket;
  }

  public void readConsole() throws JSONException {
    System.out.println(api.getCollection().getSize() + " elements of file were read");

    while (this.running) {
      String line;

      try {
        byte buf[] = new byte[64 * 1024];

        try {
          socket.getInputStream().read(buf);
        } catch (SocketException ex) {

        }

        ArrayList<Byte> bytes = new ArrayList<>();
        for (byte b : buf) {
          if (0 != b) {
            bytes.add(b);
          }
        }

        byte buf1[] = new byte[bytes.size()];

        int i = 0;
        for (byte b : bytes) {
          buf1[i++] = b;
        }

        bytes.clear();

        line = new String(buf1, StandardCharsets.UTF_8);

        if (isLineEmpty(line)) {
          continue;
        }

        System.out.print("> ");
        System.out.println(line);
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }

      String[] lineData = line.split(" ");
      if (lineData.length == 0) {
        continue;
      }

      Book book;
      int status;

      String command = lineData[0].toLowerCase();
      String[] args = Arrays.copyOfRange(lineData, 1, lineData.length);
      switch (command) {

        case "info":
          printCollectionInfo(api.getCollection());
          break;

        case "clear":
          api.clear();
          System.out.println("Collection was cleared!");
          break;

        case "load":
          try {
            api.load((args.length == 0) ? null : args[0]);
            writeCollection(socket.getOutputStream(), api.getCollection());
            System.out.println(api.getCollection().getSize()
                + " elements of file were loaded");
          } catch (IOException ex) {
            System.err.println("Error happened during sending collection to client during loading");
          }
          break;

        case "get":
          try {
            writeCollection(socket.getOutputStream(), api.getCollection());
          } catch (IOException e) {
            System.err.println("Collection could not be sent to the client");
            e.printStackTrace();
          }
          break;

        case "save":
          api.save((args.length == 0) ? null : args[0]);
          System.out.println("Collection was successfully saved!");
          break;

        case "import":
          api.importOne((args.length == 0) ? null : args[0]);
          break;

        case "add":
          book = JSONUtils.parseBook((args.length == 0) ? null : args[0]);
          api.add(book);
          break;

        case "add_if_max":
          book = JSONUtils.parseBook((args.length == 0) ? null : args[0]);
          api.addIfMax(book);
          break;

        case "add_if_min":
          book = JSONUtils.parseBook((args.length == 0) ? null : args[0]);
          api.addIfMin(book);
          break;

        case "remove_all":
          book = JSONUtils.parseBook((args.length == 0) ? null : args[0]);
          api.removeAll(book);
          break;

        case "remove_one":
          book = JSONUtils.parseBook((args.length == 0) ? null : args[0]);
          api.removeOne(book);
          break;

        case "remove_first":
          api.removeFirst();
          break;

        case "remove_last":
          api.removeLast();
          break;

        case "remove_lower":
          book = JSONUtils.parseBook((args.length >= 1) ? args[0] : null);
          api.removeLower(book);
          break;

        case "remove_greater":
          book = JSONUtils.parseBook((args.length >= 1) ? args[0] : null);
          api.removeGreater(book);
          break;

        case "exit":
          try {
            status = (args.length >= 1 ? Integer.parseInt(args[0]) : 0);
            api.exit(status);
          } catch (NumberFormatException ex) {
            System.out.println("Could not parse exit status: " + args[0]);
          }
          break;

        default:
          System.out.println(" It is not a command!");
      }

      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static void writeCollection(OutputStream to, Collection collection)
      throws IOException {
    to.write(convertToBytes(collection));
  }

  private static byte[] convertToBytes(Object collection) throws IOException {
    try (ByteArrayOutputStream bos1 = new ByteArrayOutputStream()) {
      try (ObjectOutput out = new ObjectOutputStream(bos1)) {
        out.writeObject(collection);
        return bos1.toByteArray();
      }
    }
  }

  private static boolean isLineEmpty(String line) {
    if (line == null) {
      return true;
    }

    if (line.isEmpty()) {
      return true;
    }

    char[] chars = line.toCharArray();
    for (char c : chars) {
      if (c != ' ' && c != '\n') {
        return false;
      }
    }

    return true;
  }

  private static void printCollectionInfo(Collection collection) {
    System.out.println("Collection class: "
        + collection.getClass().getTypeName());
    System.out.println("Collection container: "
        + collection.getBooks().getClass().getTypeName());
    System.out.println("Collection size: "
        + collection.getSize());
  }
}




