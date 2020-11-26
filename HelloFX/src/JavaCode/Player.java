/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

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
    private String role;
    private int num_of_matches;
    private int team_ID;
    private int jersey_No;

    private PreparedStatement ps = null;
    private String insert1 = "INSERT INTO CRICBUZZ.PLAYER (FIRST_NAME, LAST_NAME, BORN, DOB, ROLE, TEAM_ID, JERSEY) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private String insert2 = "INSERT INTO CRICBUZZ.PLAYER (FIRST_NAME, LAST_NAME, BORN, DOB, ROLE, TEAM_ID, JERSEY, PROFILE_PIC) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public Player(String first_Name, String last_Name, String birthplace, Date dob, String role, int team_ID, int jersey_No) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.birthplace = birthplace;
        this.dob = dob;
        this.role = role;
        this.team_ID = team_ID;
        this.jersey_No = jersey_No;
    }

    public int getPlayer_ID() {
        return player_ID;
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

    public void setJersey_No(int jersey_No) {
        this.jersey_No = jersey_No;
    }

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

}
