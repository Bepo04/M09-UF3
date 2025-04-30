import java.io.ObjectInputStream;

public class FilLectorCX extends Thread {
    private ObjectInputStream input;

    public FilLectorCX(ObjectInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {
        System.out.println("Missatge ('sortir' per tancar): Fil de lectura iniciat");
        try {
            String missatge;
            while (!(missatge = (String) input.readObject()).equalsIgnoreCase(ServidorXat.MSG_SORTIR)) {
                System.out.println("Enviant missatge: " + missatge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
