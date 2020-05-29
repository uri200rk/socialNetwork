package com.example.snapshot.Clases;

public class Follow {

    private int idUser, following, idFollow;


    public Follow(int idUser, int following, int idFollow) {
        this.idUser = idUser;
        this.following = following;
        this.idFollow = idFollow;
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

    @Override
    public String toString() {
        return "Follow{" +
                "idUser=" + idUser +
                ", following=" + following +
                ", idFollow=" + idFollow +
                '}';
    }
}
