/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

import java.sql.SQLException;

public class Batsman {
    private int player_ID;
    private int runs;
    private int centuries;
    private int half_centuries;
    private int top_score;
    private double batting_avg;
    private double strike_rate;
    private String batting_style;

    public int getPlayer_ID() {
        return player_ID;
    }

    public void setPlayer_ID(int player_ID) {
        this.player_ID = player_ID;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getCenturies() {
        return centuries;
    }

    public void setCenturies(int centuries) {
        this.centuries = centuries;
    }

    public int getHalf_centuries() {
        return half_centuries;
    }

    public void setHalf_centuries(int half_centuries) {
        this.half_centuries = half_centuries;
    }

    public int getTop_score() {
        return top_score;
    }

    public void setTop_score(int top_score) {
        this.top_score = top_score;
    }

    public double getBatting_avg() {
        return batting_avg;
    }

    public void setBatting_avg(double batting_avg) {
        this.batting_avg = batting_avg;
    }

    public double getStrike_rate() {
        return strike_rate;
    }

    public void setStrike_rate(double strike_rate) {
        this.strike_rate = strike_rate;
    }

    public String getBatting_style() {
        return batting_style;
    }

    public void setBatting_style(String batting_style) {
        this.batting_style = batting_style;
    }

    @Override
    public String toString() {
        return "Batsman{" +
                "player_ID=" + player_ID +
                ", runs=" + runs +
                ", centuries=" + centuries +
                ", half_centuries=" + half_centuries +
                ", top_score=" + top_score +
                ", batting_avg=" + batting_avg +
                ", strike_rate=" + strike_rate +
                ", batting_style='" + batting_style + '\'' +
                '}';
    }

    public Batsman(int player_ID, String batting_style) {
        this.player_ID = player_ID;
        this.batting_style = batting_style;
    }

    public boolean insertBatsman() {
        try {
            DatabaseConnection dc = new DatabaseConnection();
            String insert = "INSERT INTO CRICBUZZ.BATSMAN (PLAYER_ID, BATTING_STYLE) " +
                    "VALUES (" + player_ID + "," + " '" + batting_style + "')";
            boolean v = dc.doUpate(insert);
            if(v) {
                System.out.println("Batsman insert is successful");
                return true;
            }
            else {
                String del = "DELETE FROM CRICBUZZ.PLAYER WHERE PLAYER_ID = " + player_ID;
                dc.doUpate(del);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Failed to create statement Javacode\\Bowler :: " + e);
        }
        return false;
    }
}
