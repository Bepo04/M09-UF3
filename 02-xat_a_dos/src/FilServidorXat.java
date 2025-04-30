import java.io.IOException;
import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    
    private ObjectInputStream input;

    public FilServidorXat(String name, ObjectInputStream input) {
        super(name);
        this.input = input;
    }

    @Override
    public void run() {
        String msg;
        try {
            while ((msg = (String) input.readObject()) != null) {
                if (msg.equals(ServidorXat.MSG_SORTIR)) {
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
