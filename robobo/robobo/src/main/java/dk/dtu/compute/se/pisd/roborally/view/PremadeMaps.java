package dk.dtu.compute.se.pisd.roborally.view;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;

public class PremadeMaps {
    private static final ArrayList<PremadeMaps> maps = new ArrayList<>();

    private final int[][] mapArray;
    private final String mapName;

    static {
        maps.add(new PremadeMaps(new int[][]{
                // ... Your map data ...
        }, "Test Map"));
        maps.add(new PremadeMaps(new int[][]{
                // ... Your map data ...
        }, "Corridor Blitz"));
        maps.add(new PremadeMaps(new int[][]{
                // ... Your map data ...
        }, "Sprint Cramp"));
    }

    public PremadeMaps(int[][] mapArray, String mapName) {
        this.mapArray = mapArray;
        this.mapName = mapName;
    }

    public static PremadeMaps get(String mapName) {
        for (PremadeMaps map : maps) {
            if (map.mapName.equals(mapName)) {
                return map;
            }
        }
        return null;  // Return null if no map matches the given name
    }

    public int[][] getMapArray() {
        return mapArray;
    }

    public String getMapName() {
        return mapName;
    }

    public static PremadeMaps loadMapFromJson(String filePath) {
        try {
            FileReader reader = new FileReader(filePath);
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();

            String mapName = jsonObject.get("mapName").getAsString();
            JsonArray mapData = jsonObject.getAsJsonArray("mapArray");

            int rows = mapData.size();
            int cols = mapData.get(0).getAsJsonArray().size();

            int[][] mapArray = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    mapArray[i][j] = mapData.get(i).getAsJsonArray().get(j).getAsInt();
                }
            }

            return new PremadeMaps(mapArray, mapName);
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
