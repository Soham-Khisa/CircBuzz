/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class Team {
    private int team_ID;
    private String team_Name;
    private Date established_date;
    private int matches;
    private int won;
    private int lost;
    private int draw;
    private ImageView imageholder;
    private String head_coach;
    private String board_president;
    private String team_sf;
    private PreparedStatement ps;

    private String insert1 = "INSERT INTO CRICBUZZ.TEAM (TEAM_NAME, TEAM_SF, ESTABLISH_DATE, HEAD_COACH, BOARD_PRESIDENT,  TEAM_LOGO) "
            + "VALUES(?, ?, ?, ?, ?, ?)";
    private String insert2 = "INSERT INTO CRICBUZZ.TEAM (TEAM_NAME, TEAM_SF, ESTABLISH_DATE, HEAD_COACH, BOARD_PRESIDENT) "
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

    public void setImage(ImageView imageholder) {
        this.imageholder = imageholder;
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

    public ImageView getImageholder() {
        return imageholder;
    }

    public String getHead_coach() {
        return head_coach;
    }

    public String getBoard_president() {
        return board_president;
    }

    public Team(String team_Name, String team_sf, Date established_date, String head_coach, String board_president) {
        this.team_Name = team_Name;
        this.team_sf = team_sf;
        this.established_date = established_date;
        this.head_coach = head_coach;
        this.board_president = board_president;
    }
    public Team() {

    }

    public Team(Integer team_ID, String team_Name, String team_sf, Date established_date, String head_coach, String board_president, ImageView imageholder) {
        this.team_ID = team_ID;
        this.team_Name = team_Name;
        this.team_sf = team_sf;
        this.established_date = established_date;
        this.head_coach = head_coach;
        this.board_president = board_president;
        this.imageholder = imageholder;
    }

    public boolean insertTeam(FileInputStream fin) {
        try {
            DatabaseConnection dc = new DatabaseConnection();

            //java.util.date -> java.sql.date
            java.sql.Date sqlDate = new java.sql.Date(established_date.getTime());
            Connection connection = dc.cricbuzzConnection();
            String generatedColumns[] = { "TEAM_ID" };
            if (fin == null) {
                System.out.println("Without Team Logo");
                ps = connection.prepareStatement(insert2, generatedColumns);
            } else {
                System.out.println("With Team Logo");
                ps = connection.prepareStatement(insert1, generatedColumns);
            }
            ps.setString(1, team_Name);
            ps.setString(2, team_sf);
            ps.setDate(3, sqlDate);
            ps.setString(4, head_coach);
            ps.setString(5, board_president);
            if(fin!=null)
                ps.setBinaryStream(6, fin, fin.available());
            System.out.println("Team input successful");
            int v = ps.executeUpdate();

            if(v > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        team_ID = generatedKeys.getInt(1);
                    }
                    else {
                        throw new SQLException("Creating teamId failed, no ID obtained.");
                    }
                }
                return true;
            }
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
