package readeronline;

import java.io.*;
import java.net.*;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class HiloServidor extends Thread {

    DataInputStream fentrada;
    Socket socket = null;

    public HiloServidor(Socket s) {
        socket = s;

        try {
            // Crear flujo de entrada
            fentrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end constructor

    public void run() {

        // Enviar el historial de mensajes
        String texto = Chat.textarea.getText();
        EnviarMensajes(texto);

        // bucle que recibe lo que el cliente escribe en el chat
        // al salir se le envia un * al servidor
        while (true) {
            String cadena = "";

            try {
                cadena = fentrada.readUTF(); // Lee lo que el cliente escribe

                if (cadena.trim().equals("*")) {
                    Chat.activas -= 1;
                    break; // Salir del bucle
                } // end if

                Chat.textarea.append(cadena + "\n");
                texto = Chat.textarea.getText();
                EnviarMensajes(texto); // Se envia el texto a todos los clientes
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end bucle
    } // end run

    private void EnviarMensajes(String t) {

        // Recorrer tabla de sockets para enviar mensajes a todos
        for (int i = 0; i < Chat.conexiones; i++) {
            Socket s1 = Chat.tabla[i]; // Obtener socket
            try {
                DataOutputStream fsalida = new DataOutputStream(s1.getOutputStream());
                fsalida.writeUTF(t);
            } catch (SocketException se) {
                se.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } // end for
    } // end EnviarMensaje
} // end HiloServidor
