import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Blago on 05.07.2017.
 */
public class Parser {
    private static Document getPage() throws IOException {
        String url = "https://yandex.ru/pogoda/moscow/details?from=serp_title";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }
    static private Pattern pattern = Pattern.compile("\\d{2}");

    static private String getDateFromString(String stringDate) throws Exception {

                Matcher matcher = pattern.matcher(stringDate);
                if (matcher.find()) {
                    return matcher.group();
                }
                throw new Exception("Cant words");
    }
    
    private static int printScreenValues(Elements values, int index){
        int iterationCount = 1;
        for (int i = 0; i < iterationCount; i++) {
            Element valuesLine = values.get(index+i);
            for (Element tbody : valuesLine.select("tr")) {
                String[] wordPrint = tbody.text().trim().split("\\s+");
                for (int j = 0; j < wordPrint.length; j++) {
                        System.out.print(wordPrint[j] + "    ");
                }
                System.out.println();
            }
            System.out.println();
        }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWeather = page.select("div[class=tabs-panes__pane tabs-panes__pane_active_yes]").first();
        Elements names = tableWeather.select("dt[data-anchor]");
        Elements values = tableWeather.select("tbody[class=weather-table__body]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("strong[class=forecast-detailed__day-number]").text();
            String dateMonth = name.select("span[class=forecast-detailed__day-month").text();
            String date = getDateFromString(dateString);
            System.out.println(date +" "+ dateMonth + "   температура   явление    давление    влажность    ветер    ощущение");
            int iterationCount = printScreenValues(values, index);
            index = index + iterationCount;
        }
    }
}
