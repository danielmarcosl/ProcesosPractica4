package clientreader;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Principal {

    /**
     * Metodo principal que llama al ClientReader y muestra la ventana creada
     *
     * @param args No usado
     */
    public static void main(String args[]) {
        ClientReaderVentana crv = new ClientReaderVentana(); // Invocamos al constructor de ClientReaderVentana

        new ClientReader(crv);
    }
}
