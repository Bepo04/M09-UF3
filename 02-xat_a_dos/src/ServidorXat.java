import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorXat {

    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    private ServerSocket serverSocket;

    public void iniciarServidor() {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.printf("Servidor iniciat a %s:%d%n", HOST, PORT);
            } catch (IOException e) {
                System.out.println("Error en iniciar el servidor");
                e.printStackTrace();
            }
        }
    }

    public void pararServidor() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            System.out.println("Servidor aturat.");
        }
    }


    public String getNom(ObjectInputStream input) throws IOException, ClassNotFoundException {
        return (String) input.readObject();
    }

    public static void main(String[] args) {
        try {
            ServidorXat servidor = new ServidorXat();
            servidor.iniciarServidor();

            Socket clientSocket = servidor.serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            String nomClient = servidor.getNom(in);
            System.out.println("Nom del client: " + nomClient);

            FilServidorXat fil = new FilServidorXat(in);
            fil.start();

            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
            String missatge;
            do {
                missatge = consola.readLine();
                out.writeObject(missatge);
                out.flush();
            } while (!missatge.equalsIgnoreCase(MSG_SORTIR));

            fil.join();
            in.close();
            out.close();
            clientSocket.close();
            servidor.pararServidor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}