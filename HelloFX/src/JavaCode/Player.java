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
    private String insert1 = "INSERT INTO CRICBUZZ.PLAYER (PLAYER_ID, FIRST_NAME, LAST_NAME, BORN, DOB, ROLE, TEAM_ID, JERSEY) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private String insert2 = "INSERT INTO CRICBUZZ.PLAYER (PLAYER_ID, FIRST_NAME, LAST_NAME, BORN, DOB, ROLE, PROFILE_PIC, TEAM_ID, JERSEY) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public Player(int player_ID, String first_Name, String last_Name, String birthplace, Date dob, String role, int team_ID, int jersey_No) {
        this.player_ID = player_ID;
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
            Connection connection = dc.cricbuzzConnection();
            if (fin == null) {
                ps = connection.prepareStatement(insert1);

                ps.setInt(1, player_ID);
                ps.setString(2, first_Name);
                ps.setString(3, last_Name);
                ps.setString(4, birthplace);
                ps.setDate(5, sqlDate);
                ps.setString(6, role);
                ps.setInt(7, team_ID);
                ps.setInt(8, jersey_No);
                System.out.println("Player input successful");

            } else {
                ps = connection.prepareStatement(insert2);

                ps.setInt(1, player_ID);
                ps.setString(2, first_Name);
                ps.setString(3, last_Name);
                ps.setString(4, birthplace);
                ps.setDate(5, sqlDate);
                ps.setString(6, role);
                ps.setBinaryStream(7, fin, fin.available());
                ps.setInt(8, team_ID);
                ps.setInt(9, jersey_No);
                System.out.println("Player input successful");
            }
            int v = ps.executeUpdate();
            System.out.println("player successful");
            if(v > 0)   return  true;
            else    return false;

        } catch (SQLException e) {
            System.out.println("No preparedstatement or couldn't set the inputs Player\\insertPlayer :: " + e);
        } catch (IOException e) {
            System.out.println("FileInputStream is not available Player\\inserPlayer :: " + e);
        }
        return false;
    }

}
