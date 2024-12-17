package src.com.Ravindu.javachatapp;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
  // List to keep track of all connected clients
  private static List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) throws IOException {
      ServerSocket serverSocket = null;
      try {
          serverSocket = new ServerSocket(5000);
          System.out.println("Server started. Waiting for clients...");

          while (true) {
              Socket clientSocket = serverSocket.accept();
              System.out.println("Client connected: " + clientSocket);

              // Spawn a new thread for each client
              ClientHandler clientThread = new ClientHandler(clientSocket, clients);
              clients.add(clientThread);
              new Thread(clientThread).start();
          }
      } catch (IOException e) {
          System.out.println("Error occurred while accepting client connections: " + e.getMessage());
      } finally {
          try {
              // Close the server socket when the server shuts down
              if (serverSocket != null && !serverSocket.isClosed()) {
                  serverSocket.close();
                  System.out.println("Server socket closed.");
              }
          } catch (IOException e) {
              System.out.println("Error occurred while closing server socket: " + e.getMessage());
          }
      }
  }
}

class ClientHandler implements Runnable {
  private Socket clientSocket;
  private List<ClientHandler> clients;
  private PrintWriter out;
  private BufferedReader in;

  public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
      this.clientSocket = socket;
      this.clients = clients;
      this.out = new PrintWriter(clientSocket.getOutputStream(), true);
      this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

  public void run() {
      try {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
              // Broadcast message to all clients
              for (ClientHandler aClient : clients) {
                  aClient.out.println(inputLine);
              }
          }
      } catch (IOException e) {
          System.out.println("An error occurred: " + e.getMessage());
      } finally {
          try {
              in.close();
              out.close();
              clientSocket.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
}
