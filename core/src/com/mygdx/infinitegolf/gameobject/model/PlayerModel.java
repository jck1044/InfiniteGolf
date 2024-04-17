package com.mygdx.infinitegolf.gameobject.model;

public class PlayerModel {
    private String name;
    private int score;

    public PlayerModel(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
