package ru.shestakova.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnecter extends Thread {

  private static final long DEFAULT_SLEEP = 1000L;

  Socket socket;
  DataInputStream input;
  DataOutputStream output;

  public ServerConnecter(String host, int port) throws IOException, InterruptedException {
    this(host, port, 1);
  }

  public ServerConnecter(String host, int port, int tries)
      throws IOException, InterruptedException {
    this(host, port, tries, DEFAULT_SLEEP);
  }

  public ServerConnecter(String host, int port, int tries, long sleepMillis)
      throws IOException, InterruptedException {
    if (tries <= 0) {
      throw new IllegalArgumentException("Illegal amount of tries: " + tries);
    }

    if (host == null) {
      throw new IllegalArgumentException("Host is null");
    }

    if (port <= 0) {
      throw new IllegalArgumentException("Port is negative: " + port);
    }

    for (int i = 0; i < tries; ++i) {
      try {
        System.out.println("Trying to reconnect. Try #" + (i + 1));
        socket = new Socket(host, port);
        break;
      } catch (Exception ex) {
        if (i == tries - 1) {
          throw ex;
        } else {
          Thread.sleep(sleepMillis);
        }
      }
    }

    output = new DataOutputStream(socket.getOutputStream());
    input = new DataInputStream(socket.getInputStream());
  }

  public Socket getSocket() {
    return socket;
  }

}