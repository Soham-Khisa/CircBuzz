/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;
import javafx.animation.ScaleTransition;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Team {
    private int team_ID;
    private String team_Name;
    private Date established_date;
    private int matches;
    private int won;
    private int lost;
    private int draw;
    private String head_coach;
    private String board_president;
    private PreparedStatement ps;

    private String insert1 = "INSERT INTO CRICBUZZ.TEAM (TEAM_ID, TEAM_NAME, ESTABLISH_DATE, TEAM_LOGO, HEAD_COACH, BOARD_PRESIDENT) "
            + "VALUES(?, ?, ?, ?, ?, ?)";
    private String insert2 = "INSERT INTO CRICBUZZ.TEAM (TEAM_ID, TEAM_NAME, ESTABLISH_DATE, HEAD_COACH, BOARD_PRESIDENT) "
            + "VALUES(?, ?, ?, ?, ?)";

    @Override
    public String toString() {
        return "Team{" +
                "team_ID=" + team_ID +
                ", team_Name='" + team_Name + '\'' +
                ", established_date=" + established_date +
                ", matches=" + matches +
                ", won=" + won +
                ", lost=" + lost +
                ", draw=" + draw +
                ", head_coach='" + head_coach + '\'' +
                ", board_president='" + board_president + '\'' +
                '}';
    }

    public void setTeam_ID(int team_ID) {
        this.team_ID = team_ID;
    }

    public void setTeam_Name(String team_Name) {
        this.team_Name = team_Name;
    }

    public void setEstablished_date(Date established_date) {
        this.established_date = established_date;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public void setHead_coach(String head_coach) {
        this.head_coach = head_coach;
    }

    public void setBoard_president(String board_president) {
        this.board_president = board_president;
    }

    public int getTeam_ID() {
        return team_ID;
    }

    public String getTeam_Name() {
        return team_Name;
    }

    public Date getEstablished_date() {
        return established_date;
    }

    public int getMatches() {
        return matches;
    }

    public int getWon() {
        return won;
    }

    public int getLost() {
        return lost;
    }

    public int getDraw() {
        return draw;
    }

    public String getHead_coach() {
        return head_coach;
    }

    public String getBoard_president() {
        return board_president;
    }

    public Team(int team_ID, String team_Name, Date established_date, String head_coach, String board_president) {
        this.team_ID = team_ID;
        this.team_Name = team_Name;
        this.established_date = established_date;
        this.head_coach = head_coach;
        this.board_president = board_president;
    }
    public Team() {

    }

    public boolean insertTeam(FileInputStream fin) {
        try {
            DatabaseConnection dc = new DatabaseConnection();

            //java.util.date -> java.sql.date
            java.sql.Date sqlDate = new java.sql.Date(established_date.getTime());
            Connection connection = dc.cricbuzzConnection();
            if (fin == null) {
                System.out.println("Without Team Logo");
                ps = connection.prepareStatement(insert2);

                ps.setInt(1, team_ID);
                ps.setString(2, team_Name);
                ps.setDate(3, sqlDate);
                ps.setString(4, head_coach);
                ps.setString(5, board_president);
                System.out.println("Team input successful");
            } else {
                System.out.println("With Team Logo");
                ps = connection.prepareStatement(insert1);

                ps.setInt(1, team_ID);
                ps.setString(2, team_Name);
                ps.setDate(3, sqlDate);
                ps.setBinaryStream(4, fin, fin.available());
                ps.setString(5, head_coach);
                ps.setString(6, board_president);
                System.out.println("Team input successful");
            }
            int v = ps.executeUpdate();
            if(v > 0)
                return true;
            else
                return false;
        }
        catch (SQLException e) {
            System.out.println("Result is not returned, Team.insertteam :: " + e);
        }
        catch (IOException e) {
            System.out.println("Image is not loaded Team.insetteam :: " + e);
        }
        return  false;
    }
}
