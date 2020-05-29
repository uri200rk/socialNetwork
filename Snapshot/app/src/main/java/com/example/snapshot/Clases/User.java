package com.example.snapshot.Clases;

public class User {

    private int idUser;
    private String fullName, nick, mail, password;

    //first constructor
    public User(int idUser, String fullName, String nick, String mail, String password) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.nick = nick;
        this.mail = mail;
        this.password = password;

    }

    //second constructor
    public User( String nick , int idUser) {
        this.nick = nick;
        this.idUser = idUser;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", fullName='" + fullName + '\'' +
                ", nick='" + nick + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
