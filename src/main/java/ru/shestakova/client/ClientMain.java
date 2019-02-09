package ru.shestakova.client;

import java.util.concurrent.ConcurrentLinkedDeque;
import ru.shestakova.model.Collection;
import ru.shestakova.server.ApplicationAPI;

public class ClientMain extends Thread {

  public static void main(String args[]) {
    int exitCode = -1;
    while (exitCode != 0) {
      Collection collection = new Collection(new ConcurrentLinkedDeque<>());
      ApplicationAPI api = new ClientApplicationAPI(null, collection);

      ConsoleManager cm = new ConsoleManager(api);
      exitCode = cm.readConsole();
    }
  }
}
