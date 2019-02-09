package ru.shestakova.client;


import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.codehaus.jettison.json.JSONException;
import ru.shestakova.model.Collection;
import ru.shestakova.server.ApplicationAPI;
import ru.shestakova.utils.JSONUtils;

public class ConsoleManager {

  private ApplicationAPI api;
  private boolean running;

  {
    running = true;
  }

  public ConsoleManager(ApplicationAPI api) {
    this.api = api;
  }

  public int readConsole() {
    Scanner sc = new Scanner(System.in);

    Collection collection = api.getCollection();
    int exitStatus = 0;
    String fileName;

    System.out.println(collection.getSize() + " elements were read");

    while (running) {
      System.out.print("> ");
      String line;

      try {
        line = sc.nextLine();
      } catch (NoSuchElementException nsee) {
        break;
      }

      String[] data = line.split(" ");
      String command = data[0];
      String[] args = Arrays.copyOfRange(data, 1, data.length);
      switch (command) {
        case "connect":
          if (args.length == 0) {
            System.err.println("You should provide server IP");
          }

          // connect 192.168.1.1 2282
          try {
            ServerConnecter connecter =
                new ServerConnecter(args[0], Integer.parseInt(args[1]));

            Socket socket = connecter.getSocket();
            api = new ClientApplicationAPI(socket, null);

            System.out.println(
                "Connection to server "
                    + args[0] + ":" + args[1]
                    + " was successfully established"
            );
          } catch (Exception ex) {
            System.err.println("Could not connect to server");
          }
          break;

        case "info":
          collection = api.getCollection();
          printCollectionInfo(collection);
          break;

        case "get":
          collection = api.getCollection();
          System.out.println("Got collection from server. Its size is " + collection.getSize());
          break;

        case "clear":
          api.clear();
          System.out.println("Collection was cleared");
          break;

        case "load":
          fileName = (args.length >= 1 ? args[0] : null);
          collection = api.load(fileName);
          collection = api.getCollection();

          if (collection != null) {
            System.out.println(collection.getSize() + " elements read");
          } else {
            System.err.println("There are some errors happened during collection reading!");
          }
          break;

        case "save":
          fileName = (args.length >= 1 ? args[0] : null);
          api.save(fileName);

          System.out.println("Data has been written");
          break;

        case "import":
          fileName = args.length >= 1 ? args[0] : null;
          api.importOne(fileName);

          collection = api.getCollection();
          System.out.println("Imported " + collection.getSize() + " elements");
          break;

        case "add":
          try {
            api.add(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "add_if_max":
          try {
            api.addIfMax(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "add_if_min":
          try {
            api.addIfMin(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "remove_all":
          try {
            api.removeAll(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "remove_one":
          try {
            api.removeOne(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "remove_first":
          api.removeFirst();
          printCollectionInfo(api.getCollection());
          break;

        case "remove_last":
          api.removeLast();
          printCollectionInfo(api.getCollection());
          break;

        case "remove_lower":
          try {
            api.removeLower(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "remove_greater":
          try {
            api.removeGreater(JSONUtils.parseBook((args.length >= 1 ? args[0] : null)));
          } catch (JSONException e) {
            System.err.println("Could not parse " + args[0]);
            System.err.println("Try typing JSON-object one more time");
          }
          break;

        case "exit":
          try {
            exitStatus = (args.length >= 1 ? Integer.parseInt(args[0]) : 0);
          } catch (NumberFormatException ex) {
            exitStatus = 0;
          }
          fileName = (args.length >= 2 ? args[1] : null);

          System.out.println("Saving collection to " + (fileName == null ? "default file" : fileName));
          api.save(fileName);

          System.out.println("Exiting with status " + exitStatus + "...");
          try {
            Thread.sleep(2 * 1000L);
            api.exit( exitStatus );
            return 0;
          } catch (InterruptedException e) {
            exitStatus = 0;
            System.out.println("Exiting was interrupted");
          }
          break;

        default:
          System.out.println("There's no such a command: " + command);
      }
    }

    return exitStatus;
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


