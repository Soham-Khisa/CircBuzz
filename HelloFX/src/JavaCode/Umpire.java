/*
 * Author Soham Khisa
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class Umpire {
    private Integer umpire_ID;
    private String first_name;
    private String last_name;
    private String name;
    private String country;
    private String status = "Active";
    private Date dob;
    private Date death=null;
    private Integer age = 0;
    private Integer ID;

    public Umpire(String first_name, String last_name, String country, Date dob) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.name = first_name + " " + last_name;
        this.country = country;
        this.dob = dob;

        ///Finding the age without using PL/SQL
        /*try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String datetext = formatter.format(this.dob);
            Date formatedDate = formatter.parse(datetext);
            LocalDate date = formatedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if(this.death==null) {
                LocalDate today = LocalDate.now();
                Period period = Period.between(date, today);
                this.age = period.getYears();
            }
            else {
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                datetext = formatter.format(this.death);
                formatedDate = formatter.parse(datetext);
                LocalDate dead = formatedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Period period = Period.between(date, dead);
                this.age = period.getYears();
            }
        }
        catch (ParseException e) {
            System.out.println("constructor of umpire " + e);;
        }*/

        //Finding the age using PL/SQL
        DatabaseConnection dc = new DatabaseConnection();
        Connection conn = dc.cricbuzzConnection();
        java.sql.Date birthdate = new java.sql.Date(this.dob.getTime());
        LocalDate today = LocalDate.now();
        java.sql.Date enddate = java.sql.Date.valueOf(today);

        if(this.death!=null)
            enddate = new java.sql.Date(this.death.getTime());
        try {
            CallableStatement cstmt = conn.prepareCall("{? = call CRICBUZZ.AGECALCULATOR(?, ?)}");
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setDate(2, enddate);
            cstmt.setDate(3, birthdate);
            cstmt.executeUpdate();
            this.age = cstmt.getInt(1);
        } catch (SQLException e) {
            System.out.println("Failed to calculate date with PL_SQL function :: " + e);
        }

    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setDeath(Date death) {
        this.death = death;
    }

    public Umpire(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public void makeUmpireWithFirstLastName() throws SQLException {
        DatabaseConnection dc= new DatabaseConnection();
        String query="SELECT * FROM UMPIRE\n" +
                "WHERE FIRST_NAME='"+first_name+"' AND LAST_NAME='"+last_name+"'";
        ResultSet rs =dc.getQueryResult(query);
        if (rs.next()){
            umpire_ID = rs.getInt("UMPIRE_ID");
            country=rs.getString("COUNTRY");
            dob=rs.getDate("DOB");
        }
    }

    public Umpire(Integer ID, String first_name, String last_name, String country, String status, Date dob, Date death) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.name = first_name + " " + last_name;
        this.country = country;
        this.status = status;
        this.dob = dob;
        this.ID = ID;
        this.death = death;

        //Finding age using PL/SQL
        DatabaseConnection dc = new DatabaseConnection();
        Connection conn = dc.cricbuzzConnection();
        java.sql.Date birthdate = new java.sql.Date(this.dob.getTime());
        LocalDate today = LocalDate.now();
        java.sql.Date enddate = java.sql.Date.valueOf(today);

        if(this.death!=null)
            enddate = new java.sql.Date(this.death.getTime());
        try {
            CallableStatement cstmt = conn.prepareCall("{? = call CRICBUZZ.AGECALCULATOR(?, ?)}");
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setDate(2, enddate);
            cstmt.setDate(3, birthdate);
            cstmt.executeUpdate();
            this.age = cstmt.getInt(1);
        } catch (SQLException e) {
            System.out.println("Failed to calculate date with PL_SQL function :: " + e);
        }
    }

    public Integer getID() {
        return ID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getCountry() {
        return country;
    }

    public Date getDob() {
        return dob;
    }

    public String getStatus() { return status; }

    public String getName() { return name; }

    public Integer getAge() { return age; }

    public int getUmpire_ID(){
        return umpire_ID;
    }

    public Date getDeath() {
        return death;
    }

    @Override
    public String toString() {
        return "Umpire{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", country='" + country + '\'' +
                ", dob=" + dob +
                '}';
    }
}
