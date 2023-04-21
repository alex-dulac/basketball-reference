package nbajava.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    private static final String baseUrl = "https://www.basketball-reference.com/";

    public String getTeamName(String team) {
        try {
            Document document = Jsoup.connect(baseUrl).get();
            return document.title();
        } catch (Exception e) {
            return "";
        }
    }
}
