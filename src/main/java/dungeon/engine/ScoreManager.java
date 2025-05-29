package dungeon.engine;

import java.io.*;
import java.util.*;

/**
 * Manages Top 5 Scores for the MiniDungeon game.
 * Saves scores to a file and loads them at startup.
 */
public class ScoreManager {
    private static final String SCORE_FILE = "top_scores.dat";
    private static final int MAX_ENTRIES = 5;
    private static List<ScoreEntry> topScores = new ArrayList<>();

    // Load scores when class is loaded
    static {
        loadScores();
    }

    /**
     * Represents a single score entry with name and score.
     */
    public static class ScoreEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String name;
        public final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }
    }

    /**
     * Adds a new score, saves the list, and returns true if it's a Top 5 score.
     */
    public static boolean addScore(String name, int score) {
        topScores.add(new ScoreEntry(name, score));
        topScores.sort((a, b) -> Integer.compare(b.score, a.score)); // Descending
        if (topScores.size() > MAX_ENTRIES) {
            topScores = topScores.subList(0, MAX_ENTRIES);
        }
        saveScores();
        return topScores.stream().anyMatch(entry -> entry.name.equals(name) && entry.score == score);
    }

    /**
     * Returns the current Top 5 list.
     */
    public static List<ScoreEntry> getTopScores() {
        return new ArrayList<>(topScores);
    }

    /**
     * Loads scores from file.
     */
    private static void loadScores() {
        File file = new File(SCORE_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                topScores = (List<ScoreEntry>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("⚠ Could not load scores: " + e.getMessage());
        }
    }

    /**
     * Saves scores to file.
     */
    private static void saveScores() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SCORE_FILE))) {
            out.writeObject(topScores);
        } catch (IOException e) {
            System.err.println("⚠ Could not save scores: " + e.getMessage());
        }
    }
}
