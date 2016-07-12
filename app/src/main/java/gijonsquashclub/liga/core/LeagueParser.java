package gijonsquashclub.liga.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

class LeagueParser {
    private static final String URL = "http://www.gijonsquashclub.com/la_liga.php";
    private static final String DIV_SELECTOR = "div#main_container_liga";

    static Map<Integer, Map<String, LinkedList<Integer>>> getGroups() {
        Map<Integer, Map<String, LinkedList<Integer>>> groups = new LinkedHashMap<>();

        try {
            Document document = Jsoup.connect(URL).get();
            Integer group = 0;
            for (Element table : document.select(DIV_SELECTOR).select("table")) {
                Map<String, LinkedList<Integer>> player = new LinkedHashMap<>();

                Element firstTRElement = table.select("tr").get(0);
                String firstTRString = firstTRElement.text();

                // Obtain the group
                if (firstTRString.contains("Grupo ")) {
                    String firstTD = firstTRElement.select("td").get(0).text();
                    group = Integer.parseInt(firstTD.substring(
                            firstTD.length() - 2).trim());
                }

                // Obtain the info for the player (name and points)
                for (int i = 1; i < table.select("tr").size(); i++) {
                    LinkedList<Integer> points = new LinkedList<>();

                    Element row = table.select("tr").get(i);
                    String namePlayer = row.select("td").get(0).text();
                    for (int j = 1; j < row.select("td").size(); j++) {
                        Element td = row.select("td").get(j);
                        if (td.text().isEmpty())
                            points.add(-1);
                        else
                            points.add(Integer.parseInt(td.text()));
                    }
                    player.put(namePlayer, points);
                }

                if (group != 0)
                    groups.put(group, player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return groups;
    }
}
