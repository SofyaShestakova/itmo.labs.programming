package ru.shestakova.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import ru.shestakova.model.Book;
import ru.shestakova.model.Collection;
import ru.shestakova.server.ApplicationAPI;
import ru.shestakova.utils.JSONUtils;

public class ClientApplicationAPI implements ApplicationAPI {

  private static final String DEFAULT_HOST = "localhost";
  private static int DEFAULT_PORT = 32283;

  private String host;
  private int port;

  private Collection collection;
  private Socket socket;

  {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
  }

  public ClientApplicationAPI(Socket socket, Collection collection) {
    if(socket != null) {
      this.host = socket.getInetAddress().getHostName();
      this.port = socket.getPort();
    }

    if(checkSocket(socket)) {
      this.socket = socket;
    } else {
      this.socket = getSocket();
    }

    this.collection = collection;
  }

  public ClientApplicationAPI(String host, int port) {
    this.host = host;
    this.port = port;

    this.socket = getSocket();
    this.collection = getCollection();
  }

  @Override
  public Collection getCollection() {
    try {
      socket.getOutputStream().write("get".getBytes());

      try {
        Thread.sleep(100); // todo before: delay 200ms
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      byte buf[] = new byte[64 * 1024];
      if (socket.getInputStream().available() > 0) {
        socket.getInputStream().read(buf);
        collection = (Collection) convertFromBytes(buf);
      }

    } catch (IOException e) {
//      e.printStackTrace();
    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
    }

    return collection;
  }

  @Override
  public void add(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("add" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void addIfMax(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("add_if_max" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void addIfMin(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("add_if_min" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void save(String fileName) {
    sendCommand("save" + (fileName != null ? " " + fileName : ""));
  }

  @Override
  public void clear() {
    sendCommand("clear");
  }

  @Override
  public void importOne(String path) {
    sendCommand("import" + (path == null ? "" : " " + path));
  }

  @Override
  public Collection load(String fileName) {
    return loadCollection(getSocket(), fileName);
  }

  @Override
  public void removeOne(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("remove_one" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void removeAll(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("remove_all" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void removeGreater(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("remove_greater" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void removeLower(Book book) {
    if (book == null) {
      return;
    }

    sendCommand("remove_lower" + " " + JSONUtils.toJson(book));
  }

  @Override
  public void removeFirst() {
    sendCommand("remove_first");
  }

  @Override
  public void removeLast() {
    sendCommand("remove_last");
  }

  @Override
  public void exit(int status) {
    sendCommand("exit " + status);
  }

  private Socket getSocket() {
    if (!checkSocket(socket)) {
      try {
        if (socket != null && !socket.isClosed()) {
          socket.close();
        }

        ServerConnecter connecter = new ServerConnecter(host, port, 20);
        socket = connecter.getSocket();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return socket;
  }

  private Collection loadCollection(Socket socket, String fileName) {
    try {
      socket.getOutputStream()
          .write(("load" + (fileName == null ? "" : " " + fileName))
              .getBytes());

      byte buf[] = new byte[64 * 1024];
      if (socket.getInputStream().available() > 0) {
        socket.getInputStream().read(buf);
        collection = (Collection) convertFromBytes(buf);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return collection;
  }

  private boolean sendCommand(String command) {
    Socket socket = getSocket();

    try {
      if ("load".equalsIgnoreCase(command)) {
        loadCollection(socket, null);
      } else if (!"get".equalsIgnoreCase(command)) {
        //  getCollection (socket);
        socket.getOutputStream().write(command.getBytes());
      }

      Thread.sleep(300);
      getCollection();
      return true;
    } catch (IOException ex) {

      ServerConnecter connecter;
      try {
        connecter = new ServerConnecter(DEFAULT_HOST, DEFAULT_PORT, 20);
        this.socket = connecter.getSocket();
      } catch (IOException | InterruptedException e) {

        return false;
      }

      return false;
    } catch (InterruptedException e) {
      e.printStackTrace();

      return false;
    }
  }

  private static boolean checkSocket(Socket socket) {
    return (socket != null
        && (socket.isConnected() && !socket.isClosed())
        && (!socket.isInputShutdown() && !socket.isOutputShutdown()));
  }

  private static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis)) {
      return in.readObject();
    }
  }
}
