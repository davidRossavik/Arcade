
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapLoader {
    public static String[] loadMap(String filepath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));
            return lines.toArray(new String[0]);
            
        } catch (IOException e) {
            System.err.println("Kunne ikke lese kartfilen: " + e.getMessage());
            return new String[0];
        }
    }
}
