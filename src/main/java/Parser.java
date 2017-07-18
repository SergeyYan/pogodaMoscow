import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    
    private static int printScreenValues(Elements values, int index) {
        int iterationCount = 1;
        ArrayList frashWordPrint = new ArrayList();
        for (int i = 0; i < iterationCount; i++) {
            Element valuesLine = values.get(index + i);
            for (Element tbody : valuesLine.select("tr")) {
                String[] wordPrint = tbody.text().trim().split("\\s+");
                for (int j = 0; j < wordPrint.length; j++) {
                    if (wordPrint.length == 7) {
                        System.out.print(wordPrint[j] + "       ");
                    } else if (wordPrint.length == 8) {
                        for (int k = 0; k < 8; k++) {
                            frashWordPrint.add(wordPrint[k]);
                        }
                                String timesWeather = (wordPrint[2] + " " + wordPrint[3]);
                                frashWordPrint.set(2, timesWeather);
                                frashWordPrint.remove(3);
                        if(j==7){
                            break;
                        }
                        System.out.print(frashWordPrint.get(j) + "       ");
                        frashWordPrint.clear();
                    }
                    else if (wordPrint.length == 9) {
                        for (int k = 0; k < 9; k++) {
                            frashWordPrint.add(wordPrint[k]);
                        }
                        String timesWeather = (wordPrint[2] + " " + wordPrint[3] +" " +wordPrint[4]);
                        frashWordPrint.set(2, timesWeather);
                        frashWordPrint.remove(4);
                        frashWordPrint.remove(3);
                        if(j==7){
                            break;
                        }
                        System.out.print(frashWordPrint.get(j) + "       ");
                        frashWordPrint.clear();
                    }
                }
                System.out.println();
                frashWordPrint.clear();
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
            System.out.println(date +" "+ dateMonth + "   температура        явление         давление    влажность    ветер    ощущение");
            int iterationCount = printScreenValues(values, index);
            index = index + iterationCount;
        }
    }
}
