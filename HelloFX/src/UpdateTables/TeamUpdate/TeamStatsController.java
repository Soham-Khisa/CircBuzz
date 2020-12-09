package UpdateTables.TeamUpdate;

import Database.DatabaseConnection;
import JavaCode.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.print.DocFlavor;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamStatsController {
    @FXML
    private Label matchesODI;

    @FXML
    private Label matchesTEST;

    @FXML
    private Label matchesT20;

    @FXML
    private Label wonODI;

    @FXML
    private Label wonTEST;

    @FXML
    private Label wonT20;

    @FXML
    private Label lostODI;

    @FXML
    private Label lostTest;

    @FXML
    private Label lostT20;

    @FXML
    private Label drawODI;

    @FXML
    private Label drawTEST;

    @FXML
    private Label drawT20;

    private Team team=null;

    public void setPlayer(Team team) {
        this.team = team;
    }

    public void setAll() {
        if(team == null) {
            System.out.println("team not available in teamstatscontroller");
            return;
        }
        DatabaseConnection dc = new DatabaseConnection();
        try {
            String query = "SELECT CRICBUZZ.MATCH_TYPE.MATCH_TITLE, SUM(CRICBUZZ.MATCH.WINNER) AS WIN " +
                    "FROM CRICBUZZ.MATCH, CRICBUZZ.MATCH_TYPE WHERE CRICBUZZ.MATCH.WINNER = " + team.getTeam_ID() + " " +
                    "GROUP BY CRICBUZZ.MATCH_TYPE.MATCH_TITLE";
            ResultSet rs = dc.getQueryResult(query);
            while (rs.next()) {
                Integer ans = (Integer) rs.getObject("WIN");
                String matchname = (String) rs.getObject("MATCH_TITLE");

                if (matchname.equals("ODI")) {
                    if(ans!=null)    wonODI.setText(ans.toString());
                } else if (matchname.equals("TEST")) {
                    if(ans!=null)    wonTEST.setText(ans.toString());
                }
                else if(matchname.equals("T20")) {
                    if(ans!=null)    wonT20.setText(ans.toString());
                }
            }

            query = "SELECT CRICBUZZ.MATCH_TYPE.MATCH_TITLE, SUM(CRICBUZZ.MATCH.MATCH_ID) AS PLAY " +
                    "FROM CRICBUZZ.MATCH, CRICBUZZ.MATCH_TYPE WHERE CRICBUZZ.MATCH.HOST_TEAM = " + team.getTeam_ID() + " " +
                    "OR CRICBUZZ.MATCH.VISITING_TEAM = " + team.getTeam_ID() + " " +
                    "GROUP BY CRICBUZZ.MATCH_TYPE.MATCH_TITLE";
            rs = dc.getQueryResult(query);
            while (rs.next()) {
                Integer played = (Integer) rs.getObject("PLAY");
                String matchname = (String) rs.getObject("MATCH_TITLE");

                if (matchname.equals("ODI")) {
                    if(played!=null)    matchesODI.setText(played.toString());
                } else if (matchname.equals("TEST")) {
                    if(played!=null)    matchesTEST.setText(played.toString());
                } else if(matchname.equals("T20")) {
                    if(played!=null)    matchesT20.setText(played.toString());
                }
            }

            query = "SELECT CRICBUZZ.MATCH_TYPE.MATCH_TITLE, SUM(CRICBUZZ.MATCH.MATCH_ID) AS PLAY " +
                    "FROM CRICBUZZ.MATCH, CRICBUZZ.MATCH_TYPE WHERE (CRICBUZZ.MATCH.HOST_TEAM = " + team.getTeam_ID() + " " +
                    "OR CRICBUZZ.MATCH.VISITING_TEAM = " + team.getTeam_ID() + ") AND " +
                    "CRICBUZZ.MATCH.WINNER IS NULL " +
                    "GROUP BY CRICBUZZ.MATCH_TYPE.MATCH_TITLE";
            rs = dc.getQueryResult(query);
            while (rs.next()) {
                Integer ans = (Integer) rs.getObject("PLAY");
                String matchname = (String) rs.getObject("MATCH_TITLE");

                if (matchname.equals("ODI")) {
                    if(ans!=null)    drawODI.setText(ans.toString());
                } else if (matchname.equals("TEST")) {
                    if(ans!=null)   drawTEST.setText(ans.toString());
                }
                else if(matchname.equals("T20")) {
                    if(ans!=null)    drawT20.setText(ans.toString());
                }
            }

            boolean lost1 = false, lost2 = false, lost3 = false;
            ///lost = played - (won + drawn)
            lost1 = isNumeric(matchesODI.getText());
            lost2 = isNumeric(wonODI.getText());
            lost3 = isNumeric(drawODI.getText());

            if(lost1 == true) {
                Integer result = 0;
                if(lost2==true && lost3==true) {
                    Integer match = Integer.parseInt(matchesODI.getText());
                    Integer win = Integer.parseInt(wonODI.getText());
                    Integer draw = Integer.parseInt(drawODI.getText());
                    result = match - (win + draw);
                    lostODI.setText(result.toString());
                }
                else if(lost2==true && lost3==false) {
                    Integer match = Integer.parseInt(matchesODI.getText());
                    Integer win = Integer.parseInt(wonODI.getText());
                    result = match - win;
                    lostODI.setText(result.toString());
                }
                else if(lost2==false && lost3==true) {
                    Integer match = Integer.parseInt(matchesODI.getText());
                    Integer draw = Integer.parseInt(drawODI.getText());
                    result = match - draw;
                    lostODI.setText(result.toString());
                }
            }

            lost1 = isNumeric(matchesTEST.getText());
            lost2 = isNumeric(wonTEST.getText());
            lost3 = isNumeric(drawTEST.getText());

            if(lost1 == true) {
                Integer result = 0;
                if(lost2==true && lost3==true) {
                    Integer match = Integer.parseInt(matchesTEST.getText());
                    Integer win = Integer.parseInt(wonTEST.getText());
                    Integer draw = Integer.parseInt(drawTEST.getText());
                    result = match - (win + draw);
                    lostTest.setText(result.toString());
                }
                else if(lost2==true && lost3==false) {
                    Integer match = Integer.parseInt(matchesTEST.getText());
                    Integer win = Integer.parseInt(wonTEST.getText());
                    result = match - win;
                    lostTest.setText(result.toString());
                }
                else if(lost2==false && lost3==true) {
                    Integer match = Integer.parseInt(matchesTEST.getText());
                    Integer draw = Integer.parseInt(drawTEST.getText());
                    result = match - draw;
                    lostTest.setText(result.toString());
                }
            }

            lost1 = isNumeric(matchesT20.getText());
            lost2 = isNumeric(wonT20.getText());
            lost3 = isNumeric(drawT20.getText());

            if(lost1 == true) {
                Integer result = 0;
                if(lost2==true && lost3==true) {
                    Integer match = Integer.parseInt(matchesT20.getText());
                    Integer win = Integer.parseInt(wonT20.getText());
                    Integer draw = Integer.parseInt(drawT20.getText());
                    result = match - (win + draw);
                    lostT20.setText(result.toString());
                }
                else if(lost2==true && lost3==false) {
                    Integer match = Integer.parseInt(matchesT20.getText());
                    Integer win = Integer.parseInt(wonT20.getText());
                    result = match - win;
                    lostT20.setText(result.toString());
                }
                else if(lost2==false && lost3==true) {
                    Integer match = Integer.parseInt(matchesT20.getText());
                    Integer draw = Integer.parseInt(drawT20.getText());
                    result = match - draw;
                    lostT20.setText(result.toString());
                }
            }
        }
        catch (SQLException e) {
            System.out.println("failed to retrieve result teamstatscontroller :: " + e);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
