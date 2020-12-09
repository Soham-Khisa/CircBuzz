package Database;

import JavaCode.Match_type;
import JavaCode.Player;
import JavaCode.Stadium;
import JavaCode.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection{
    private Statement statement;
    private Connection connection;
    //private PreparedStatement preparedStatement;

    public DatabaseConnection() {
        try {
            connection = OracleConnectionPool.getDataSource().getConnection();
            statement = connection.createStatement();
        }
        catch (SQLException e) {
            System.out.println("Failed to establish connection :: " + e);
        }
    }


    public ResultSet getQueryResult (String query) {
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(query);
        }
        catch (SQLException e) {
            System.out.println("Failed to Execute Query in getQueryResult :: " + e);
        }
        return rs;
    }

    public boolean doUpdate(String insert_query) {
        int v = 0;
        try {
            v = statement.executeUpdate(insert_query);
        } catch (SQLException e) {
            System.out.println("Failed to execute query doUpdate\\DatabaseConnection :: " + e);
        }
        if(v>0)  return true;
        else return false;
    }

    public boolean doUpdate(String query, String Columns[]) {
        int v = 0;
        try {
            v = statement.executeUpdate(query, Columns);
        } catch (SQLException e) {
            System.out.println("Failed to execute query doUpdate(query, col)\\DatabaseConnection :: " + e);
        }
        if(v>0)  return true;
        else return false;
    }

    public ResultSet generatedKeys() {
        try {
            return statement.getGeneratedKeys();
        } catch (SQLException e) {
            System.out.println("Failed to return generated keys generatedKeys()\\DatabaseConnection :: " + e);
        }
        return null;
    }

    public void insert(String insert_query) throws SQLException {
         statement.executeUpdate(insert_query);
    }

    public boolean logInCheck(int userID, String pass) {
        try {
            String query = "SELECT ADMIN_ID, PASSWORD" +
                    " FROM Cricbuzz_Admin.ADMIN " +
                    "WHERE ADMIN_ID=" + userID+ " and PASSWORD =" + "'" + pass + "'";
            ResultSet rs = statement.executeQuery(query);
            return rs.isBeforeFirst();
        }
        catch (SQLException e) {
            System.out.println("Log in failed :: " + e);
        }
        return false;
    }



    public List<Team> getAllTeams() throws SQLException{
        String query="SELECT DISTINCT TEAM_NAME FROM TEAM";
        statement=connection.createStatement();
        ResultSet rs= statement.executeQuery(query);
        List<Team> teamList= new ArrayList<Team>();
        while(rs.next()){
            String team_name=rs.getString("TEAM_NAME");
            Team team=new Team();
            team.setTeam_Name(team_name);
            teamList.add(team);
        }
        return teamList;
    }

    public List<Stadium> getStadium() throws SQLException {
        String query="SELECT DISTINCT STADIUM_NAME FROM STADIUM";
        statement=connection.createStatement();
        ResultSet rs=statement.executeQuery(query);
        List<Stadium> stadiumList= new ArrayList<Stadium>();
        while(rs.next()){
            String stadium_name= rs.getString("STADIUM_NAME");
            Stadium stadium=new Stadium();
            stadium.setStadium_name(stadium_name);
            stadiumList.add(stadium);
        }
        return stadiumList;
    }

    public List<Match_type> getMatchType() throws SQLException {
        String query= "SELECT DISTINCT MATCH_TITLE FROM MATCH_TYPE";
        statement=connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        List<Match_type> match_typeList= new ArrayList<>();
        while(rs.next()){
            Match_type match_type= new Match_type();
            String match_type_name= rs.getString("MATCH_TITLE");
            match_type.setMatch_title(match_type_name);
            match_typeList.add(match_type);
        }
        return match_typeList;

    }

    public List<String> getUmpiresName(){
        String query="SELECT (FIRST_NAME||' '||LAST_NAME) AS FULL_NAME \n" +
                "FROM UMPIRE WHERE STATUS = 'Active'";
        try {
            ResultSet rs=statement.executeQuery(query);
            List<String> umpiresNameList= new ArrayList<>();
            while(rs.next()){
                String umpireName= rs.getString("FULL_NAME");
                umpiresNameList.add(umpireName);
            }
            return umpiresNameList;
        } catch (SQLException e) {
            System.out.println("getUmpiresName query can't be executed::"+e);
        }
        return null;

    }

    public ArrayList<Player> activePlayerOfTeam(int team_id) throws SQLException {
        String query="SELECT * FROM CRICBUZZ.PLAYER\n" +
                "WHERE TEAM_ID="+team_id+" AND STATUS = 'Active'";
        ResultSet rs=statement.executeQuery(query);
        ArrayList<Player> playerArrayList= new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt("PLAYER_ID");
            String first_name= rs.getString("FIRST_NAME");
            String last_name=rs.getString("LAST_NAME");
            String birth_place=rs.getString("BORN");
            String role=rs.getString("ROLE");
            int jersey=rs.getInt("JERSEY");
            Date dob= rs.getDate("DOB");
            int numMatch= rs.getInt("NUM_OF_MATCHES");
            Player player=new Player(id, first_name, last_name, birth_place, dob, role, team_id, jersey);
            player.setNum_of_matches(numMatch);
            playerArrayList.add(player);
        }
        return playerArrayList;
    }

    public Connection cricbuzzConnection() {
       return connection;
   }

    public void closeConnection() throws SQLException {
        if(!connection.isClosed())
            connection.close();
    }
}





