package com.nosql.comnosql.beans;

public class Authenticate {
    private boolean authenticate;

    public Authenticate(boolean auth){
        this.authenticate = auth;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

}
