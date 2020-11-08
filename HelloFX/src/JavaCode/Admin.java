/*
 * Author MD. Sakibur Reza
 */

package JavaCode;

public class Admin {
    private int adminID;
    private String password;

    public int getAdminID() {
        return adminID;
    }

    public String getPassword() {
        return password;
    }

    public Admin() {
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminID=" + adminID +
                ", password='" + password + '\'' +
                '}';
    }
}
