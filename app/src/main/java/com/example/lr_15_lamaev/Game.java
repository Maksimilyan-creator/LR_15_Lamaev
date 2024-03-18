package com.example.lr_15_lamaev;

public class Game
{
    private String id;
    private String name; // название
    private String publisher; // издатель
    private int posterResource; // ресурс постера

    public Game(String id, String name, String publisher, int poster )
    {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.posterResource = poster;
    }

    public Game () {}

    public String getId() {return id;}
    public void setId (String id) {this.id = id;}

    public String getName() {return  this.name;}
    public void setName (String name) {this.name = name;}
    public String getPublisher() {return this.publisher;}
    public void setPublisher (String publisher) {this.publisher = publisher;}
    public int getPosterResource() {return this.posterResource;}
    public void setPosterResource(int posterResource) {this.posterResource = posterResource;}
}
