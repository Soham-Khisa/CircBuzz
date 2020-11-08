/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

public class Tour {
    private String host_team;
    private String visiting_team;
    private int t20;
    private int odi;
    private int test;

    public Tour(String host_team, String visiting_team, int t20, int odi, int test) {
        this.host_team = host_team;
        this.visiting_team = visiting_team;
        this.t20 = t20;
        this.odi = odi;
        this.test = test;
    }

    public String getHost_team() {
        return host_team;
    }

    public void setHost_team(String host_team) {
        this.host_team = host_team;
    }

    public String getVisiting_team() {
        return visiting_team;
    }

    public void setVisiting_team(String visiting_team) {
        this.visiting_team = visiting_team;
    }

    public int getT20() {
        return t20;
    }

    public void setT20(int t20) {
        this.t20 = t20;
    }

    public int getOdi() {
        return odi;
    }

    public void setOdi(int odi) {
        this.odi = odi;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "host_team='" + host_team + '\'' +
                ", visiting_team='" + visiting_team + '\'' +
                ", t20=" + t20 +
                ", odi=" + odi +
                ", test=" + test +
                '}';
    }
}
