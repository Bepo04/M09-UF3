import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Servidor {

    public static final int PORT = 8080;
    public static final String HOST = "localhost";
    private static final String LOCAL_STORAGE = "C:/Users/paulo/Documents/ITIC/M09/M09-UF3";

    private ServerSocket serverSocket;

    public Socket connectar() {
        Socket clientSocket = null;
        try {
            if (serverSocket == null) {
                serverSocket = new ServerSocket(PORT);
                System.out.printf("Acceptant connexions em -> %s:%d%n", HOST, PORT);
            }
            System.out.println("Esperant connexió...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexió acceptada: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.err.println("ERROR: NO S'HA POGUT INICIAR EL SERVIDOR");
            e.printStackTrace();
        }
        return clientSocket;
    }

    public void tancarConnexio(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor tancat.");
            }
        } catch (IOException e) {
            System.err.println("ERROR EN TANCAR EL SOCKET:\n" + e.getLocalizedMessage());
        }
    }

    public byte[] enviarFitxers(String name) {
        String fileName = LOCAL_STORAGE + name;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                System.err.println("El fitxer " + fileName + " no existeix.");
                return null;
            }

            Path path = file.toPath();
            byte[] contingut = Files.readAllBytes(path);
            return contingut;

        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connectar();
        servidor.enviarFitxers();
        servidor.tancarConnexio();
    }
}