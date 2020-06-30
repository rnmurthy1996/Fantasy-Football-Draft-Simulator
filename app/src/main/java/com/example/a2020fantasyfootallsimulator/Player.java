package com.example.a2020fantasyfootallsimulator;

public class Player {
    private String name;
    private String team;
    private String position;
    private double projPoints;
    private int byeWeek;
    private int overallRank;

    Player(String n, String t, String p, double pt, int b, int r) {
        this.name = n;
        this.team = t;
        this.position = p;
        this.projPoints = pt;
        this.byeWeek = b;
        this.overallRank = r;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public double getProjPoints() {
        return projPoints;
    }

    public int getByeWeek() {
        return byeWeek;
    }

    public int getOverallRank() {
        return overallRank;
    }
}
