package nbajava.services;

import nbajava.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    private final ScraperService scraperService;

    @Autowired
    public DataService(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    public Team getTeam(String city, String year) {
        return this.scraperService.getTeam(city, year);
    }
}
