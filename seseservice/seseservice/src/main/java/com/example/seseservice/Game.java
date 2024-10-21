package com.example.seseservice;

import java.util.ArrayList;

public class Game {
    private final String id;


    public void addTheLastPlayers(String lastPlayers) {
        this.lastPlayers.add(lastPlayers);


    }
    public void resetTheLastPlayers()
    {
        int size = lastPlayers.size();
        for (int i =0;i<size;i++)
        {
            this.lastPlayers.remove(0);
        }

    }

    public ArrayList<String> lastPlayers = new ArrayList<>();
    public int getPlayers() {
        return players;
    }
    public boolean containsTheLastPlayer(String player)
    {
        for (String pl : lastPlayers)
        {
            if (pl.equals(player))
            {
                return true;
            }

        }
        return false;
    }
    public void setPlayers(int players) {
        this.players = players;
    }


    public boolean ReadyProgramming(String player) {
        boolean readyreturn;
        if(playersPosted == players) {
            resetThePlayersPosted();
            readyProg = true;
        }
        readyreturn = readyProg;
        if (readyProg) {
            readyCounterProg++;
        }

        if (readyCounterProg == players) {
            readyCounterProg = 0;
            readyProg = false;
        }
        System.out.println(readyreturn +"post"+ player);
        return readyreturn;
    }
    public boolean ReadyActivation(String player) {

        boolean readyreturn=false;
        if (readyAct && !containsTheLastPlayer(player)) {
            addTheLastPlayers(player);
            readyreturn=readyAct;
            readyCounterAct++;
        }

        if (readyCounterAct == players-1) {
            readyCounterAct = 0;
            readyAct = false;
        }
        System.out.println(readyreturn+" Act" + player);
        return readyreturn;
    }
    public boolean start = false;
    public boolean readyProg =false;
    public boolean readyAct = false;
    private int readyCounterAct=0;
    private int readyCounterProg =0;
    public int players =1;
    public final int maxPlayers;
    public boolean plPosted[];

    public int getPlayersPosted() {
        return playersPosted;
    }
    public void resetThePlayersPosted()
    {
        for (int i =0;i<plPosted.length;i++)
        {
            plPosted[i] = false;
        }
        playersPosted =0;
    }
    public void setThePlayersPosted(int player) {
        if (plPosted[player-1]==false)
        {
            plPosted[player-1] =true;
            playersPosted=playersPosted+1;
        }
    }

    public int playersPosted=0;
    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    private String jsonFile;

    public Game(final String id,int maxPlayers) {
        this.id = id;
        this.jsonFile = jsonFile;
        this.maxPlayers = maxPlayers;
        this.plPosted = new boolean[maxPlayers];

    }

    public String getId() {
        return id;
    }



    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                "JsonFile as string=" + jsonFile+"\n";
    }
}