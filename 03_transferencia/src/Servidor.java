import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 8080;
    public static final String HOST = "localhost";

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Socket connectar() {
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

    public void enviarFitxers() {
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            
            while (true) {
                String nomFitxer = (String) input.readObject();
                Fitxer fitxer = new Fitxer(nomFitxer);
                if (fitxer.getName().equalsIgnoreCase("sortir")) {
                    System.out.println("El client surt.");
                    break;
                }
                System.out.println("Nom del fitxer a buscar: " + fitxer.getName());
                byte[] contingut = fitxer.getContingut();
                if (contingut == null) {
                    System.out.println("Error llegint el fitxer del client: null");
                    output.writeObject(null);
                    System.out.println("Nom del fitxer buit o nul. Sortint...");
                    return;
                } else {
                    System.out.println("Pes del fitxer del client: " + contingut.length + " bytes");
                    System.out.println("Fitxer enviat al client: " + fitxer.getPath() + fitxer.getName());
                    output.writeObject(contingut);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connectar();
        System.out.println("Esperant a que el client introdueixi el nom del fitxer...");
        servidor.enviarFitxers();
        servidor.tancarConnexio(servidor.clientSocket);
    }
}