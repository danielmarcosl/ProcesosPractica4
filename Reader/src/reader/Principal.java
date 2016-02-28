package reader;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Principal {

    /**
     * Metodo principal que llama al Reader y muestra la ventana creada
     *
     * @param args No usado
     */
    public static void main(String args[]) {
        ReaderVentana rv = new ReaderVentana();

        new Reader(rv);
    }

}
