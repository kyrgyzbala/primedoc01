package kg.kyrgyzcoder.primedoc01.ui.chat.model;

public class Doctor {

    private String image;
    private String name;
    private String surname;
    private String phone;
    private String fatherName;

    public Doctor() {
    }

    public Doctor(String image, String name, String surname, String phone, String fatherName) {
        this.image = image;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.fatherName = fatherName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", fatherName='" + fatherName + '\'' +
                '}';
    }
}
