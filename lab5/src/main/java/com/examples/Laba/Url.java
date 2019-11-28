package com.examples.Laba;

public class Url {
    String url;
    Integer count;

    public Url(String url, Integer count){
        this.url = url;
        this.count = count;
    }

    public String getUrl(){
        return url;
    }

    public String getCount(){
        return count;
    }
}
