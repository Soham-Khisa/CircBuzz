/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import Database.DatabaseConnection;

import java.sql.SQLException;

public class Wicket_Keeper {
    private int playerID;
    private int stampings;
    private int catches;

    public Wicket_Keeper(int playerID, int stampings, int catches) {
        this.playerID = playerID;
        this.stampings = stampings;
        this.catches = catches;
    }
    public Wicket_Keeper(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getStampings() {
        return stampings;
    }

    public void setStampings(int stampings) {
        this.stampings = stampings;
    }

    public int getCatches() {
        return catches;
    }

    public void setCatches(int catches) {
        this.catches = catches;
    }

    @Override
    public String toString() {
        return "Wicket_Keeper{" +
                "playerID=" + playerID +
                ", stampings=" + stampings +
                ", catches=" + catches +
                '}';
    }

    public boolean insertWicketKeeper() {
        DatabaseConnection dc = new DatabaseConnection();
        String insert = "INSERT INTO CRICBUZZ.WICKET_KEEPER (PLAYER_ID) " +
                "VALUES (" + playerID + ")";
        boolean v = dc.doUpdate(insert);
        if(v) {
            System.out.println("Wicket-keeper insert is successful");
            return true;
        }
        else {
            String del1 = "DELETE FROM CRICBUZZ.PLAYER WHERE PLAYER_ID = " + playerID;
            String del2 = "DELETE FROM CRICBUZZ.BATSMAN WHERE PLAYER_ID = " + playerID;
            String del3 = "DELETE FROM CRICBUZZ.BOWLER WHERE PLAYER_ID = " + playerID;
            dc.doUpdate(del1);
            dc.doUpdate(del2);
            dc.doUpdate(del3);
            return false;
        }
    }
}
