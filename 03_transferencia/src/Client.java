import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Socket clientSocket;

    public void connectar() {
        try {
            clientSocket = new Socket(Servidor.HOST, Servidor.PORT);
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Connectant a " + Servidor.HOST + ":" + Servidor.PORT);
            System.out.println("Connexió acceptada: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rebreFitxer(String fileName, String ruta) {
        try {
            output.writeObject(fileName);

            byte[] contingutFitxer = (byte[]) input.readObject();
            if (contingutFitxer == null) {
                return;
            }
            Path path = Paths.get(ruta);
            Files.write(path, contingutFitxer);
            System.out.println("Nom del fitxer: " + fileName);
            System.out.println("Fitxer rebut i guardat a: " + path.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void tancarConnexio() throws IOException {
        clientSocket.close();
        System.out.println("Connexió tancada.");
    }

    public static void main(String[] args) {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);
        try {
            client.connectar();
            String nom = "";
            while (!nom.equalsIgnoreCase("sortir")) {
                nom = scanner.nextLine().trim();
                if (!nom.equalsIgnoreCase("sortir")) {
                    System.out.print("Ruta local on guardar el fitxer: ");
                    String ruta = scanner.nextLine().trim();
                    client.rebreFitxer(nom, ruta);
                }
            }
            client.tancarConnexio();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
