import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServidorXat servidor;
    private String nom;
    private boolean sortir = false;

    public GestorClients(Socket client, ServidorXat servidor) {
    this.client = client;
    this.servidor = servidor;
    try {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); 
        input = new ObjectInputStream(client.getInputStream());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public String getNom() {
        return nom;
    }

    public void run() {
        try {
            while (!sortir) {
                String missatge = (String) input.readObject();
                processaMissatge(missatge);
            }
        } catch (Exception e) {
            System.out.println("Error rebent missatge. Tancant client...");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMissatge(String missatge) {
        try {
            output.writeObject(missatge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processaMissatge(String missatge) {
        String codi = Missatge.getCodiMissatge(missatge);
        String[] parts = Missatge.getPartsMissatge(missatge);
        if (codi == null || parts == null) return;

        switch (codi) {
            case Missatge.CODI_CONECTAR:
                nom = parts[1];
                servidor.afegirClient(this);
                break;
            case Missatge.CODI_SORTIR_CLIENT:
                sortir = true;
                servidor.eliminarClient(nom);
                break;
            case Missatge.CODI_SORTIR_TOTS:
                sortir = true;
                servidor.finalitzarXat();
                break;
            case Missatge.CODI_MSG_PERSONAL:
                servidor.enviarMissatgePersonal(parts[1], nom, parts[2]);
                break;
            default:
                System.out.println("Codi de missatge desconegut: " + codi);
        }
    }
}
