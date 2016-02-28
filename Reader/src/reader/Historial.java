package reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Historial {

    public Historial() {

    }

    /**
     * Metodo para anadir un string al historial
     *
     * @param linea String con informacion para el historial
     * @throws IOException Excepcion de entrada/salida
     */
    public synchronized void anadirHistorial(String linea) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("historial.txt", true));
        bw.write(linea);
        bw.newLine();
        bw.close();
    }

    /**
     * Metodo para devolver el historial
     * @return 
     */
    public String mostrarHistorial() {

        BufferedReader br = null; // Buffer de lectura
        String linea = null; // Variable donde se almacenara cada linea del fichero
        String historial = ""; // Variable donde se almaceran todas las lineas
        
        try {
            // Abrimos el fichero para lectura
            br = new BufferedReader(new FileReader("historial.txt"));
            // Lo recorremos
            while ((linea = br.readLine()) != null) {
                // Almacenamos en la variable las lineas
                historial += linea + "\n";
            }
            // Lo cerramos
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Devolvemos el historial
        return historial;
    }
}
