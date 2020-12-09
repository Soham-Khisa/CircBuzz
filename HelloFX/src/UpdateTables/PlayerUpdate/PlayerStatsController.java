package UpdateTables.PlayerUpdate;

import Database.DatabaseConnection;
import JavaCode.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.w3c.dom.ls.LSOutput;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStatsController {

    @FXML
    private Label bowlingStyle;

    @FXML
    private Label wicketsODI;

    @FXML
    private Label bowlingAvgODI;

    @FXML
    private Label bowlingStrikeODI;

    @FXML
    private Label economyODI;

    @FXML
    private Label fivewicketsODI;

    @FXML
    private Label tenwicketsODI;

    @FXML
    private Label ballsBowledODI;

    @FXML
    private Label battingStyle;

    @FXML
    private Label bestfigureMatchODI;

    @FXML
    private Label wicketsTEST;

    @FXML
    private Label bowlingAvgTEST;

    @FXML
    private Label bowlingStrikeTEST;

    @FXML
    private Label economyTEST;

    @FXML
    private Label fivewicketsTEST;

    @FXML
    private Label tenwicketsTEST;

    @FXML
    private Label ballsBowledTEST;

    @FXML
    private Label bestfigureMatchTEST;

    @FXML
    private Label wicketsT20;

    @FXML
    private Label bowlingAvgT20;

    @FXML
    private Label bowlingStrikeT20;

    @FXML
    private Label economyT20;

    @FXML
    private Label fivewicketsT20;

    @FXML
    private Label tenwicketsT20;

    @FXML
    private Label ballsBowledT20;

    @FXML
    private Label bestfigureMatchT20;

    @FXML
    private Label runsODI;

    @FXML
    private Label battingAvgODI;

    @FXML
    private Label battingStrikeODI;

    @FXML
    private Label centuriesODI;

    @FXML
    private Label halfcenturiesODI;

    @FXML
    private Label topscoreODI;

    @FXML
    private Label runsTEST;

    @FXML
    private Label battingAvgTEST;

    @FXML
    private Label battingStrikeTEST;

    @FXML
    private Label centuriesTEST;

    @FXML
    private Label halfcenturiesTEST;

    @FXML
    private Label topscoreTEST;

    @FXML
    private Label runsT20;

    @FXML
    private Label battingAvgT20;

    @FXML
    private Label battingStrikeT20;

    @FXML
    private Label centuriesT20;

    @FXML
    private Label halfcenturiesT20;

    @FXML
    private Label topscoreT20;

    @FXML
    private Label inningsbatODI;

    @FXML
    private Label inningsbatTEST;

    @FXML
    private Label inningsbatT20;

    @FXML
    private Label inningsbowlODI;

    @FXML
    private Label inningsbowlTEST;

    @FXML
    private Label inningsbowlT20;

    @FXML
    private Label matchesODI;

    @FXML
    private Label matchesTEST;

    @FXML
    private Label matchesT20;

    @FXML
    private Label catchesODI;

    @FXML
    private Label catchesTEST;

    @FXML
    private Label catchesT20;

    @FXML
    private Label stumpingODI;

    @FXML
    private Label stumpingTEST;

    @FXML
    private Label stumpingT20;

    @FXML
    private Label bestfigureIngODI;
    @FXML
    private Label bestfigureIngTEST;
    @FXML
    private Label bestfigureIngT20;

    private Player player = null;
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setAll() {
        if(player == null) {
            System.out.println("player not available in playerstatscontroller");
            return;
        }
        DatabaseConnection dc = new DatabaseConnection();
        try {
            /// showing batsman stats
            int cnt = 0;
            Integer nummatches, numing, highest, hundreds, fifties, runs;
            String matchname, batting;
            Double batavg, batstrike;
            String query = "SELECT CRICBUZZ.BATSMAN.NUM_OF_MATCHES, CRICBUZZ.MATCH_TYPE.MATCH_TITLE, " +
                    "CRICBUZZ.BATSMAN.NUM_OF_ING, CRICBUZZ.BATSMAN.BATTING_AVG, CRICBUZZ.BATSMAN.BATTING_STRIKE_RATE, " +
                    "CRICBUZZ.BATSMAN.BATTING_STYLE, CRICBUZZ.BATSMAN.TOP_SCORE, CRICBUZZ.BATSMAN.CENTURIES, " +
                    "CRICBUZZ.BATSMAN.HALF_CENTURIES, CRICBUZZ.BATSMAN.RUNS" +
                    " FROM CRICBUZZ.BATSMAN, CRICBUZZ.MATCH_TYPE WHERE CRICBUZZ.BATSMAN.PLAYER_ID = " + player.getPlayer_ID() +
                    " AND CRICBUZZ.BATSMAN.MATCH_TYPE_ID = CRICBUZZ.MATCH_TYPE.MATCH_TYPE_ID";
            ResultSet rs = dc.getQueryResult(query);
            while (rs.next()) {
                nummatches = (Integer) rs.getObject("NUM_OF_MATCHES");
                matchname = (String) rs.getObject("MATCH_TITLE");
                batting = (String) rs.getObject("BATTING_STYLE");
                numing = (Integer) rs.getObject("NUM_OF_ING");
                batavg = (Double) rs.getObject("BATTING_AVG");
                batstrike = (Double) rs.getObject("BATTING_STRIKE_RATE");
                highest = (Integer) rs.getObject("TOP_SCORE");
                hundreds = (Integer) rs.getObject("CENTURIES");
                fifties = (Integer) rs.getObject("HALF_CENTURIES");
                runs = (Integer) rs.getObject("RUNS");

                if(cnt==0 && batting!=null)
                    battingStyle.setText(batting);
                cnt++;
                if(matchname.equals("ODI")) {
                    if(nummatches != null)  matchesODI.setText(nummatches.toString());
                    if(numing != null)   inningsbatODI.setText(numing.toString());
                    if(batavg != null)  battingAvgODI.setText(batavg.toString());
                    if(batstrike != null) battingStrikeODI.setText(batstrike.toString());
                    if(hundreds != null)    centuriesODI.setText(hundreds.toString());
                    if(fifties != null) halfcenturiesODI.setText(fifties.toString());
                    if(highest != null) topscoreODI.setText(highest.toString());
                    if(runs != null)    runsODI.setText(runs.toString());
                }
                else if(matchname.equals("TEST")) {
                    if(nummatches != null)  matchesTEST.setText(nummatches.toString());
                    if(numing != null)  inningsbatTEST.setText(numing.toString());
                    if(batavg != null)  battingAvgTEST.setText(batavg.toString());
                    if(batstrike != null)   battingStrikeTEST.setText(batstrike.toString());
                    if(hundreds != null)    centuriesTEST.setText(hundreds.toString());
                    if(fifties != null) halfcenturiesTEST.setText(fifties.toString());
                    if(highest != null) topscoreTEST.setText(highest.toString());
                    if(runs != null)    runsTEST.setText(runs.toString());
                }
                else if(matchname.equals("T20")) {
                    if(nummatches != null)  matchesT20.setText(nummatches.toString());
                    if(numing != null)  inningsbatT20.setText(numing.toString());
                    if(batavg != null)  battingAvgT20.setText(batavg.toString());
                    if(batstrike != null)   battingStrikeT20.setText(batstrike.toString());
                    if(hundreds != null)    centuriesT20.setText(hundreds.toString());
                    if(fifties != null) halfcenturiesT20.setText(fifties.toString());
                    if(highest != null) topscoreT20.setText(highest.toString());
                    if(runs != null)    runsT20.setText(runs.toString());
                }
            }

            ///showing wicketkeeper stats
            Integer numcatch, numstump;
            query = "SELECT CRICBUZZ.WICKET_KEEPER.STUMPINGS, CRICBUZZ.MATCH_TYPE.MATCH_TITLE, CRICBUZZ.WICKET_KEEPER.CATCHES " +
                    "FROM CRICBUZZ.WICKET_KEEPER, CRICBUZZ.MATCH_TYPE WHERE CRICBUZZ.WICKET_KEEPER.PLAYER_ID = " + player.getPlayer_ID() +
                    " AND CRICBUZZ.MATCH_TYPE.MATCH_TYPE_ID = CRICBUZZ.WICKET_KEEPER.MATCH_TYPE_ID";
            rs = dc.getQueryResult(query);
            while(rs.next()) {
                numcatch = (Integer) rs.getObject("CATCHES");
                numstump = (Integer) rs.getObject("STUMPINGS");
                matchname = (String) rs.getObject("MATCH_TITLE");

                if(matchname.equals("ODI")) {
                    if(numcatch != null)    catchesODI.setText(numcatch.toString());
                    if(numstump != null)    stumpingODI.setText(numstump.toString());
                }
                else if(matchname.equals("TEST")) {
                    if(numcatch != null)    catchesTEST.setText(numcatch.toString());
                    if(numstump != null)    stumpingTEST.setText(numstump.toString());
                }
                else if(matchname.equals("T20")) {
                    if(numcatch != null)    catchesT20.setText(numcatch.toString());
                    if(numstump != null)    stumpingT20.setText(numstump.toString());
                }
            }

            ///showing bowler stats
            cnt = 0;
            String bestperformMatch, bestperformIng, bowling;
            Integer wicket, balls, fivewicket, tenwicket;
            Double bowlingstrike, bowlingEco, bowlingAvg;
            query = "SELECT CRICBUZZ.BOWLER.WICKETS, CRICBUZZ.BOWLER.NUM_OF_ING, CRICBUZZ.BOWLER.BALLS_BOWLED, CRICBUZZ.BOWLER.FIVE_WICKET_HAUL, " +
                    "CRICBUZZ.BOWLER.TEN_WICKET_HAUL, CRICBUZZ.BOWLER.BEST_BOWLING_MATCH, CRICBUZZ.BOWLER.BEST_BOWLING_ING, " +
                    "CRICBUZZ.BOWLER.BOWLING_STRIKE_RATE, CRICBUZZ.BOWLER.BOWLING_ECONOMY_RATE, CRICBUZZ.BOWLER.BOWLING_AVG, " +
                    "CRICBUZZ.BOWLER.BOWLING_STYLE, CRICBUZZ.MATCH_TYPE.MATCH_TITLE " +
                    "FROM CRICBUZZ.BOWLER, CRICBUZZ.MATCH_TYPE WHERE PLAYER_ID = " + player.getPlayer_ID() + " " +
                    "AND CRICBUZZ.MATCH_TYPE.MATCH_TYPE_ID = CRICBUZZ.BOWLER.MATCH_TYPE_ID";

            rs = dc.getQueryResult(query);
            while (rs.next()) {
                numing = (Integer) rs.getObject("NUM_OF_ING");
                bowling = (String) rs.getObject("BOWLING_STYLE");
                wicket = (Integer) rs.getObject("WICKETS");
                matchname = (String)  rs.getObject("MATCH_TITLE");
                balls =(Integer) rs.getObject("BALLS_BOWLED");
                fivewicket = (Integer) rs.getObject("FIVE_WICKET_HAUL");
                tenwicket = (Integer) rs.getObject("TEN_WICKET_HAUL");
                bowlingstrike = (Double) rs.getObject("BOWLING_STRIKE_RATE");
                bowlingAvg = (Double) rs.getObject("BOWLING_AVG");
                bowlingEco = (Double) rs.getObject("BOWLING_ECONOMY_RATE");
                bestperformMatch = (String) rs.getObject("BEST_BOWLING_MATCH");
                bestperformIng = (String) rs.getObject("BEST_BOWLING_ING");

                if(cnt == 0 && bowling!=null)
                    bowlingStyle.setText(bowling);
                cnt++;
                if(matchname.equals("ODI")) {
                    if(wicket != null)    wicketsODI.setText(wicket.toString());
                    if(bowlingAvg != null)    bowlingAvgODI.setText(bowlingAvg.toString());
                    if(bowlingstrike != null)   bowlingStrikeODI.setText(bowlingstrike.toString());
                    if(bowlingEco != null)    economyODI.setText(bowlingEco.toString());
                    if(fivewicket != null)    fivewicketsODI.setText(fivewicket.toString());
                    if(tenwicket != null)   tenwicketsODI.setText(tenwicket.toString());
                    if(balls != null)    ballsBowledODI.setText(balls.toString());
                    if(numing != null)    inningsbowlODI.setText(numing.toString());
                    if(bestperformIng != null)    bestfigureIngODI.setText(bestperformIng);
                    if(bestperformMatch != null)    bestfigureMatchODI.setText(bestperformMatch);
                }
                else if(matchname.equals("TEST")) {
                    if(wicket != null)  wicketsTEST.setText(wicket.toString());
                    if(bowlingAvg != null)  bowlingAvgTEST.setText(bowlingAvg.toString());
                    if(bowlingstrike != null)   bowlingStrikeTEST.setText(bowlingstrike.toString());
                    if(bowlingEco != null)  economyTEST.setText(bowlingEco.toString());
                    if(fivewicket != null)  fivewicketsTEST.setText(fivewicket.toString());
                    if(tenwicket != null)   tenwicketsTEST.setText(tenwicket.toString());
                    if(balls != null)   ballsBowledTEST.setText(balls.toString());
                    if(numing != null)  inningsbowlTEST.setText(numing.toString());
                    if(bestperformIng != null)  bestfigureIngTEST.setText(bestperformIng);
                    if(bestperformMatch != null)    bestfigureMatchTEST.setText(bestperformMatch);
                }
                else if (matchname.equals("T20")) {
                    if(wicket != null)  wicketsT20.setText(wicket.toString());
                    if(bowlingAvg != null)  bowlingAvgT20.setText(bowlingAvg.toString());
                    if(bowlingstrike != null)   bowlingStrikeT20.setText(bowlingstrike.toString());
                    if(bowlingEco != null)  economyT20.setText(bowlingEco.toString());
                    if(fivewicket != null)  fivewicketsT20.setText(fivewicket.toString());
                    if(tenwicket != null)   tenwicketsT20.setText(tenwicket.toString());
                    if(balls != null)   ballsBowledT20.setText(balls.toString());
                    if(numing != null)    inningsbowlT20.setText(numing.toString());
                    if(bestperformIng != null)  bestfigureIngT20.setText(bestperformIng);
                    if(bestperformMatch != null)    bestfigureMatchT20.setText(bestperformMatch);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to load resultset playerstatscontroller :: " + e);
        }
    }
}
