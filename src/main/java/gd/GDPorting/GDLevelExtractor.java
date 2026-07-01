package gd.GDPorting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Java port of u/GarlicBurrito's Python GD save-file level extraction script
 * (<a href="https://www.reddit.com/r/geometrydash/comments/155qdgo/comment/jy4tfpm/">...</a>)
 */

public class GDLevelExtractor {

    private static final int XOR_KEY = 11;
    private static final String OFFICIAL_LEVEL_MAGIC = "H4sIAAAAAAAAA";

    public static String extractLevel(String saveFilePath) throws Exception {
        return extractLevel(saveFilePath, null);
    }

    public static String extractLevel(String saveFilePath, String levelName) throws Exception {
        Element levelContainer = loadLevelContainer(saveFilePath);
        List<Element> containerChildren = getChildElements(levelContainer);

        for (int lInd = 3; lInd < containerChildren.size(); lInd += 2) {
            List<Element> levelDict = getChildElements(containerChildren.get(lInd));

            for (int i = 0; i < levelDict.size(); i++) {
                if ("k2".equals(levelDict.get(i).getTextContent())) {
                    String name = levelDict.get(i + 1).getTextContent();
                    String data = levelDict.get(i + 3).getTextContent();

                    if (levelName == null || levelName.equals(name)) {
                        return decodeLevel(data, false);
                    }
                    break;
                }
            }
        }

        throw new NoSuchElementException(
                levelName == null ? "No levels found in save file" : "Level not found: " + levelName);
    }

    public static List<String> listLevelNames(String saveFilePath) throws Exception {
        Element levelContainer = loadLevelContainer(saveFilePath);
        List<Element> containerChildren = getChildElements(levelContainer);
        List<String> names = new ArrayList<>();

        for (int lInd = 3; lInd < containerChildren.size(); lInd += 2) {
            List<Element> levelDict = getChildElements(containerChildren.get(lInd));
            for (int i = 0; i < levelDict.size(); i++) {
                if ("k2".equals(levelDict.get(i).getTextContent())) {
                    names.add(levelDict.get(i + 1).getTextContent());
                    break;
                }
            }
        }
        return names;
    }

    private static Element loadLevelContainer(String saveFilePath) throws Exception {
        byte[] raw = Files.readAllBytes(Path.of(saveFilePath));
        raw = stripUtf8Bom(raw);

        String xml = decryptData(new String(raw, StandardCharsets.UTF_8));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        Element root = doc.getDocumentElement();
        Element level0 = getChildElements(root).getFirst();
        return getChildElements(level0).get(1);
    }

    private static byte[] stripUtf8Bom(byte[] data) {
        if (data.length >= 3
                && (data[0] & 0xFF) == 0xEF
                && (data[1] & 0xFF) == 0xBB
                && (data[2] & 0xFF) == 0xBF) {
            byte[] stripped = new byte[data.length - 3];
            System.arraycopy(data, 3, stripped, 0, stripped.length);
            return stripped;
        }
        return data;
    }

    private static List<Element> getChildElements(Element parent) {
        List<Element> elements = new ArrayList<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) n);
            }
        }
        return elements;
    }

    public static String xor(String input, int key) {
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            sb.append((char) (input.charAt(i) ^ key));
        }
        return sb.toString();
    }

    public static String decryptData(String data) throws IOException {
        String xored = xor(data, XOR_KEY);

        StringBuilder cleaned = new StringBuilder(xored.length());
        for (int i = 0; i < xored.length(); i++) {
            char c = xored.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '=') {
                cleaned.append(c);
            }
        }

        byte[] base64Decoded = Base64.getUrlDecoder().decode(cleaned.toString().getBytes(StandardCharsets.UTF_8));
        return gunzip(base64Decoded);
    }


    public static String encryptData(String data) throws IOException {
        byte[] gzipped = gzip(data);
        String base64Encoded = Base64.getUrlEncoder().encodeToString(gzipped);
        return xor(base64Encoded, XOR_KEY);
    }

    public static String encodeLevel(String levelString, boolean isOfficialLevel) throws IOException {
        byte[] gzipped = gzip(levelString);
        String base64Encoded = Base64.getUrlEncoder().encodeToString(gzipped);
        if (isOfficialLevel) {
            base64Encoded = base64Encoded.substring(13);
        }
        return base64Encoded;
    }

    public static String decodeLevel(String levelData, boolean isOfficialLevel) throws IOException, DataFormatException {
        if (isOfficialLevel) {
            levelData = OFFICIAL_LEVEL_MAGIC + levelData;
        }
        byte[] base64Decoded = Base64.getUrlDecoder().decode(levelData.getBytes(StandardCharsets.UTF_8));
        return decompressAutoDetect(base64Decoded);
    }

    private static byte[] gzip(String data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(data.getBytes(StandardCharsets.UTF_8));
        }
        return baos.toByteArray();
    }

    private static String gunzip(byte[] data) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data))) {
            return readAll(gis);
        }
    }

    private static String decompressAutoDetect(byte[] data) throws IOException, DataFormatException {
        boolean looksGzip = data.length >= 2 && (data[0] & 0xFF) == 0x1f && (data[1] & 0xFF) == 0x8b;
        if (looksGzip) {
            return gunzip(data);
        }
        try {
            return inflate(data, false);
        } catch (DataFormatException e) {
            return inflate(data, true);
        }
    }

    private static String inflate(byte[] data, boolean nowrap) throws DataFormatException {
        Inflater inflater = new Inflater(nowrap);
        inflater.setInput(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                if (count == 0) {
                    if (inflater.needsInput() || inflater.needsDictionary()) break;
                }
                out.write(buffer, 0, count);
            }
        } finally {
            inflater.end();
        }
        return out.toString(StandardCharsets.UTF_8);
    }

    private static String readAll(java.io.InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while ((n = is.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toString(StandardCharsets.UTF_8);
    }
}