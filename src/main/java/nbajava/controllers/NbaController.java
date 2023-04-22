package nbajava.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nbajava.models.Team;
import nbajava.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/nba")
public class NbaController {

    private final DataService dataService;

    @Autowired
    public NbaController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * curl -v http://localhost:8080/nba/team\?city\=BOS
     * http://localhost:8080/nba/team?city=BOS
     */
    @GetMapping("/team")
    public String getTeam(
            @RequestParam(value = "city", defaultValue = "") String city,
            @RequestParam(value = "year", defaultValue = "") String year
    ) {
        Team team = this.dataService.getTeam(city, year);
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(team);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
            return "Could not process team object. Try again.";
        }
    }

    /**
     * curl -v http://localhost:8080/nba/seasonYears
     * http://localhost:8080/nba/seasonYears
     *
     * Returns available seasons to analyze
     */
    @GetMapping("/seasonYears")
    public String getSeasonYears() {
        ArrayList<String> seasonYears = this.dataService.getSeasonYears();
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(seasonYears);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
            return "Could not process season years. Try again.";
        }
    }
}
