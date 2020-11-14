package Database;

import JavaCode.Match_type;
import JavaCode.Stadium;
import JavaCode.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection{
   private Statement statement;
   private Connection connection;


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

   public boolean doUpate(String insert_query) throws SQLException {
       int v = statement.executeUpdate(insert_query);
       if(v>0)  return true;
       else return false;
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

    public Connection cricbuzzConnection() {
       return connection;
   }

   public void closeConnection() throws SQLException {
       if(!connection.isClosed())
           connection.close();
   }
}





