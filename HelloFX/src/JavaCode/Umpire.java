/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

import java.util.Date;

public class Umpire {
    private String first_name;
    private String last_name;
    private String country;
    private Date dob;

    public Umpire(String first_name, String last_name, String country, Date dob) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.dob = dob;
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
