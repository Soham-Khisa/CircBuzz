/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Bowler {
    private int playerID;
    private int wickets;
    private int balls_bowled;
    private int five_wickets_haul;
    private int ten_wickets_haul;
    private String best_bowling = null;
    private double bowling_strike_rate;
    private double bowling_economy_rate;
    private double bowling_avg;
    private String bowling_style = null;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getBalls_bowled() {
        return balls_bowled;
    }

    public void setBalls_bowled(int balls_bowled) {
        this.balls_bowled = balls_bowled;
    }

    public int getFive_wickets_haul() {
        return five_wickets_haul;
    }

    public void setFive_wickets_haul(int five_wickets_haul) {
        this.five_wickets_haul = five_wickets_haul;
    }

    public int getTen_wickets_haul() {
        return ten_wickets_haul;
    }

    public void setTen_wickets_haul(int ten_wickets_haul) {
        this.ten_wickets_haul = ten_wickets_haul;
    }

    public String getBest_bowling() {
        return best_bowling;
    }

    public void setBest_bowling(String best_bowling) {
        this.best_bowling = best_bowling;
    }

    public double getBowling_strike_rate() {
        return bowling_strike_rate;
    }

    public void setBowling_strike_rate(double bowling_strike_rate) {
        this.bowling_strike_rate = bowling_strike_rate;
    }

    public double getBowling_economy_rate() {
        return bowling_economy_rate;
    }

    public void setBowling_economy_rate(double bowling_economy_rate) {
        this.bowling_economy_rate = bowling_economy_rate;
    }

    public double getBowling_avg() {
        return bowling_avg;
    }

    public void setBowling_avg(double bowling_avg) {
        this.bowling_avg = bowling_avg;
    }

    public String getBowling_style() {
        return bowling_style;
    }

    public void setBowling_style(String bowling_style) {
        this.bowling_style = bowling_style;
    }

    @Override
    public String toString() {
        return "Bowler{" +
                "playerID=" + playerID +
                ", wickets=" + wickets +
                ", balls_bowled=" + balls_bowled +
                ", five_wickets_haul=" + five_wickets_haul +
                ", ten_wickets_haul=" + ten_wickets_haul +
                ", best_bowling='" + best_bowling + '\'' +
                ", bowling_strike_rate=" + bowling_strike_rate +
                ", bowling_economy_rate=" + bowling_economy_rate +
                ", bowling_avg=" + bowling_avg +
                ", bowling_style='" + bowling_style + '\'' +
                '}';
    }

    public Bowler(int playerID, String bowling_style) {
        this.playerID = playerID;
        this.bowling_style = bowling_style;
    }

    public Bowler(int playerID) {
        this.playerID = playerID;
    }

    public boolean insertBowler() {
        try {
            DatabaseConnection dc = new DatabaseConnection();
            String insert1 = "INSERT INTO CRICBUZZ.BOWLER (PLAYER_ID) " +
                    "VALUES (" + playerID + ")";
            String insert2 = "INSERT INTO CRICBUZZ.BOWLER (PLAYER_ID, BOWLING_STYLE) " +
                            "VALUES (" + playerID + "," + " '" + bowling_style + "')";
            boolean v = false;
            if(bowling_style==null) {
                v = dc.doUpate(insert1);
            }
            else {
                v = dc.doUpate(insert2);
            }

            if(v) {
                System.out.println("Bowler insert is successful");
                return true;
            }
            else {
                System.out.println("Bowling insert unsuccessful");
                String del1 = "DELETE FROM CRICBUZZ.PLAYER WHERE PLAYER_ID = " + playerID;
                String del2 = "DELETE FROM CRICBUZZ.BATSMAN WHERE PLAYER_ID = " + playerID;
                dc.doUpate(del1);
                dc.doUpate(del2);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Failed to create statement Javacode\\Bowler :: " + e);
        }
        return false;
    }
}
