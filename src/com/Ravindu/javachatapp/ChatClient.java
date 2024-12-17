package src.com.Ravindu.javachatapp;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Consumer<String> onMessageReceived;

  // Constructor for ChatClient
  public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
      this.socket = new Socket(serverAddress, serverPort);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.onMessageReceived = onMessageReceived;
  }

  // Method to send a message to the server
  public void sendMessage(String msg) {
      out.println(msg);
  }

  // Method to start receiving messages from the server
  public void startClient() {
      new Thread(() -> {
          try {
              String line;
              while ((line = in.readLine()) != null) {
                  onMessageReceived.accept(line);  // Process incoming messages
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }).start();
  }

  // Main method to run the client
  public static void main(String[] args) {
      try {
          // Provide the server address and port, along with a callback to handle received messages
          String serverAddress = "localhost";  // Or the server's IP address
          int serverPort = 5000;
          
          // Callback to handle received messages (for example, print them to the console)
          Consumer<String> onMessageReceived = System.out::println;

          // Create a new ChatClient
          ChatClient client = new ChatClient(serverAddress, serverPort, onMessageReceived);
          client.startClient();  // Start the client to receive messages

          // Send a test message (optional)
          client.sendMessage("Hello, Server!");

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}
