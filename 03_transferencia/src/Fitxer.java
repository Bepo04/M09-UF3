import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Fitxer {
    
    private String name;
    private byte[] contingut;
    
    private final String LOCAL_PATH = "/tmp/missatges/";

    public Fitxer(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public String getPath() { return LOCAL_PATH; }

    public byte[] getContingut() {
        File file = new File(LOCAL_PATH + name);
        if (!file.exists()) {
            return null;
        }
        Path path = file.toPath();
        try {
            contingut = Files.readAllBytes(path);
            return contingut;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
