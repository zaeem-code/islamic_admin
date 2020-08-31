package com.example.islamicappp_adminpanel;

public class Model
{

    String answer;
    String name;

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    String phone;

    public Model(String answer, String name, String phone, String date, String time) {
        this.answer = answer;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.time = time;
    }

    String date;
    String time;


    public Model() {
    }




    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
