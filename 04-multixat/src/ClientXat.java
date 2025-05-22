import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat extends Thread {
    
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean sortir = false;

    public void connecta() {
    try {
        socket = new Socket("localhost", 9999);
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush(); 
        input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client connectat a localhost:9999");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public void enviarMissatge(String msg) throws IOException {
        output.writeObject(msg);
    }

    public void tancarClient() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLinea(Scanner sc, String prompt, boolean obligatori) {
        System.out.print(prompt + " ");
        String line = sc.nextLine().trim();

        while (obligatori && line.isEmpty()) {
            System.out.print(prompt + " ");
            line = sc.nextLine().trim();
        }

        return line;
    }

    public void ajuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println(" 1.- Conectar");
        System.out.println(" 2.- Enviar missatge personal");
        System.out.println(" 3.- Enviar missatge grup");
        System.out.println(" 4.- Sortir");
        System.out.println(" 5.- Finalitzar tothom");
        System.out.println("---------------------");
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            while (!sortir) {
                String msgRaw = (String) input.readObject();
                String codi   = Missatge.getCodiMissatge(msgRaw);
                String[] parts    = Missatge.getPartsMissatge(msgRaw);
                switch (codi) {
                    case Missatge.CODI_MSG_PERSONAL:
                        System.out.println("Missatge de (" + parts[1] + "): " + parts[2]);
                        break;
                    case Missatge.CODI_MSG_GRUP:
                        System.out.println(parts[1]);
                        break;
                    case Missatge.CODI_SORTIR_TOTS:
                        sortir = true;
                        break;
                    default:
                        System.out.println("Error: codi desconegut " + codi);
                }
            }
        } catch (Exception e) {
        } finally {
            try { tancarClient(); } catch (Exception e) {}
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientXat client = new ClientXat();
        try {
            client.connecta();
            client.start();
            client.ajuda();
            boolean acaba = false;
            while (!acaba) {
                String op = sc.nextLine().trim();
                switch (op) {
                    case "1":
                        String nom = client.getLinea(sc, "Introdueix el nom:", true);
                        client.enviarMissatge(Missatge.getMissatgeConectar(nom));
                        break;
                    case "2":
                        String dest = client.getLinea(sc, "Destinatari:", true);
                        String msg  = client.getLinea(sc, "Missatge a enviar:", true);
                        client.enviarMissatge(Missatge.getMissatgePersonal(dest, msg));
                        break;
                    case "3":
                        String mg   = client.getLinea(sc, "Missatge grup:", true);
                        client.enviarMissatge(Missatge.getMissatgeGrup(mg));
                        break;
                    case "4":
                        client.enviarMissatge(Missatge.getMissatgeSortirClient("Adeu"));
                        acaba = true;
                        break;
                    case "5":
                        client.enviarMissatge(Missatge.getMissatgeSortirTots("Adeu"));
                        acaba = true;
                        break;
                    default:
                        System.out.println("Opció no vàlida. Utilitza numeros per a escollir una opció.");
                }
            }
            client.tancarClient();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
