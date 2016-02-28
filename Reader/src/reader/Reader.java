package reader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Reader implements ActionListener {

    // Variables globales
    public static int maxConexiones = 2; // Numero total de conexiones
    public static int actConexiones = 0; // Conexiones actuales

    public String ip = "192.168.1.35"; // Ip del servidor
    public int puerto = 1027; // Puerto del servidor

    public static ServerSocket servidor; // Socket servidor

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // Formato de fecha

    public static ArrayList<Socket> sockets = new ArrayList<Socket>(); // Array de sockets
    public static ArrayList<String> nombres = new ArrayList<String>(); // Array de Strings
    public static ArrayList<HiloServidor> hilos = new ArrayList<HiloServidor>(); // Array de hilos

    // Declaramos el objeto ReaderVentana
    public static ReaderVentana ventana = null; // Objeto ReaderVentana
    public static Historial historial; // Objeto Historial

    /**
     * Constructor de la clase Reader
     *
     * @param rv Objeto ReaderVentana
     */
    public Reader(ReaderVentana rv) {
        this.ventana = rv;

        historial = new Historial();

        // Anadimos listeners a los elementos de la ventana
        addListeners();

        // Hacemos la ventana visible
        ventana.setVisible(true);

        try {
            historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime())
                    + " Servidor iniciado");

            // Inicializamos socket servidor
            servidor = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress(ip, puerto);
            servidor.bind(addr);

            // Iniciamos los sockets del servidor
            for (int i = 0; i < maxConexiones; i++) {
                iniciarSocketServidor(i);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ventana.bHisto) { // Boton historial
            ventana.textarea.setText(historial.mostrarHistorial());
        } else if (e.getSource() == ventana.bUsers) { // Boton Usuarios
            mostrarUsuarios();
        } else if (e.getSource() == ventana.bConex) { // Boton Conexiones
            cambiarConexiones();
        } else if (e.getSource() == ventana.bSalir) { // Boton Salir
            System.out.println("Se ha cerrado el programa");
            try {
                historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime())
                        + " Servidor finalizado");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    /**
     * Metodo para anadir listeners a los botones
     */
    private void addListeners() {
        ventana.bHisto.addActionListener(this);
        ventana.bUsers.addActionListener(this);
        ventana.bConex.addActionListener(this);
        ventana.bSalir.addActionListener(this);
    }

    /**
     * Metodo para crear hilos con sockets para que los clientes puedan
     * conectarse
     *
     * @throws IOException Excepcion de entrada/salida
     */
    private static void iniciarSocketServidor(int n) throws IOException {

        Socket s = servidor.accept();

        sockets.add(s);

        HiloServidor h = new HiloServidor(sockets.get(n), n, historial);
        hilos.add(h);
        hilos.get(n).start();
    }

    /**
     * Metodo para mostrar los usuarios
     */
    private void mostrarUsuarios() {
        ventana.textarea.setText("Usuarios activos");
        for (int i = 0; i < sockets.size(); i++) {
            if (sockets.get(i).isConnected()) {
                ventana.textarea.setText(ventana.textarea.getText() + "\n" + nombres.get(i));
            }
        }
        try {
            historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime())
                    + " Peticion de servidor de listar usuarios");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para cambiar el numero maximo de conexiones
     */
    private void cambiarConexiones() {
        int num = Integer.parseInt(JOptionPane.showInputDialog("Introduce el maximo de conexiones: "));
        maxConexiones = num;
        try {
            historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime())
                    + " Peticion de servidor de cambiar maximo de conexiones a " + num);
            comprobarConexiones();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para comprobar el numero de conexiones y crearlas hasta alcanzar
     * el maximo
     *
     * @throws IOException Excepcion de entrada/salida
     */
    public static void comprobarConexiones() throws IOException {
        while (sockets.size() != maxConexiones) {
            iniciarSocketServidor(sockets.size());
        }
    }
}
