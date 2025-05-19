import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean sortir = false;

    public void connecta() {
        try {
            socket = new Socket("localhost", 9999);
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Client connectat a localhost:9999");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMissatge(String msg) {
        try {
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tancarClient() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execucio() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> {
                try {
                    while (!sortir) {
                        String missatge = (String) ois.readObject();
                        String codi = Missatge.getCodiMissatge(missatge);
                        String[] parts = Missatge.getPartsMissatge(missatge);

                        switch (codi) {
                            case Missatge.CODI_SORTIR_TOTS:
                                sortir = true;
                                break;
                            case Missatge.CODI_MSG_PERSONAL:
                                System.out.println("Missatge de (" + parts[1] + "): " + parts[2]);
                                break;
                            case Missatge.CODI_MSG_GRUP:
                                System.out.println(parts[1]);
                                break;
                            default:
                                System.out.println("Error: Codi desconegut");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error rebent missatge. Sortint...");
                } finally {
                    tancarClient();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void mainLoop() {
        connecta();
        execucio();
        ajuda();
        Scanner sc = new Scanner(System.in);
        
        while (!sortir) {
            System.out.print("> ");
            String input = sc.nextLine();

            if (input.isEmpty()) {
                sortir = true;
                enviarMissatge(Missatge.getMissatgeSortirClient("Adeu"));
                break;
            }

            switch (input) {
                case "1":
                    System.out.print("Introdueix el nom: ");
                    String nom = sc.nextLine();
                    enviarMissatge(Missatge.getMissatgeConectar(nom));
                    break;
                case "2":
                    System.out.print("Destinatari: ");
                    String dest = sc.nextLine();
                    System.out.print("Missatge: ");
                    String msg = sc.nextLine();
                    enviarMissatge(Missatge.getMissatgePersonal(dest, msg));
                    break;
                case "3":
                    System.out.print("Missatge grup: ");
                    String grupMsg = sc.nextLine();
                    enviarMissatge(Missatge.getMissatgeGrup(grupMsg));
                    break;
                case "4":
                    sortir = true;
                    enviarMissatge(Missatge.getMissatgeSortirClient("Adeu"));
                    break;
                case "5":
                    sortir = true;
                    enviarMissatge(Missatge.getMissatgeSortirTots("Adeu"));
                    break;
                default:
                    ajuda();
            }
        }

        sc.close();
        tancarClient();
    }

    public static void main(String[] args) {
        new ClientXat().mainLoop();
    }
}