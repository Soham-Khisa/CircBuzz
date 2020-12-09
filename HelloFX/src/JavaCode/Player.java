/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PipedReader;
import java.lang.module.FindException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Player {
    private int player_ID;
    private String first_Name;
    private String last_Name;
    private String birthplace;
    private Date dob;
    private Date death = null;
    private String role;
    private int num_of_matches;
    private int team_ID;
    private int jersey_No;
    private String status;

    // Needed for tableview in player update
    private String teamName;
    private String fullname;
    private ImageView imageView;

    private PreparedStatement ps = null;
    private String insert1 = "INSERT INTO CRICBUZZ.PLAYER (FIRST_NAME, LAST_NAME, BORN, DOB, ROLE, TEAM_ID, JERSEY) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private String insert2 = "INSERT INTO CRICBUZZ.PLAYER (FIRST_NAME, LAST_NAME, BORN, DOB, ROLE, TEAM_ID, JERSEY, PROFILE_PIC) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public Player(Integer player_ID, String first_Name, String last_Name, String birthplace, Date dob, String role, int team_ID, int jersey_No) {
        this.player_ID = player_ID;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.fullname = this.first_Name + " " + this.last_Name;
        this.birthplace = birthplace;
        this.dob = dob;
        this.role = role;
        this.team_ID = team_ID;
        this.jersey_No = jersey_No;
    }

    public Player(String first_Name, String last_Name, String birthplace, Date dob, String role, int team_ID, int jersey_No) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.fullname = this.first_Name + " " + this.last_Name;
        this.birthplace = birthplace;
        this.dob = dob;
        this.role = role;
        this.team_ID = team_ID;
        this.jersey_No = jersey_No;
    }

    public Player(String first_Name, String last_Name) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
    }

    public Player(String first_Name, String last_Name, String status, String birthplace, Date dob, String role, int team_ID, int jersey_No, String teamName, ImageView imageView) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.fullname = this.first_Name + " " + this.last_Name;
        this.status = status;
        this.birthplace = birthplace;
        this.dob = dob;
        this.role = role;
        this.team_ID = team_ID;
        this.jersey_No = jersey_No;
        this.teamName = teamName;
        this.imageView = imageView;
    }

    public Player(Integer player_ID, String first_Name, String last_Name, String status, String birthplace, Date dob, Date death, String role, int team_ID, int jersey_No, String teamName, ImageView imageView) {
        this.player_ID = player_ID;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.fullname = this.first_Name + " " + this.last_Name;
        this.status = status;
        this.birthplace = birthplace;
        this.dob = dob;
        this.death = death;
        this.role = role;
        this.team_ID = team_ID;
        this.jersey_No = jersey_No;
        this.teamName = teamName;
        this.imageView = imageView;
    }

    public int getPlayer_ID() {
        return player_ID;
    }

    public Date getDeath() {
        return death;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setPlayer_ID(int player_ID) {
        this.player_ID = player_ID;
    }
    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }
    public String getLast_Name() {
        return last_Name;
    }

    public String getRole() {return role;}
    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }

    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDeath(Date death) {
        this.death = death;
    }

    public int getNum_of_matches() {
        return num_of_matches;
    }
    public void setNum_of_matches(int num_of_matches) {
        this.num_of_matches = num_of_matches;
    }

    public int getTeam_ID() {
        return team_ID;
    }
    public void setTeam_ID(int team_ID) {
        this.team_ID = team_ID;
    }

    public int getJersey_No() {
        return jersey_No;
    }
    public void setJersey_No(int jersey_No) { this.jersey_No = jersey_No; }

    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getFullname() { return fullname; }

    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getTeamName() { return teamName; }

    public ImageView getImageView() { return imageView; }
    public void setImageView(ImageView imageView) { this.imageView = imageView; }

    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Player{" +
                "player_ID = " + player_ID +
                ", first_Name = '" + first_Name + '\'' +
                ", last_Name = '" + last_Name + '\'' +
                ", born = " + birthplace +
                ", dob = " + dob +
                ", num_of_matches = " + num_of_matches +
                ", team_ID = " + team_ID +
                ", jersey_No = " + jersey_No +
                '}';
    }

    public boolean insertPlayer(FileInputStream fin) {
        try {
            DatabaseConnection dc = new DatabaseConnection();
            java.sql.Date sqlDate = new java.sql.Date(dob.getTime());
            String generatedColumns[] = { "PLAYER_ID" };
            Connection connection = dc.cricbuzzConnection();
            if (fin == null) {
                ps = connection.prepareStatement(insert1, generatedColumns);
            } else {
                ps = connection.prepareStatement(insert2, generatedColumns);
            }
            ps.setString(1, first_Name);
            ps.setString(2, last_Name);
            ps.setString(3, birthplace);
            ps.setDate(4, sqlDate);
            ps.setString(5, role);
            ps.setInt(6, team_ID);
            ps.setInt(7, jersey_No);
            if(fin != null)
                ps.setBinaryStream(8, fin, fin.available());
            System.out.println("Player input successful");
            int v = ps.executeUpdate();
            if(v > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        player_ID = generatedKeys.getInt(1);
                    }
                    else {
                        throw new SQLException("Creating teamId failed, no ID obtained.");
                    }
                }
                return true;
            }
            else    return false;

        } catch (SQLException e) {
            System.out.println("No preparedstatement or couldn't set the inputs Player\\insertPlayer :: " + e);
        } catch (IOException e) {
            System.out.println("FileInputStream is not available Player\\inserPlayer :: " + e);
        }
        return false;
    }

    public void makePlayerWithFirstLastName(int teamId) throws SQLException {
        team_ID=teamId;
        DatabaseConnection dc= new DatabaseConnection();
        String query="SELECT * FROM PLAYER\n" +
                "WHERE FIRST_NAME='"+first_Name+"' AND LAST_NAME='"+last_Name+"'AND TEAM_ID="+team_ID+" AND STATUS LIKE('Active')";
        ResultSet rs= dc.getQueryResult(query);
        if(rs.next()){
            player_ID=rs.getInt("PLAYER_ID");
            birthplace=rs.getString("BORN");
            role=rs.getString("ROLE");
            jersey_No=rs.getInt("JERSEY");
            dob= rs.getDate("DOB");
            num_of_matches= rs.getInt("NUM_OF_MATCHES");
        }
    }

    public void UpdateNumOfMatches(int id) throws SQLException {
        player_ID=id;
        DatabaseConnection dc= new DatabaseConnection();
        String query="UPDATE PLAYER\n" +
                "SET NUM_OF_MATCHES=NVL(NUM_OF_MATCHES,0)+1\n" +
                "WHERE PLAYER_ID="+player_ID;
        dc.doUpdate(query);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player temp = (Player) obj;
            if (this.first_Name.equals(temp.getFirst_Name()) && this.last_Name.equals(temp.last_Name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.first_Name.hashCode() + this.last_Name.hashCode());
    }
}
