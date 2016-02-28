package clientreader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class ClientReader implements ActionListener {

    // Declaramos variables globales
    public String ip = "192.168.1.35";
    public int puerto = 1027;

    DataInputStream fentrada; // Flujo de entrada de datos
    DataOutputStream fsalida; // Flujo de salida de datos
    Socket socket; // Socket para conectarse al servidor
    String nombre = ""; // Nombre de usuario

    // Declaramos el objeto cventana
    public static ClientReaderVentana cventana;

    /**
     * Constructor de la clase ClientReader
     *
     * @param crv Objeto ClientReaderVentana
     */
    public ClientReader(ClientReaderVentana crv) {
        // Vinculamos la ventana con la variable
        this.cventana = crv;

        // Anadimos listeners a los elementos de la ventana
        addListeners();

        // Hacemos la ventana visible
        cventana.setVisible(true);

        // Iniciamos el socket
        iniciarSocket();

        // Pedimos el nombre al Usuario
        pedirNombre();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == cventana.bLibro) { // Boton Libros
                // Enviamos peticion al servidor
                fsalida.writeUTF("libros");
                // Recogemos el listado de libros
                String[] listado = fentrada.readUTF().split("\n");

                // Mostramos una ventana con los libros para que elija el usuario
                // Devolvera la posicion del libro en el array
                int returnValue = JOptionPane.showOptionDialog(null,
                        "Que libro quieres leer?",
                        "Selecciona un libro",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        listado,
                        listado[0]);

                // Enviamos el libro seleccionado al servidor
                fsalida.writeUTF(listado[returnValue]);

                // Recibimos las lineas del libro
                cventana.textarea.setText(fentrada.readUTF());
            } else if (e.getSource() == cventana.bPrevL) {
                fsalida.writeUTF("prev");
                cventana.textarea.setText(fentrada.readUTF() + "\n"
                        + cventana.textarea.getText());
            } else if (e.getSource() == cventana.bNextL) {
                fsalida.writeUTF("next");
                cventana.textarea.setText(cventana.textarea.getText() + "\n"
                        + fentrada.readUTF());
            } else if (e.getSource() == cventana.bSalir) {
                System.out.println("Se ha cerrado el programa");
                fsalida.writeUTF("*");
                socket.close();
                System.exit(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para anadir listeners a los botones
     */
    private void addListeners() {
        cventana.bLibro.addActionListener(this);
        cventana.bPrevL.addActionListener(this);
        cventana.bNextL.addActionListener(this);
        cventana.bSalir.addActionListener(this);
    }

    /**
     * Metodo para crear un hilo con un socket para conectarse al servidor
     */
    private void iniciarSocket() {
        try {
            // Declaracion e inicializacion de socket
            socket = new Socket(ip, puerto);

            // Vinculamos el flujo de entrada del socket con la variable
            fentrada = new DataInputStream(socket.getInputStream());
            // Vinculamos el flujo de salida del socket con la variable
            fsalida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();

            // Mostramos ventana con mensaje de error
            JOptionPane.showMessageDialog(cventana,
                    "No se puede conectar al servidor\n" + ex.getMessage(),
                    "Mensaje de error:1",
                    JOptionPane.ERROR_MESSAGE);

            // Cerramos el programa
            System.exit(0);
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Metodo para pedir el nombre de usuario
     */
    private void pedirNombre() {
        while (nombre.equals("")) {
            nombre = JOptionPane.showInputDialog("Introduce tu nombre de usuario: ");
        }
        try {
            fsalida.writeUTF(nombre);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
