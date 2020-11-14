/*
 * Author MD. Sakibur Reza
 */

package TableView;

public class FixtureTable {
    String date;
    String MatchDetails;
    String time;

    public FixtureTable(String date, String matchDetails, String time) {
        this.date = date;
        MatchDetails = matchDetails;
        this.time = time;
    }

    public FixtureTable() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatchDetails() {
        return MatchDetails;
    }

    public void setMatchDetails(String matchDetails) {
        MatchDetails = matchDetails;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
