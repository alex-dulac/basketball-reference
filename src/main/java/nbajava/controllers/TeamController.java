package nbajava.controllers;

import nbajava.models.TeamTest;
import nbajava.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/nba")
public class TeamController {

    @Autowired
    private DataService dataService;

    /* curl -v http://localhost:8080/nba/team\?team\=Celtics */
    @GetMapping("/team")
    public TeamTest getTeam(@RequestParam(value = "team", defaultValue = "") String team) {
        String title = dataService.getTeamName(team);
        return new TeamTest(team, title);
    }
}
