package com.example.shenmiao;

public class Ad_information {
    private String name,site,time,iden;
    private int type;
    public Ad_information(String name,String time,String site,String iden,int type) {
        this.name = name;
        this.site = site;
        this.time = time;
        this.type = type;
        this.iden = iden;
    }
    public String getName(){return  name;}
    public String getSite(){return  site;}
    public String getTime(){return  time;}
    public String getIden(){return  iden;}
    public int getType(){return  type;}
    public void setName(String name){this.name = name;}
    public void setSite(String site){this.site = site;}
    public void setTime(String time){this.time = time;}
    public void setIden(String iden){this.iden = iden;}
    public void setType(int type){this.type = type;}
}
