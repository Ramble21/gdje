package gd;

import gd.Objects.GameObject;
import gd.Objects.Hazards.DefaultSpike;
import gd.Objects.Solids.DefaultBlock;
import gd.Physics.Physics;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Level {
    protected final String name;
    protected final String difficulty;
    public Color bgColor;
    protected final ArrayList<GameObject> objects = new ArrayList<>(); // hazards, solids, etc. (anything with a hitbox)
    protected final ArrayList<GameObject> deco = new ArrayList<>(); // anything without a hitbox
    protected final int spawnX;
    protected final int spawnY;
    protected final Physics spawnPhysics;

    public Level(int id) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("levels/" + id + ".txt"));
        String[] bg_arr = lines.get(2).split("\\s+");
        String[] player_arr = lines.get(3).split("\\s+");

        name = lines.getFirst().substring(lines.getFirst().indexOf(" ") + 1);
        difficulty = lines.get(1).substring(lines.get(1).indexOf(" ") + 1);
        bgColor = new Color(Integer.parseInt(bg_arr[1]), Integer.parseInt(bg_arr[2]), Integer.parseInt(bg_arr[3]));
        spawnX = Integer.parseInt(player_arr[1]);
        spawnY = Integer.parseInt(player_arr[2]);
        spawnPhysics = Physics.getPhysics(player_arr[3]);
        for (int i = 5; i < lines.size(); i++) {
            String[] arr = lines.get(i).split("\\s+");
            if (arr[0].equals("obj")) {
                objects.add(getObject(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3])));
            }
            else if (!arr[0].startsWith("/")) {
                deco.add(getObject(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3])));
            }
        }
    }

    private GameObject getObject(String type, int x, int y) {
        return switch (type) {
            case "DefaultBlock" -> new DefaultBlock(x, y);
            case "DefaultSpike" -> new DefaultSpike(x, y);
            default -> throw new RuntimeException("Unexpected object type: " + type);
        };
    }

}
