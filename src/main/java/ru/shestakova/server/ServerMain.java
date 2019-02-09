package ru.shestakova.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import ru.shestakova.model.Book;


public class ServerMain extends Thread {

  private static final int SERVER_PORT = 32283;

  private Socket socket;
  private ApplicationAPI api;

  public ServerMain(Socket socket, ApplicationAPI api) {
    this.socket = socket;
    this.api = api;

    setDaemon(true);
    setPriority(NORM_PRIORITY);
    start();
  }

  public ServerMain(Socket socket, Deque<Book> deque) {
    this(socket ,new ServerApplicationAPI(deque));
  }

  public ServerMain(Socket socket) {
    this(socket, new ConcurrentLinkedDeque<>());
  }

  public static void main(String args[]) {
    try {
      ServerSocket server = new ServerSocket(SERVER_PORT, 0,
          InetAddress.getByName("localhost"));

      System.out.println("Started server at port: " + SERVER_PORT);

      while (true) {
        new ServerMain(server.accept());
        Thread.sleep(50);
      }
    } catch (Exception e) {
      System.out.println("init error: " + e);
    }
  }

  @Override
  public void run() {
    try {
      ConsoleManagerServer cm = new ConsoleManagerServer(socket, api);
      cm.readConsole();
    } catch (Exception ex) {
      System.err.println("Initialization error: " + ex.getMessage());
    }
  }

  public ApplicationAPI getApi() {
    return api;
  }
}

