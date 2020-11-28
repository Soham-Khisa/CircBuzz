/*
 * Author Soham Khisa
 * Author MD. Sakibur Reza
 */

package JavaCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class Umpire {
    private String first_name;
    private String last_name;
    private String name;
    private String country;
    private String status = "Active";
    private Date dob;
    private Integer age = 0;
    private Integer ID;

    public Umpire(String first_name, String last_name, String country, Date dob) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.name = first_name + " " + last_name;
        this.country = country;
        this.dob = dob;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String datetext = formatter.format(this.dob);
            Date formatedDate = formatter.parse(datetext);

            LocalDate date = formatedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();
            Period period = Period.between(date, today);
            this.age = period.getYears();
        }
        catch (ParseException e) {
            System.out.println("constructor of umpire " + e);;
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

    public Umpire(Integer ID, String first_name, String last_name, String country, String status, Date dob) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.name = first_name + " " + last_name;
        this.country = country;
        this.status = status;
        this.dob = dob;
        this.ID = ID;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String datetext = formatter.format(this.dob);
            Date formatedDate = formatter.parse(datetext);

            LocalDate date = formatedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();
            Period period = Period.between(date, today);
            this.age = period.getYears();
        }
        catch (ParseException e) {
            System.out.println("constructor of umpire " + e);;
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
