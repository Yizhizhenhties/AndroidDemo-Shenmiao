package com.example.shenmiao;

import android.media.Image;

public class News {
    private String newsUrl;     //新闻链接地址
    private String desc;        //新闻概要
    private String autor;
    private String image;

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getAutor(){return autor;}
    public void setAutor(String autor){this.autor = autor;}

    public String getImage(){return image;}
    public void setImage(String image){this.image = image;}
}