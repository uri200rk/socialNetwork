package com.example.snapshot.Clases;

import java.util.Date;

public class Publication {

    private int idPublication, idUser, likes;
    private String nick, title, description, idMedia, date;

    //first constructor
    public Publication(int idPublication, int idUser, String nick, int likes, String title, String description, String idMedia, String date) {
        this.idPublication = idPublication;
        this.idUser = idUser;
        this.nick = nick;
        this.likes = likes;
        this.title = title;
        this.description = description;
        this.idMedia = idMedia;
        this.date = date;
    }


    public int getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(int idPublication) {
        this.idPublication = idPublication;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(String idMedia) {
        this.idMedia = idMedia;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "idPublication=" + idPublication +
                ", idUser=" + idUser +
                ", likes=" + likes +
                ", nick='" + nick + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idMedia='" + idMedia + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
