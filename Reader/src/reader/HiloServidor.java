package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class HiloServidor extends Thread {

    Historial historial; // Objeto Historial
    DataInputStream fentrada; // Flujo de entrada de datos
    DataOutputStream fsalida; // Flujo de salida de datos
    Socket socket; // Socket de escucha para el cliente
    int numHilo; // Numero de hilo
    public String nombre; // Nombre de usuario
    String libro = ""; // Titulo del libro
    int numeroLinea; // Numero de linea actual
    int numeroLineaPrev; // Numero de linea para retroceder
    boolean primeraLineaB = true; // Boolean para saber la primera linea que lee el cliente
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // Formato de fecha

    /**
     * Constructor del hilo servidor
     *
     * @param s Socket
     * @exception ex Excepcion de entrada/salida
     */
    public HiloServidor(Socket s, int n, Historial h) {
        this.socket = s;
        this.numHilo = n;
        this.historial = h;

        try {
            fentrada = new DataInputStream(socket.getInputStream());
            fsalida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo ejecutado al lanzar el hilo
     */
    public void run() {
        try {
            historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Conexion entrante con ip: " + socket.getInetAddress());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Recogemos el nombre del usuario
        getNombre();

        // Manejamos las peticiones del usuario
        manejarPeticiones();
    }

    private void getNombre() {
        try {
            nombre = fentrada.readUTF();

            Reader.nombres.add(nombre);

            historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Usuario con ip: " + socket.getInetAddress() + " se ha identificado como " + nombre);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para manejar las peticiones realizadas mediante los botones de la
     * interfaz del cliente
     */
    private void manejarPeticiones() {

        // Variable donde se guardara la peticion del usuario
        String peticion = "";

        // Bucle que ejecutara las peticiones hasta que el cliente cierre
        while (!peticion.equals("*")) {
            try {
                peticion = fentrada.readUTF();

                if (peticion.equals("libros")) {
                    // Enviamos el listado de libros
                    enviarListado();

                    // Recibimos el nombre del libro seleccionado
                    libro = fentrada.readUTF();

                    enviarLinea(true);

                    historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Usuario " + nombre + "(ip: " + socket.getInetAddress() + ") solicita el libro " + libro);
                } else if (peticion.equals("prev") && !libro.equals("")) {
                    enviarLinea(false);
                    historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Usuario " + nombre + "(ip: " + socket.getInetAddress() + ") solicita linea " + numeroLineaPrev + " del libro " + libro);
                } else if (peticion.equals("next") && !libro.equals("")) {
                    enviarLinea(true);
                    historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Usuario " + nombre + "(ip: " + socket.getInetAddress() + ") solicita linea " + numeroLinea + " del libro " + libro);
                } else if (peticion.equals("*")) {
                    historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Usuario " + nombre + "(ip: " + socket.getInetAddress() + ") se ha desconectado");
                    // Borramos los datos de los arrays
                    Reader.nombres.remove(nombre);
                    Reader.sockets.remove(socket);
                    Reader.hilos.remove(this);
                    Reader.comprobarConexiones();
                } else {
                    fsalida.writeUTF("Error, selecciona un libro.");
                    historial.anadirHistorial(dateFormat.format(Calendar.getInstance().getTime()) + " Usuario " + nombre + "(ip: " + socket.getInetAddress() + ") ha enviado una solicitud erronea");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                peticion = "*";
            }
        }
    }

    /**
     * Metodo para enviar el listado de libros al cliente
     *
     * @throws FileNotFoundException Excepcion de no encontrar fichero
     * @throws IOException Excepcion de entrada/salida
     */
    private void enviarListado() throws FileNotFoundException, IOException {
        // Abrimos el fichero para lectura
        BufferedReader br = new BufferedReader(new FileReader("listado.txt"));

        String linea = null; // Variable que almacenara cada linea del fichero
        String listado = ""; // Variable que almacenara todo el fichero

        // Reocrremos el fichero y lo anadimos a la variable
        while ((linea = br.readLine()) != null) {
            listado += linea + "\n";
        }

        // Borramos el ultimo \n
        listado = listado.substring(0, listado.length() - 1);

        // Mandamos el fichero al cliente
        fsalida.writeUTF(listado);

        // Cerramos el fichero
        br.close();

        // Reiniciamos variables
        libro = "";
        numeroLinea = 0;
        numeroLineaPrev = 0;
        primeraLineaB = true;
    }

    /**
     * Metodo para enviar las lineas del libro deseado al cliente
     *
     * @param posicion True = linea siguiente, False = linea anterior
     * @throws FileNotFoundException Excepcion de no encontrar fichero
     * @throws IOException Excepcion de entrada/salida
     */
    private void enviarLinea(boolean posicion) throws FileNotFoundException, IOException {
        // Abrimos el fichero para lectura
        BufferedReader br = new BufferedReader(new FileReader("libros\\" + libro + ".txt"));
        // Recogemos la ultima linea leida por el usuario
        numeroLinea = comprobarLinea();
        // Linea del documento
        int lineaActual = 0;
        // Variable que almacenara cada linea del fichero
        String linea = null;

        // Si es la primera linea, almacenarla en la variable para retroceder
        if (primeraLineaB) {
            numeroLineaPrev = numeroLinea;
            primeraLineaB = false;
        }

        // Si la posicion es false, enviamos la linea anterior
        if (!posicion) {
            numeroLineaPrev -= 1;
        }

        while ((linea = br.readLine()) != null) {
            // Segun la posicion enviamos una linea u otra
            if (posicion) {
                if (lineaActual == numeroLinea) {
                    fsalida.writeUTF(linea);
                    // Aumentamos la linea
                    numeroLinea += 1;
                    // Guardamos la linea en el fichero del usuario
                    guardarLinea(numeroLinea);
                    // Salimos el while
                    break;
                }
            } else // Comentario de carga
            {
                if (numeroLineaPrev >= 0) { // Si es positiva, enviamos la linea
                    if (lineaActual == numeroLineaPrev) {
                        fsalida.writeUTF(linea);
                        // Salimos del while
                        break;
                    }
                } else { // Si es negativa, enviamos un string vacio
                    fsalida.writeUTF(" ");
                    // Salimos del while
                    break;
                }
            }
            // Aumentamos contador
            lineaActual++;
        }
        // Cerramos el fichero

        br.close();
    }

    /**
     * Metodo para recoger la ultima linea leida por el usuario
     *
     * @return Linea
     */
    private int comprobarLinea() throws FileNotFoundException, IOException {

        // Variable que contiene el numero de linea
        int num = 0;

        // Comprobamos si existe el fichero del usuario
        // Si existe y contiene el libro, devolvemos la linea
        // Sino devolvemos 0
        if (new File("users\\" + nombre + ".txt").exists()) {
            // Abrimos el fichero para lectura
            BufferedReader br = new BufferedReader(new FileReader("users\\" + nombre + ".txt"));

            // Variable que almacenara cada linea del fichero
            String linea = null;

            while ((linea = br.readLine()) != null) {
                // Declaracion de StringTokenizer para la linea almacenada con el token #
                StringTokenizer tok = new StringTokenizer(linea, "#");
                // Mientras haya tokens, almacenarlos en sus variables
                while (tok.hasMoreTokens()) {
                    String titulo = tok.nextToken();
                    int n = Integer.parseInt(tok.nextToken());

                    // Comprobamos que el libro de la linea coincide con el pedido por el usuario
                    if (titulo.equals(libro)) {
                        num = n;
                        break;
                    } // end if linea
                } // end while token
            } // end while br
            // Cerramos el fichero
            br.close();
        } // end if exists

        // Devolvemos el numero de linea
        return num;
    }

    private void guardarLinea(int n) throws IOException {
        // Declaramos el buffer de escritura que utilizaremos despues
        BufferedWriter bw = null;

        // Comprobaoms si existe el fichero para escribir los tokens correctamente
        if (new File("users\\" + nombre + ".txt").exists()) {

            if (n == 1) {// Si existe y la linea es 1 significa que no esta guardado, se anade al final
                bw = new BufferedWriter(new FileWriter("users\\" + nombre + ".txt", true));
                bw.write("#" + libro + "#" + n);
                bw.newLine();
            } else {// Si esta guardado, reemplazar la linea
                // Abrimos el fichero para lectura
                BufferedReader br = new BufferedReader(new FileReader("users\\" + nombre + ".txt"));

                String linea; // Variable donde se guardara el fichero linea a linea
                String contenido = ""; // Variable donde se guardara todo el contenido
                int numLinea = 0; // Variable que contendra el numero de linea donde esta el libro
                int nl = 0; // Variable para contar las lineas

                // Almacenamos todo el fichero en la variable contenido
                while ((linea = br.readLine()) != null) {

                    // Anadimos la linea a la variable que contendra todo
                    contenido += linea + "\n";

                    // Comprobamos la linea en la que esta el libro
                    if (linea.contains(libro)) {
                        numLinea = nl;
                    }

                    // Aumentamos el contador
                    nl += 1;
                }

                // Cerramos el fichero
                br.close();

                // Almacenamos las lineas en un array
                String[] contenidoLineas = contenido.split("\n");

                // Dependiendo de la linea en la que este, ponemos mas o menos tokens
                if (numLinea == 0) {
                    contenidoLineas[numLinea] = libro + "#" + n;
                } else {
                    String nuevaLinea = "#" + libro + "#" + n;
                    contenidoLineas[numLinea] = nuevaLinea;
                }

                // Abrimos el fichero para escritura
                bw = new BufferedWriter(new FileWriter("users\\" + nombre + ".txt"));

                // Escribimos las lineas del array en el fichero
                for (int i = 0; i < contenidoLineas.length; i++) {
                    bw.write(contenidoLineas[i]);
                    bw.newLine();
                }
            }
        } else {
            // Abrimos el fichero para escritura
            bw = new BufferedWriter(new FileWriter("users\\" + nombre + ".txt"));
            // Anadimos el string al fichero
            bw.write(libro + "#" + n);
        }

        // Cerramos el fichero
        bw.close();
    }
}
