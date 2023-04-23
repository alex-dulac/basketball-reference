package nbajava.services;

import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    /**
     * A utility that takes a seasonYear ("YYYY-XX") format and returns the "YYXX" format.
     * Example: 2022-23 seasonYear returns 2023
     * Note: This is totally assuming what's passed in is in the expected format right now.
     */
    public String getYearFromSeasonYear(String seasonYear) {
        return seasonYear.substring(0, 2) + seasonYear.substring(5, 7);
    }
}
