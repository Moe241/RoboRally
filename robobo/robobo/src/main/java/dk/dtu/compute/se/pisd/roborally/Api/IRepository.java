package dk.dtu.compute.se.pisd.roborally.Api;

import dk.dtu.compute.se.pisd.roborally.model.Board;

import java.util.ArrayList;

public interface IRepository {

    void SaveGameServer(Board board, String saveName);

    int getId();

    int getPlayerNumb();

    void createGame(int maxNumbOfPlayers);

    ArrayList<String> getFiles();

    void PostGameInstanceActivation(Board board);

    void PostGameInstanceProgramming(Board board);

    void postGameInstance(Board board);

    void joinGame(int id);

    Board getGameFromServer(String fileName);

    Board getGameInstance(Board oldBoard);


    void waitForPlayersProgress();

    void waitForPlayersAct();

    void waitingToStart();

    void makeWaitButton();

    void setStartTrue();

    void makeGameStartButton();

    String getStartData();
}
