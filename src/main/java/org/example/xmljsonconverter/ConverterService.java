package org.example.xmljsonconverter;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class ConverterService {

    public String xmlToJsonManual(String xml) {
        if (xml == null || xml.trim().isEmpty()) return "ERREUR : Champ vide.";
        String input = xml.replaceAll("<\\?xml.*?\\?>", "").trim();

        Pattern rootPattern = Pattern.compile("^<(\\w+)>(.*)</\\1>$", Pattern.DOTALL);
        Matcher rootMatcher = rootPattern.matcher(input);

        if (!rootMatcher.matches()) {
            return "ERREUR : XML invalide (Pas de racine unique ou balise racine mal fermée).";
        }

        String rootName = rootMatcher.group(1);
        String content = rootMatcher.group(2).trim();

        try {
            Object parsedData = parseXmlWithValidation(content);

            return "{\n  \"" + rootName + "\": " + formatAsJson(parsedData, "  ") + "\n}";
        } catch (Exception e) {
            return "ERREUR : " + e.getMessage();
        }
    }

    private Object parseXmlWithValidation(String content) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("<(\\w+)>(.*?)</\\1>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        int lastIndex = 0;
        boolean foundAnyTag = false;

        while (matcher.find()) {
            String gap = content.substring(lastIndex, matcher.start()).trim();
            if (gap.contains("<") || gap.contains(">")) {
                throw new Exception("Structure corrompue ou balise non fermée détectée près de : \"" + gap + "\"");
            }

            foundAnyTag = true;
            String key = matcher.group(1);
            String inner = matcher.group(2).trim();

            Object value = (inner.contains("<") && inner.contains(">")) ? parseXmlWithValidation(inner) : inner;

            if (map.containsKey(key)) {
                Object existing = map.get(key);
                if (existing instanceof List) {
                    ((List<Object>) existing).add(value);
                } else {
                    List<Object> list = new ArrayList<>();
                    list.add(existing);
                    list.add(value);
                    map.put(key, list);
                }
            } else {
                map.put(key, value);
            }
            lastIndex = matcher.end();
        }

        String remainder = content.substring(lastIndex).trim();
        if (remainder.contains("<") || remainder.contains(">")) {
            throw new Exception("Balise non fermée ou mal formée à la fin : \"" + remainder + "\"");
        }

        return foundAnyTag ? map : content;
    }


    private String formatAsJson(Object obj, String indent) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            String nextIndent = indent + "  ";
            return "{\n" + map.entrySet().stream()
                    .map(e -> nextIndent + "\"" + e.getKey() + "\": " + formatAsJson(e.getValue(), nextIndent))
                    .collect(Collectors.joining(",\n")) + "\n" + indent + "}";
        } else if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            return "[" + list.stream().map(o -> formatAsJson(o, indent)).collect(Collectors.joining(", ")) + "]";
        } else {
            String s = String.valueOf(obj);
            if (s.matches("-?\\d+") || s.equals("true") || s.equals("false")) return s;
            return "\"" + s + "\"";
        }
    }


    public String jsonToXmlManual(String json) {
        if (json == null || json.trim().isEmpty()) return "ERREUR : Champ vide.";
        try {
            Pattern p = Pattern.compile("^\\{\\s*\"(\\w+)\"\\s*:\\s*\\{(.*)\\}\\s*\\}$", Pattern.DOTALL);
            Matcher m = p.matcher(json.trim());
            if (!m.matches()) return "ERREUR : Format JSON non supporté (manque racine ou accolades).";

            String root = m.group(1);
            return "<" + root + ">\n" + jsonToXmlRecursive(m.group(2).trim(), "  ") + "</" + root + ">";
        } catch (Exception e) {
            return "ERREUR : Structure JSON mal formée.";
        }
    }

    private String jsonToXmlRecursive(String content, String indent) {
        StringBuilder xml = new StringBuilder();
        Pattern pattern = Pattern.compile("\"(\\w+)\"\\s*:\\s*(\\{.*?\\}|\\[.*?\\]|\".*?\"|[^,\\s\\}]+)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String key = matcher.group(1);
            String val = matcher.group(2).trim();

            if (val.startsWith("{")) {
                String inner = val.substring(1, val.length() - 1).trim();
                xml.append(indent).append("<").append(key).append(">\n")
                        .append(jsonToXmlRecursive(inner, indent + "  "))
                        .append(indent).append("</").append(key).append(">\n");
            } else if (val.startsWith("[")) {
                String[] items = val.substring(1, val.length() - 1).split(",");
                for (String item : items) {
                    xml.append(indent).append("<").append(key).append(">")
                            .append(item.trim().replaceAll("^\"|\"$", ""))
                            .append("</").append(key).append(">\n");
                }
            } else {
                xml.append(indent).append("<").append(key).append(">")
                        .append(val.replaceAll("^\"|\"$", ""))
                        .append("</").append(key).append(">\n");
            }
        }
        return xml.toString();
    }
}