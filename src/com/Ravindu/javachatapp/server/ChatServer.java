package src.com.Ravindu.javachatapp.client;
import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("Server started. Waiting for clients..");
        Socket clienSocket = serverSocket.accept();
        System.out.println("Client connected.");
    }
}
