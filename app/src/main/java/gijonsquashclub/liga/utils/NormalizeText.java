package gijonsquashclub.liga.utils;

import java.util.HashMap;
import java.util.Map;

public class NormalizeText {
    private static Map<Character, Character> MAP_NORM;

    public static String removeAccents(String name) {
        name = name.toLowerCase();
        if (MAP_NORM == null || MAP_NORM.size() == 0) {
            MAP_NORM = new HashMap<>();
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ú', 'u');
        }

        if (name == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(name);

        for (int i = 0; i < name.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c);
            }
        }

        return sb.toString();
    }
}
