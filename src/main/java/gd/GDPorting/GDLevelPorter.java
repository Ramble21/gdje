package gd.GDPorting;

import java.util.*;
import java.util.stream.Collectors;

public class GDLevelPorter {
    private static final HashMap<Integer, String> GDIDtoName = new HashMap<>(Map.ofEntries(
            Map.entry(1, "DefaultBlock"),
            Map.entry(2, "Block1Edge"),
            Map.entry(3, "Block1Angle"),
            Map.entry(5, "Block1Empty"),
            Map.entry(6, "Block1Full"),
            Map.entry(7, "Block1Sides"),
            Map.entry(8, "DefaultSpike"),
            Map.entry(9, "GroundHazard"),
            Map.entry(10, "PortalGravityP"),
            Map.entry(11, "PortalGravityN"),
            Map.entry(12, "PortalCube"),
            Map.entry(13, "PortalShip")
    ));
    private static final HashMap<String, Integer> nameToJEID = new HashMap<>(Map.ofEntries(
            Map.entry("DefaultBlock", 1),
            Map.entry("DefaultSpike", 2),
            Map.entry("GroundHazard", 3),
            Map.entry("Block1Full", 4),
            Map.entry("Block1Edge", 5),
            Map.entry("Block1Angle", 6),
            Map.entry("Block1Sides", 7),
            Map.entry("PortalGravityP", 10),
            Map.entry("PortalGravityN", 11),
            Map.entry("PortalCube", 12),
            Map.entry("PortalShip", 13),
            Map.entry("Block1Empty", 1001)
    ));
    public static final Map<Integer, String> JEIDToName =
            nameToJEID.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(
                            Map.Entry::getValue,
                            Map.Entry::getKey
                    ));

    public static void createTxt(String CCLLPath, String levelName) throws Exception {
        String decoded = GDLevelExtractor.extractLevel(CCLLPath, levelName);
        List<GDObject> objsGD = parseObjects(decoded);
        for (GDObject obj : objsGD) {
            System.out.println(obj.getGDJEString());
        }
    }

    public static List<GDObject> parseObjects(String innerLevelString) {
        List<GDObject> objects = new ArrayList<>();
        for (String raw : extractObjectStrings(innerLevelString)) {
            objects.add(new GDObject(parseObjectProperties(raw)));
        }
        return objects;
    }

    private static int convertX(int x) {
        return (int) (600 + (x * (5.0 / 3)));
    }

    private static int convertY(int y) {
        return (int) (875 - (y * (5.0 / 3)));
    }

    public record GDObject(Map<Integer, String> props) {

        public int getId() {
            return Integer.parseInt(props.get(1));
        }

        public double getX() {
            return Double.parseDouble(props.get(2));
        }

        public double getY() {
            return Double.parseDouble(props.get(3));
        }

        public boolean isFlippedHorizontally() {
            return "1".equals(props.getOrDefault(4, "0"));
        }

        public boolean isFlippedVertically() {
            return "1".equals(props.getOrDefault(5, "0"));
        }

        public double getRotation() {
            return Double.parseDouble(props.getOrDefault(6, "0"));
        }

        public int getCardinalRotation() {
            long rounded = Math.round(getRotation() / 90.0) * 90;
            return (int) (((rounded % 360) + 360) % 360);
        }

        public String getGDJEString() {
            int jeid = nameToJEID.getOrDefault(GDIDtoName.getOrDefault(getId(), null), -1);
            String type = jeid < 1000 ? "obj" : "deco";
            int x = convertX((int) getX());
            int y = convertY((int) getY());
            String ifh = isFlippedHorizontally() ? "h1" : "h0";
            String ifv =  isFlippedVertically() ? "v1" : "v0";
            String d = "d" + (int) getRotation();
            return type + " " + jeid + " " + x + " " + y + " " + ifh + " " + ifv + " " + d;
        }

        @Override
        public String toString() {
            return "GDObject" + props;
        }
    }

    public static List<String> extractObjectStrings(String innerLevelString) {
        int headerEnd = innerLevelString.indexOf(';');
        String body = (headerEnd == -1) ? "" : innerLevelString.substring(headerEnd + 1);

        List<String> objects = new ArrayList<>();
        for (String part : body.split(";")) {
            if (!part.isEmpty()) {
                objects.add(part);
            }
        }
        return objects;
    }

    public static Map<Integer, String> parseObjectProperties(String objectString) {
        String[] tokens = objectString.split(",");
        Map<Integer, String> props = new LinkedHashMap<>();
        for (int i = 0; i + 1 < tokens.length; i += 2) {
            int key = Integer.parseInt(tokens[i]);
            props.put(key, tokens[i + 1]);
        }
        return props;
    }


}
