/*
 * Author MD. Sakibur Reza
 */

package TableView;

public class EverydayMatch {
    String matchDetails;
    String time;
    String hostTeam,visitingTeam;
    Integer matchID;

    public EverydayMatch(String matchDetails, String time, String hostTeam, String visitingTeam, int matchID) {
        this.matchDetails = matchDetails;
        this.time = time;
        this.hostTeam = hostTeam;
        this.visitingTeam = visitingTeam;
        this.matchID = matchID;
    }

    public EverydayMatch(String matchDetails, String time) {
        this.matchDetails = matchDetails;
        this.time = time;
    }

    public String getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(String matchDetails) {
        this.matchDetails = matchDetails;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHostTeam() {
        return hostTeam;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public int getMatchID() {
        return matchID;
    }

    @Override
    public String toString() {
        return hostTeam+" "+visitingTeam;
    }
}
