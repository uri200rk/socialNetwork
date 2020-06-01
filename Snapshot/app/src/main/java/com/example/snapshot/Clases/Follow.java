package com.example.snapshot.Clases;

public class Follow {

    private int idUser, following, idFollow;
    private String nameFollowing;


    public Follow(int idUser, int following, int idFollow) {
        this.idUser = idUser;
        this.following = following;
        this.idFollow = idFollow;
    }

    //second constructor
    public Follow(String nameFollowing, int idUser) {
        this.nameFollowing = nameFollowing;
        this.idUser = idUser;

    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getIdFollow() {
        return idFollow;
    }

    public void setIdFollow(int idFollow) {
        this.idFollow = idFollow;
    }

    public String getNameFollowing() {
        return nameFollowing;
    }

    public void setNameFollowing(String nameFollowing) {
        this.nameFollowing = nameFollowing;
    }


    @Override
    public String toString() {
        return "Follow{" +
                "idUser=" + idUser +
                ", following=" + following +
                ", idFollow=" + idFollow +
                ", nameFollowing='" + nameFollowing + '\'' +
                '}';
    }
}
