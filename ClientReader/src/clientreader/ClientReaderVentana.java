package clientreader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class ClientReaderVentana extends JFrame {

    // Panel con scroll para el panel de texto
    private JScrollPane scroll;
    // Panel de texto
    static JTextArea textarea;
    // Botones
    static JButton bLibro; // Boton para ver los libros
    static JButton bPrevL; // Boton para ver las anteriores lineas
    static JButton bNextL; // Boton para ver las siguientes lineas
    static JButton bSalir; // Boton para salir del programa

    public ClientReaderVentana() {
        // Detalles de la ventana
        setTitle("Cliente de Libros");
        setLayout(null);
        setBounds(0, 0, 525, 390);
        setResizable(false);

        // Inicializacion de botones
        bLibro = new JButton("Libros");
        bPrevL = new JButton("Prev Line");
        bNextL = new JButton("Next Line");
        bSalir = new JButton("Salir");

        // Inicializacion del panel de texto
        textarea = new JTextArea();
        // Hacemos que el texto se ajuste horizontalmente sin salirse de la ventana
        textarea.setLineWrap(true);
        // Lo anadimos al JScrollPane
        scroll = new JScrollPane(textarea);

        // Fijamos las posiciones de los botones y su tamano
        bLibro.setBounds(60, 10, 100, 30);
        bPrevL.setBounds(160, 10, 100, 30);
        bNextL.setBounds(260, 10, 100, 30);
        bSalir.setBounds(360, 10, 100, 30);

        // Anadimos los botones a la ventana
        add(bLibro);
        add(bPrevL);
        add(bNextL);
        add(bSalir);

        // Fijamos la posicion del JScrollPane
        scroll.setBounds(10, 50, 500, 300);

        // Desactivamos el scroll horizontal
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Anadimos el JScrollPanel a la ventana
        add(scroll);

        // Hacemos que el panel de texto no sea editable
        textarea.setEditable(false);
    }
}
