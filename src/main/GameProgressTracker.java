package main;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameProgressTracker {
    // Define tile sets for each location
    private final Set<Integer> panggungTiles = Set.of(4430, 4385, 4429, 4341, 2399, 4338, 4339);
    private final Set<Integer> alkidTiles = Set.of(4160, 4115, 4113, 4116, 4071, 4069, 4067, 4070, 2422, 2377);
    private final Set<Integer> kratonTiles = Set.of(2185, 2332, 2443, 2397, 2422, 2377);
    private final Set<Integer> tuguTiles = Set.of(3252);
    private final Set<Integer> merapiTiles = Set.of(2903, 2902);

    // Map to track progress for each location
    private final Map<String, Integer> progressMap = new HashMap<>();

    public GameProgressTracker() {
        // Initialize progress for each location
        progressMap.put("Panggung", 0);
        progressMap.put("Alkid", 0);
        progressMap.put("Kraton", 0);
        progressMap.put("Tugu", 0);
        progressMap.put("Merapi", 0);
    }

    // Method to update progress for a location
    public void updateProgress(int tile) {
        boolean updated = false;

        // Check each location to see if it contains the tile
        if (panggungTiles.contains(tile)) {
            updated = incrementProgress("Panggung", panggungTiles.size()) || updated;
        }

        if (alkidTiles.contains(tile)) {
            updated = incrementProgress("Alkid", alkidTiles.size()) || updated;
        }

        if (kratonTiles.contains(tile)) {
            updated = incrementProgress("Kraton", kratonTiles.size()) || updated;
        }

        if (tuguTiles.contains(tile)) {
            updated = incrementProgress("Tugu", tuguTiles.size()) || updated;
        }

        if (merapiTiles.contains(tile)) {
            updated = incrementProgress("Merapi", merapiTiles.size()) || updated;
        }

        // If the tile does not belong to any location, throw an exception
        if (!updated) {
            throw new IllegalArgumentException("Tile does not belong to any known location: " + tile);
        }
    }

    private boolean incrementProgress(String location, int totalTiles) {
        // Increment progress if not yet completed
        int currentProgress = progressMap.get(location);
        if (currentProgress < totalTiles) {
            progressMap.put(location, currentProgress + 1);
            System.out.println((currentProgress + 1) + "/" + totalTiles);
            return true;
        }
        return false;
    }

    // Method to get progress for a location
    public String getProgress(String location) {
        int progress = progressMap.getOrDefault(location, 0);
        int totalTiles = switch (location) {
            case "Panggung" -> panggungTiles.size();
            case "Alkid" -> alkidTiles.size();
            case "Kraton" -> kratonTiles.size();
            case "Tugu" -> tuguTiles.size();
            case "Merapi" -> merapiTiles.size();
            default -> throw new IllegalArgumentException("Unknown location: " + location);
        };
        return progress + "/" + totalTiles;
    }
}
