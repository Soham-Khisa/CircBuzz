package JavaCode;

import Database.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Match {
    private int matchID;
    private int fixtureID;
    private int tourID;
    private int u1;
    private int u2;
    private int u3;
    private int hostTeam;
    private int visitingTeam;
    private int winner;
    private int matchTypeID;
    private String match_status;
    private int manOftheMatch;
    private int tossWinner;
    private String tossDecision;
    private int hostTeamCaptain;
    private int visitingTeamCaptain;
    private int hostTeamWK;
    private int visitingTeamWK;
    int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public Match(int matchID) throws SQLException {
        this.matchID = matchID;
        DatabaseConnection dc= new DatabaseConnection();
        String query="SELECT * FROM MATCH\n" +
                "WHERE MATCH_ID="+matchID;
        ResultSet rs= dc.getQueryResult(query);
        if(rs.next()){
            fixtureID=rs.getInt("FIXTURE_ID");
            tourID=rs.getInt("TOUR_ID");
            hostTeam=rs.getInt("HOST_TEAM");
            visitingTeam=rs.getInt("VISITING_TEAM");
            matchTypeID=rs.getInt("MATCH_TYPE_ID");
            match_status=rs.getString("MATCH_STATUS");
        }

    }
    public void updateDay(int id) throws SQLException {
        matchID=id;
        DatabaseConnection dc= new DatabaseConnection();
        String query= "UPDATE MATCH\n" +
                "SET DAY=NVL(DAY,0)+1\n" +
                "WHERE MATCH_ID="+matchID;
        dc.doUpdate(query);
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public int getFixtureID() {
        return fixtureID;
    }

    public void setFixtureID(int fixtureID) {
        this.fixtureID = fixtureID;
    }

    public int getTourID() {
        return tourID;
    }

    public void setTourID(int tourID) {
        this.tourID = tourID;
    }

    public int getU1() {
        return u1;
    }

    public void setU1(int u1) {
        this.u1 = u1;
    }

    public int getU2() {
        return u2;
    }

    public void setU2(int u2) {
        this.u2 = u2;
    }

    public int getU3() {
        return u3;
    }

    public void setU3(int u3) {
        this.u3 = u3;
    }

    public int getHostTeam() {
        return hostTeam;
    }

    public void setHostTeam(int hostTeam) {
        this.hostTeam = hostTeam;
    }

    public int getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(int visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getMatchTypeID() {
        return matchTypeID;
    }

    public void setMatchTypeID(int matchTypeID) {
        this.matchTypeID = matchTypeID;
    }

    public String getMatch_status() {
        return match_status;
    }

    public void setMatch_status(String match_status) {
        this.match_status = match_status;
    }

    public int getManOftheMatch() {
        return manOftheMatch;
    }

    public void setManOftheMatch(int manOftheMatch) {
        this.manOftheMatch = manOftheMatch;
    }

    public int getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(int tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getTossDecision() {
        return tossDecision;
    }

    public void setTossDecision(String tossDecision) {
        this.tossDecision = tossDecision;
    }

    public int getHostTeamCaptain() {
        return hostTeamCaptain;
    }

    public void setHostTeamCaptain(int hostTeamCaptain) {
        this.hostTeamCaptain = hostTeamCaptain;
    }

    public int getVisitingTeamCaptain() {
        return visitingTeamCaptain;
    }

    public void setVisitingTeamCaptain(int visitingTeamCaptain) {
        this.visitingTeamCaptain = visitingTeamCaptain;
    }

    public int getHostTeamWK() {
        return hostTeamWK;
    }

    public void setHostTeamWK(int hostTeamWK) {
        this.hostTeamWK = hostTeamWK;
    }

    public int getVisitingTeamWK() {
        return visitingTeamWK;
    }

    public void setVisitingTeamWK(int visitingTeamWK) {
        this.visitingTeamWK = visitingTeamWK;
    }
}
