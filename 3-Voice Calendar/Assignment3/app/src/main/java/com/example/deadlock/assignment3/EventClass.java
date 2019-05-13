package com.example.deadlock.assignment3;

public class EventClass {
    private Long ID;
    private String Name;
    private String Date;

    public EventClass(Long id ,String name, String date)
    {
        this.ID = id;
        this.Date= date;
        this.Name=name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
