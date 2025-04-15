import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 7777;
    public static final String HOST = "localhost";

    private ServerSocket srvSocket;
    private Socket clientSocket;

    public Socket connect() {
        try {
            if (srvSocket == null) {
                srvSocket = new ServerSocket(PORT);
                System.out.printf("Servidor en marxa a %s:%d%n", HOST, PORT);
            }
            System.out.println("Esperant connexions a " + HOST + ":" + PORT);
            clientSocket = srvSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientSocket;
    }

    public String repDades() {
        String missatge = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            while ((missatge = in.readLine()) != null) {
                System.out.println("Rebut: " + missatge);
                if (missatge.equalsIgnoreCase("Adeu")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return missatge;
    }

    public void tanca() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
    
            if (srvSocket != null && !srvSocket.isClosed()) {
                srvSocket.close();
                System.out.println("Servidor tancat.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connect();
        servidor.repDades();
        servidor.tanca();
    }
}