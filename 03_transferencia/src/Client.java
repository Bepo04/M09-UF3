import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static final String DIR_ARRIBADA = "/tmp";

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Socket clientSocket;

    public void connectar() {
        clientSocket = new Socket(Servidor.HOST, Servidor.PORT);
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        input = new ObjectInputStream(clientSocket.getInputStream());
    }
}
