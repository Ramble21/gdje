package gd;

import gd.GDPorting.GDLevelPorter;
import gd.Objects.GameObject;
import gd.Objects.Hazards.DefaultSpike;
import gd.Objects.Hazards.HalfSpike;
import gd.Objects.Solids.DefaultBlock;
import gd.Objects.Solids.DefaultSlab;
import gd.Physics.Physics;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Level {
    protected final String name;
    protected final String difficulty;
    protected final String songName;
    public Color bgColor;
    protected final ArrayList<GameObject> objects = new ArrayList<>(); // hazards, solids, etc. (anything with a hitbox)
    protected final ArrayList<GameObject> deco = new ArrayList<>(); // anything without a hitbox
    protected final int spawnX;
    protected final int spawnY;
    protected final Physics spawnPhysics;

    public Level(int id) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("levels/" + id + ".txt"));
        String[] bg_arr = lines.get(3).split("\\s+");
        String[] player_arr = lines.get(4   ).split("\\s+");

        name = lines.getFirst().substring(lines.getFirst().indexOf(" ") + 1);
        difficulty = lines.get(1).substring(lines.get(1).indexOf(" ") + 1);
        songName = lines.get(2).substring(lines.get(2).indexOf(" ") + 1);
        bgColor = new Color(Integer.parseInt(bg_arr[1]), Integer.parseInt(bg_arr[2]), Integer.parseInt(bg_arr[3]));
        spawnX = Integer.parseInt(player_arr[1]);
        spawnY = Integer.parseInt(player_arr[2]);
        spawnPhysics = Physics.getPhysics(player_arr[3]);
        for (int i = 6; i < lines.size(); i++) {
            String[] arr = lines.get(i).split("\\s+");
            int jeid = Integer.parseInt(arr[1]);
            if (jeid == -1) {
                continue;
            }
            boolean h = arr[4].equals("h0");
            boolean v = arr[5].equals("v0");
            GameObject obj = getObject(jeid, Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), h, v, Integer.parseInt(arr[6].substring(1)));
            if (obj != null) {
                if (arr[0].equals("obj")) {
                    objects.add(obj);
                } else {
                    deco.add(obj);
                }
            }
        }
    }



    private GameObject getObject(int jeid, int x, int y, boolean flippedHoriz, boolean flippedVertic, int degreesRotated) {
        String name = GDLevelPorter.JEIDToName.get(jeid);
        return switch (name) {
            case "DefaultBlock" -> new DefaultBlock(x, y);
            case "DefaultSpike" -> new DefaultSpike(x, y);
            case "HalfSpike" -> new HalfSpike(x, y);
            case "DefaultSlab" -> new DefaultSlab(x, y);
            default -> null;
        };
    }

}
