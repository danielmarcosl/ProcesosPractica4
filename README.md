### Spanish
Queremos desarrollar en Java un programa (servidor) que suministrará las líneas de texto de un libro a otro programa (cliente) que desarrollaremos que las recibirá y las mostrará. El servidor almacenará la posición de lectura en un fichero con el nombre del usuario.

El programa servidor consta de 5 clases:
-	Principal, lanza el programa.
-	Reader, contiene las variables globales y lanza los hilos.
-	ReaderVentana, contiene la interfaz gráfica.
-	HiloServidor, contiene los hilos y su funcionamiento.
-	Historial, contiene métodos sincronizados para acceder al fichero del historial.

El programa cliente consta de 3 clases:
-	Principal, lanza el programa.
-	ClientReader, contiene toda la lógica del programa.
-	ClientReaderVentana, contiene la interfaz gráfica.

### English
We want to develop in Java a program (server) that send the lines of text of a book to another program (client) that we'll develop that received and showed them. The server store the read position in a file with the user name.

The server program has:
- Principal, launch the program.
- Reader, contain the global variables and launch the threads.
- ReaderVentana, containt the graphic interface.
- HiloServidor, contain the threads and its functionality
- Historial, contain the synchronized methods to access to the history file.

The client program has:
- Principal, launch the program.
- ClientReader, containt the program logic.
- ClientReaderVentana, containt the graphic interface.
