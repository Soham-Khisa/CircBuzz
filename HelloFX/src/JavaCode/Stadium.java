package JavaCode;

public class Stadium {
    private int stadiumID;
    private String stadium_name;
    private String location;
    private String country;
    private int capacity;

    public Stadium(String stadium_name, String location, String country, int capacity) {
        this.stadium_name = stadium_name;
        this.location = location;
        this.country = country;
        this.capacity = capacity;
    }

    public Stadium(Integer stadiumID, String stadium_name, String location, String country, int capacity) {
        this.stadiumID = stadiumID;
        this.stadium_name = stadium_name;
        this.location = location;
        this.country = country;
        this.capacity = capacity;
    }

    public Stadium() {
    }

    public String getStadium_name() {
        return stadium_name;
    }

    public void setStadium_name(String stadium_name) {
        this.stadium_name = stadium_name;
    }

    public int getStadiumID() {
        return stadiumID;
    }
    public void setStadiumID(int stadiumID) {
        this.stadiumID = stadiumID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Stadium{" +
                "stadium_name='" + stadium_name + '\'' +
                ", location='" + location + '\'' +
                ", country='" + country + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
