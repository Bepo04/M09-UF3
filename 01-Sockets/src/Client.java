import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static final int PORT = Servidor.PORT;
    public static final String HOST = Servidor.HOST;
    
    private Socket socket;
    private PrintWriter out;

    public void conecta() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connectat a servidor en: " + HOST + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void envia(String missatge) {
        if (out != null) {
            out.println(missatge);
            System.out.println("Missatge enviat al servidor: " + missatge);
        } else {
            System.err.println("Error: el PrintWriter no està inicialitzat.");
        }
    }

    public void tanca() {
        try {
            if (out != null) {
                out.close();
                System.out.println("Client tancat.");
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error tancant el client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.conecta();

        client.envia("Prova d'enviament 1");
        client.envia("Prova d'enviament 2");
        client.envia("Adeu");

        try {
            System.out.println("Prem ENTER per tancar el client...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            reader.readLine();
        } catch (IOException e) {
            System.err.println("Error al esperar ENTER: " + e.getMessage());
        }
        client.tanca();
    }
}