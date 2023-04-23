package nbajava.services;

import nbajava.models.Team;
import nbajava.models.TeamStats;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

/**
 * Okay, it's not really an API service, but I'm not really sure what to call it right now
 */
@Service
public class ApiService {

    private static final String baseUrl = "https://www.basketball-reference.com/";
    private static final String teamsPage = "teams/";
    private static final String leaguesPage = "leagues/";
    private static final String ratingsPrefix = "NBA_";
    private static final String ratingsSuffix = "_ratings.html";

    public HtmlPage getTeam(String city, String year) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + teamsPage + city;

        try {
            return client.getPage(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public HtmlPage getSeasonYears() {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + leaguesPage;

        try {
            return client.getPage(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public HtmlPage getTeamStatsFromRatingPage(Team team) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        final String url = baseUrl + leaguesPage + ratingsPrefix + team.getYear() + ratingsSuffix;

        try {
            return client.getPage(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Jsoup here instead of WebClient because the table is commented out when returned.
     * So let's strip out the comments and re-parse with Jsoup.
     */
    public Document getTeamStatsFromTeamPage(TeamStats teamStats) {
        final String url = baseUrl + teamsPage + teamStats.team.abbreviation + "/" + teamStats.team.getYear() + ".html";

        try {
            // We need to get the document via Jsoup, remove comments from the HTML string, and have Jsoup parse it back
            Document document = Jsoup.connect(url).get();
            String htmlString = document.html();
            htmlString = htmlString.replaceAll("<!--|-->","");
            return Jsoup.parse(htmlString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
