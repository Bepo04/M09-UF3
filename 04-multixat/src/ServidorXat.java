import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {

    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    private Hashtable<String, GestorClients> clients = new Hashtable<>();
    private boolean sortir = false;
    private ServerSocket serverSocket;

    public void servidorAEscoltar() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);

            while (!sortir) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket.getInetAddress());
                GestorClients gc = new GestorClients(clientSocket, this);
                gc.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pararServidor();
        }
    }

    public void pararServidor() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(MSG_SORTIR);
        clients.clear();
        sortir = true;
    }

    public synchronized void afegirClient(GestorClients gc) {
        clients.put(gc.getNom(), gc);
        enviarMissatgeGrup("Entra: " + gc.getNom());
    }

    public synchronized void eliminarClient(String nom) {
        if (clients.containsKey(nom)) {
            clients.remove(nom);
        }
    }

    public synchronized void enviarMissatgeGrup(String missatge) {
        for (GestorClients gc : clients.values()) {
            gc.enviarMissatge("Servidor", missatge);
        }
    }

    public synchronized void enviarMissatgePersonal(String destinatari, String remitent, String missatge) {
        GestorClients dest = clients.get(destinatari);
        if (dest != null) {
            dest.enviarMissatge(remitent, missatge);
        }
    }

    public static void main(String[] args) {
        new ServidorXat().servidorAEscoltar();
    }
}