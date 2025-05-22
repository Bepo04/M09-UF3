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
                gc.start(); // Cada client es gestiona amb un nou thread
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
        System.out.println("Tancant tots els clients.");
    }

    public void afegirClient(GestorClients gc) {
        clients.put(gc.getNom(), gc);
        enviarMissatgeGrup("Entra: " + gc.getNom());

        String debugMsg = "DEBUG: multicast Entra: " + gc.getNom();
        for (GestorClients gestor : clients.values()) {
            gestor.enviarMissatge(Missatge.getMissatgeGrup(debugMsg));
        }
    }

    public void eliminarClient(String nom) {
        if (clients.containsKey(nom)) {
            clients.remove(nom);
            System.out.println("Surt: " + nom);
        }
    }

    public void enviarMissatgeGrup(String missatge) {
        for (GestorClients gc : clients.values()) {
            gc.enviarMissatge(missatge);
        }
    }

    public void enviarMissatgePersonal(String destinatari, String remitent, String missatge) {
        System.out.println("Missatge personal per (" + destinatari + ") de (" + remitent + "): " + missatge);   
        GestorClients dest = clients.get(destinatari);
        if (dest != null) {
            String missatgeEnviar = Missatge.CODI_MSG_PERSONAL + "#" + remitent + "#" + missatge;
            dest.enviarMissatge(missatgeEnviar);
        }
    }

    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        servidor.servidorAEscoltar();
    }
}
