package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import server.threads.ClientHandler;

public class Server {

    public static final int PORT = 1234;
    public static final List<String> mensajes = new CopyOnWriteArrayList<>();
    public static final List<Socket> clientes = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
    
        ServerSocket serverSocket = new ServerSocket(PORT);


        System.out.println("Servidor iniciado en el puerto " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

            clientes.add(clientSocket);
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }
    }
}