package kr.ac.kpu.se2018158037.tensor_test1;

public class User {
    String femail;
    String fname;

    public String getUEmail() {
        return femail;
    }

    public void setUEmail(String UEmail) {
        this.femail = femail;
    }

    public String getplant_name() {
        return fname;
    }

    public void setplant_name(String plant_name) {
        this.fname = fname;
    }

    public User(String femail, String fname) {
        this.femail = femail;
        this.fname = fname;
    }
}
