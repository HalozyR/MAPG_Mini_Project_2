package ece.np.edu.miniproject2;

public class Users {

    String Name, Password, Email, Phone;

    public Users(){

    }

    public Users(String pName, String pPassword, String pEmail, String pPhone) {
        Name = pName;
        Password = pPassword;
        Email = pEmail;
        Phone = pPhone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
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
