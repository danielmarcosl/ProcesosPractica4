package reader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class ReaderVentana extends JFrame {

    // Panel con scroll para el panel de texto
    private JScrollPane scroll;
    // Panel de texto
    static JTextArea textarea;
    // Botones
    static JButton bHisto; // Boton para ver el historial
    static JButton bUsers; // Boton para ver los usuarios actuales
    static JButton bConex; // Boton para cambiar el numero de conexiones
    static JButton bSalir; // Boton para salir del programa

    /**
     * Constructor de la ventana
     */
    public ReaderVentana() {
        // Detalles de la ventana
        setTitle("Servidor de Libros");
        setLayout(null);
        setBounds(0, 0, 525, 390);
        setResizable(false);

        // Inicializacion de botones
        bHisto = new JButton("Historial");
        bUsers = new JButton("Usuarios");
        bConex = new JButton("Conexiones");
        bSalir = new JButton("Salir");

        // Inicializacion del panel de texto
        textarea = new JTextArea();
        // Hacemos que el texto se ajuste horizontalmente sin salirse de la ventana
        textarea.setLineWrap(true);
        // Lo anadimos al JScrollPane
        scroll = new JScrollPane(textarea);

        // Fijamos las posiciones de los botones y su tamano
        bHisto.setBounds(60, 10, 100, 30);
        bUsers.setBounds(160, 10, 100, 30);
        bConex.setBounds(260, 10, 100, 30);
        bSalir.setBounds(360, 10, 100, 30);

        // Anadimos los botones a la ventana
        add(bHisto);
        add(bUsers);
        add(bConex);
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
