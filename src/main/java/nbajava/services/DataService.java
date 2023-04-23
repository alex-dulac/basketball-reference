package nbajava.services;

import nbajava.models.Team;
import nbajava.models.TeamStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DataService {

    private final ScraperService scraperService;
    private final UtilityService utilityService;

    @Autowired
    public DataService(ScraperService scraperService, UtilityService utilityService) {
        this.scraperService = scraperService;
        this.utilityService = utilityService;
    }

    public Team getTeam(String city, String year) {
        return this.scraperService.getTeam(city, year);
    }

    public ArrayList<String> getSeasonYears() {
        return this.scraperService.getSeasonYears();
    }

    public TeamStats getTeamStatsToCompareByTeam(String city, String year) {
        final Team team = this.getTeam(city, year);
        team.setYear(this.utilityService.getYearFromSeasonYear(team.getSeasonYear()));
        TeamStats teamStats = this.scraperService.getTeamStatsFromRatingPage(team);
        return this.scraperService.getTeamStatsFromTeamPage(teamStats);
    }
}
