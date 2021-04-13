package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Models;

import com.google.firebase.database.Exclude;


public class ModelsContact {

    private String Description;
    private String lastName;
    private String firstName;
    private String phone;
    private String photo;
    private String email;
    private String sex;
    private String dayOfbirth ;
    private String password;
    private String seletedSex;
    private String confirmPassword;
    private String getKeyUser;


    public ModelsContact(String getKeyUser) {
    }

    public ModelsContact(String lastName, String firstName,
                         String phone, String email, String dayOfbirth, String seletedSex) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.email = email;
        this.dayOfbirth = dayOfbirth;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.photo = photo;
        this.sex = seletedSex;
        this.getKeyUser = getKeyUser;

    }

    public  ModelsContact(){

    }


    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getDayOfbirth() { return dayOfbirth; }

    public void setDayOfbirth(String dayOfbirth) { this.dayOfbirth = dayOfbirth; }

    @Exclude
    public String getPassword() { return password; }

    @Exclude
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }

    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }

    public String getSex() { return sex; }

    public void setSex(String sex) { this.sex = sex; }

    public void setGetKeyUser(String getKeyUser) { this.getKeyUser = getKeyUser; }

    public String getGetKeyUser() { return getKeyUser; }

    public String getDescription() { return Description; }

    public void setDescription(String description) { Description = description; }

}
