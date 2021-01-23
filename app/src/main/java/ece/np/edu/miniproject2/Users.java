package ece.np.edu.miniproject2;

public class Users {

    String Name, Email, Phone;

    public Users(){

    }

    public Users(String pName, String pEmail, String pPhone) {
        Name = pName;
        Email = pEmail;
        Phone = pPhone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
}
