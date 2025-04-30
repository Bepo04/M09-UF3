import java.io.IOException;
import java.net.ServerSocket;

public class ServidorXat {

    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    private ServerSocket serverSocket;

    public void iniciarServidor() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.printf("Servidor iniciat a %s:%d", HOST, PORT);
            } catch (IOException e) {
                System.out.println("Error en iniciar el servidor");
                e.printStackTrace();
            }
        }
    }

    public void pararServidor() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNom(FilServidorXat fil) {
        return fil.getName();
    }
}