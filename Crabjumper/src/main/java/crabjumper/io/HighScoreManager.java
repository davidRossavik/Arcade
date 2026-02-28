package crabjumper.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HighScoreManager {
    
    private static String filePath = System.getProperty("user.home") + File.separator + ".crabjumper"
            + File.separator + "Highscores.txt";

    private static File getScoreFile() {
        return new File(filePath);
    }

    private static void ensureStorageExists() throws IOException {
        File file = getScoreFile();
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Kunne ikke opprette mappe: " + parent.getAbsolutePath());
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Kunne ikke opprette fil: " + file.getAbsolutePath());
        }
    }

    // Lagre ny highscore - Skrive til fil //
    public static boolean saveScore(String name, int score) {
        String safeName = (name == null || name.isBlank()) ? "Spiller" : name.trim();
        try {
            ensureStorageExists();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(getScoreFile(), true))) {
            writer.println(safeName + " : " + score);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getTop5() {
        List<String> result = new ArrayList<>();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(); //bruker entries for å tillate duplikater

        File file = getScoreFile();
        if (!file.exists()) {
            result.add("Ingen lagrede scores");
            return result;
        }

        try (Scanner scanner = new Scanner(file)) {
            // Legger alle linjene inn i en liste
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                
                if (line.contains(":")) { //hopper over linjer som ikke har :
                    String[] parts = line.split(":");

                    if (parts.length == 2) { // hopper over linjer som ikke har to elementer
                        String name = parts[0].trim();
                        String scorePart = parts[1].trim();

                        try {
                            int score = Integer.parseInt(scorePart); // hopper over linjer hvor score er uleselig
                            entries.add(new AbstractMap.SimpleEntry<>(name, score));
                        } catch (NumberFormatException e) {
                            System.err.println("Ugyldig score-format: " + scorePart);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.add("Kunne ikke lese til fil");
            return result;
        }

        // Sorterer etter score i synkende rekkefølge
        entries.sort((a, b) -> b.getValue() - a.getValue()); //positiv verdi = b først og motsatt

        // Legger til de 5 beste inn i resultatlista
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            result.add(entry.getKey() + " : " + entry.getValue());
        }

        return result;
    }


    // SETTERS //

    public static void setFilePath(String path) {
        filePath = path;
    }
}
