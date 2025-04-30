import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public void connecta() throws IOException {
        socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        System.out.printf("Client connectat a %s:%d%n", ServidorXat.HOST, ServidorXat.PORT);
        System.out.println("Flux d'entrada i sortida creat.");
    }

    public void enviarMissatge(String missatge) throws IOException {
        output.writeObject(missatge);
        output.flush();
    }

    public void tancarClient() throws IOException {
        if (socket != null && !socket.isClosed()) {
            input.close();
            output.close();
            socket.close();
            System.out.println("Tancant client...");
            System.out.println("Client tancat.");
        }
    }

    public static void main(String[] args) {
        try {
            ClientXat client = new ClientXat();
            client.connecta();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Rebut: Escriu el teu nom:");
            String nom = scanner.nextLine();
            client.enviarMissatge(nom);
            FilLectorCX fil = new FilLectorCX(client.input);
            fil.start();
            
            String missatge;
            do {
                missatge = scanner.nextLine();
                System.out.println("Missatge ('sortir' per tancar): Rebut: " + missatge);
                client.enviarMissatge(missatge);
            } while (!missatge.equalsIgnoreCase(ServidorXat.MSG_SORTIR));

            fil.join();
            scanner.close();
            client.tancarClient();
            System.out.println("El servidor ha tancat la connexió.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
