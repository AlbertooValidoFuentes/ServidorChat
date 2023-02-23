package server.threads;

import java.io.*;
import java.net.*;
import java.util.*;

import server.Server;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private String usuario;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
            usuario = in.readLine();
            System.out.println("Usuario " + usuario + " conectado");

        
            out.println("Lista de mensajes:");

            for (String message : Server.mensajes) {
                out.println(message);
            }

            out.println("Fin de la lista");
        
            while (true) {
            
                String mensaje = in.readLine();
            
                if (mensaje.equals("bye")) {
                    out.println("goodbye");
                    break;
                }

                if (mensaje.startsWith("message:")) {
                
                    String text = mensaje.substring(8);
                
                    String time = String.format("[%tT]", new Date());

                
                    String mensajeFormateado = time + " <" + usuario + ">: " + text;
                
                    Server.mensajes.add(mensajeFormateado);

                    for (Socket socket : Server.clientes) {
                        PrintWriter out2 = new PrintWriter(socket.getOutputStream(), true);
                        out2.println(mensajeFormateado);
                    }
                } else {
                    out.println("Mensaje no válido");
                }
            }

            clientSocket.close(); // Cierre
            System.out.println("Usuario " + usuario + " desconectado");

            Server.clientes.remove(clientSocket);

        } catch (IOException e) {
        
            e.printStackTrace();
        }
    }
}