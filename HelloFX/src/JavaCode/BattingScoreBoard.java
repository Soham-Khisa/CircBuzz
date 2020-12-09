/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BattingScoreBoard {
    int batting_ID;
    int player_ID;
    int innings_num;
    int match_ID;
    int runs;
    int balls;
    int fours;
    int sixes;
    int out_by;
    int out_contributor;
    int out_id;
    String status="YET TO BAT";

    public BattingScoreBoard(int player_ID, int innings_num, int match_ID) {
        this.player_ID = player_ID;
        this.innings_num = innings_num;
        this.match_ID = match_ID;
    }
    public void makeBattingScore() throws SQLException {
        DatabaseConnection dc= new DatabaseConnection();
        String query="SELECT * FROM BATTING_SCORE\n" +
                "WHERE MATCH_ID="+match_ID+" AND INNINGS_NO="+innings_num+" AND PLAYER_ID="+player_ID;
        ResultSet rs = dc.getQueryResult(query);
        batting_ID=rs.getInt("BATTING_ID");
        out_by=rs.getInt("OUT_BY");
        out_contributor=rs.getInt("OUT_CONTRIBUTOR");
        out_id=rs.getInt("OUT_ID");
        runs=rs.getInt("RUNS");
        balls=rs.getInt("BALLS");
        fours=rs.getInt("FOURS");
        sixes=rs.getInt("SIXES");
        status=rs.getString("STATUS");
    }

    public int getBatting_ID() {
        return batting_ID;
    }
    public void insert() throws SQLException {
        DatabaseConnection dc= new DatabaseConnection();
        String query="INSERT INTO BATTING_SCORE(PLAYER_ID,INNINGS_NO,MATCH_ID,STATUS)\n" +
                "VALUES(" + player_ID + "," + innings_num + "," + match_ID + ",'" + status + "')";
        String generatedColumn[] = {"BATTING_ID"};
        dc.doUpdate(query, generatedColumn);
    }
}

