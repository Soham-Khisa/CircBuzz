/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

public class Match_type {
    private int match_type_ID;
    private String match_title;

    public Match_type(int match_type_ID, String match_title) {
        this.match_type_ID = match_type_ID;
        this.match_title = match_title;
    }

    public int getMatch_type_ID() {
        return match_type_ID;
    }

    public void setMatch_type_ID(int match_type_ID) {
        this.match_type_ID = match_type_ID;
    }

    public String getMatch_title() {
        return match_title;
    }

    public void setMatch_title(String match_title) {
        this.match_title = match_title;
    }

    @Override
    public String toString() {
        return "Match_type{" +
                "match_type_ID=" + match_type_ID +
                ", match_title='" + match_title + '\'' +
                '}';
    }
}
