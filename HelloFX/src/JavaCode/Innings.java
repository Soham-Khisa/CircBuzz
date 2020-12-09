/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

import java.sql.SQLException;

public class Innings {
    int innings_no;
    int match_ID;
    int batting_team;
    int bowling_team;
    int total_run;
    int wickets;
    int fours;
    int sixes;
    int overs;
    int wide;
    int no_balls;
    int bys_runs;
    int leg_byes;
    String status;

    public Innings(int innings_no, int match_ID, int batting_team, int bowling_team) throws SQLException {
        this.innings_no = innings_no;
        this.match_ID = match_ID;
        this.batting_team = batting_team;
        this.bowling_team = bowling_team;
        DatabaseConnection dc= new DatabaseConnection();
        String query="INSERT INTO INNINGS(MATCH_ID,INNINGS_NO,BATTING_TEAM,BOWLING_TEAM)\n" +
                "VALUES("+match_ID+","+innings_no+","+batting_team+","+bowling_team+")";
        dc.insert(query);
    }

    public Innings() {
    }

    public int getInnings_no() {
        return innings_no;
    }

    public int getMatch_ID() {
        return match_ID;
    }

    public int getBatting_team() {
        return batting_team;
    }

    public int getBowling_team() {
        return bowling_team;
    }
}
