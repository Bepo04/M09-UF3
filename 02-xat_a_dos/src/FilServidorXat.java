import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    
    private ObjectInputStream input;

    public FilServidorXat(ObjectInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {
        try {
            String missatge;
            while (!(missatge = (String) input.readObject()).equalsIgnoreCase(ServidorXat.MSG_SORTIR)) {
                System.out.println("Missatge (\"sortir\" per tancar): Rebut: " + missatge);
            }
            System.out.println("Fil de xat finalitzat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
