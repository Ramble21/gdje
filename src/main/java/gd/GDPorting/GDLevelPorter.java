package gd.GDPorting;

import java.util.*;
import java.util.stream.Collectors;

public class GDLevelPorter {
    private static final HashMap<Integer, String> GDIDtoName = new HashMap<>(Map.ofEntries(
            Map.entry(1, "DefaultBlock"),
            Map.entry(2, "Block1Edge"),
            Map.entry(3, "Block1Angle"),
            Map.entry(4, "Block1Corner"),
            Map.entry(5, "Block1Empty"),
            Map.entry(6, "Block1Full"),
            Map.entry(7, "Block1Sides"),
            Map.entry(8, "DefaultSpike"),
            Map.entry(9, "GroundHazard"),
            Map.entry(10, "PortalGravityP"),
            Map.entry(11, "PortalGravityN"),
            Map.entry(12, "PortalCube"),
            Map.entry(13, "PortalShip"),
            Map.entry(15, "PulseRodL"),
            Map.entry(16, "PulseRodM"),
            Map.entry(17, "PulseRodS"),
            Map.entry(18, "SpikeDecoXL"),
            Map.entry(19, "SpikeDecoL"),
            Map.entry(20, "SpikeDecoM"),
            Map.entry(21, "SpikeDecoS"),
            Map.entry(29, "BackgroundTrigger"),
            Map.entry(30, "GroundTrigger"),
            Map.entry(39, "HalfSpike"),
            Map.entry(40, "DefaultSlab"),
            Map.entry(41, "ChainXL"),
            Map.entry(54, "PulseStar"),
            Map.entry(62, "SwirlSlab"),
            Map.entry(65, "SwirlSlabHalf"),
            Map.entry(103, "SmallSpike"),
            Map.entry(1329, "Coin")
    ));

    private static final HashMap<String, Integer> nameToJEID = new HashMap<>(Map.ofEntries(
            Map.entry("DefaultBlock", 1),
            Map.entry("DefaultSpike", 2),
            Map.entry("DefaultSlab", 3),
            Map.entry("HalfSpike", 4),
            Map.entry("GroundHazard", 5),
            Map.entry("Block1Full", 6),
            Map.entry("Block1Edge", 7),
            Map.entry("Block1Angle", 8),
            Map.entry("Block1Sides", 9),
            Map.entry("Block1Corner", 10),
            Map.entry("PortalGravityP", 11),
            Map.entry("PortalGravityN", 12),
            Map.entry("PortalCube", 13),
            Map.entry("PortalShip", 14),
            Map.entry("SmallSpike", 30),
            Map.entry("Coin", 67),
            Map.entry("SwirlSlab", 72),
            Map.entry("SwirlSlabHalf", 73),
            Map.entry("Block1Empty", 1001),
            Map.entry("SpikeDecoS", 1010),
            Map.entry("SpikeDecoM", 1011),
            Map.entry("SpikeDecoL", 1012),
            Map.entry("SpikeDecoXL", 1013),
            Map.entry("PulseRodS", 1014),
            Map.entry("PulseRodM", 1015),
            Map.entry("PulseRodL", 1016),
            Map.entry("ChainXL", 1020),
            Map.entry("PulseStar", 1021),
            Map.entry("BackgroundTrigger", 2001),
            Map.entry("GroundTrigger", 2002)
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
            GDObject obj = new GDObject(parseObjectProperties(raw));
            if (obj.getId() == 40 || obj.getId() == 62 || obj.getId() == 65) {
                // robtop being stupid with slabs
                obj.setY((int) (obj.getY() - 7));
            }
            objects.add(obj);
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

        public String getGDJEString() {
            int jeid = nameToJEID.getOrDefault(GDIDtoName.getOrDefault(getId(), null), -1);
            String type = jeid < 0 ? "nan~" + getId() : jeid < 1000 ? "obj" : jeid < 2000 ? "deco" : "trig";
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

        public void setY(int i) {
            props.put(3, Integer.toString(i));
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
