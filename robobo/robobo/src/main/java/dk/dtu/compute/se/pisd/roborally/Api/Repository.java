package dk.dtu.compute.se.pisd.roborally.Api;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.CardFieldView;
import dk.dtu.compute.se.pisd.roborally.view.PlayerView;
import dk.dtu.compute.se.pisd.roborally.view.PlayersView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class Repository implements IRepository {
    private static Repository single_instance = null;
    Client client = new Client();
    int id;
    int playerNumb;
    boolean startGame = false;
    int maxPlayerNumb;

    public static synchronized Repository getInstance() {
        if (single_instance == null)
            single_instance = new Repository();

        return single_instance;
    }

    @Override
    public void SaveGameServer(Board board, String saveName) {

        String jsonData = Client.GetGameInstance(board);

        client.postGameSaveInstance(jsonData, saveName);

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getPlayerNumb() {
        return playerNumb;
    }



    @Override
    public void createGame(int maxNumbOfPlayers) {
        this.maxPlayerNumb = maxNumbOfPlayers;
        id = client.CreateGameInstance(maxNumbOfPlayers);
        playerNumb = 1;


    }

    @Override
    public ArrayList<String> getFiles() {
        ArrayList<String> f = new ArrayList<>();
        String[] files = client.getFilesString();
        Collections.addAll(f, files);
        return f;
    }

    @Override
    public void PostGameInstanceActivation(Board board) {
        String jsonData = Client.GetGameInstance(board);
        client.postGameInstanceActivationPhase(id, playerNumb, jsonData);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void PostGameInstanceProgramming(Board board) {
        String jsonData = Client.GetGameInstance(board);
        client.postGameInstanceProgrammingPhase(id, jsonData, playerNumb);
        waitForPlayersProgress();
    }

    @Override
    public void postGameInstance(Board board) {
        String jsonData = Client.GetGameInstance(board);
        client.postGameInstance(id, jsonData);
    }

    @Override
    public void joinGame(int id) {
        this.id = id;
        playerNumb = client.joinGame(id);
    }

    @Override
    public Board getGameFromServer(String fileName) {
        String gameInstance = client.getFileFromName(fileName);
        Board board = Client.LoadGameInstance(gameInstance);
        return board;
    }

    @Override
    public Board getGameInstance(Board oldBoard) {
        String gameString = client.getGameInstance(id);
        Board newBoard = Client.LoadGameInstance(gameString);
        if (maxPlayerNumb == 0) {
            maxPlayerNumb = newBoard.getPlayersNumber();
        }
        //add to observer if there is
        if (oldBoard != null) {
            newBoard.setObservers(oldBoard.getObservers());
            for (Observer observer : newBoard.getObservers()) {
                if (observer instanceof BoardView) {
                    ((BoardView) observer).setBoard(newBoard);
                    for (int i = 0; i < newBoard.width; i++) {
                        for (int f = 0; f < newBoard.height; f++) {
                            ((BoardView) observer).getSpaces()[i][f].setSpace(newBoard.getSpace(i, f));
                            newBoard.getSpace(i, f).attach(((BoardView) observer).getSpaces()[i][f]);
                        }
                    }
                } else if (observer instanceof PlayersView) {
                    ((PlayersView) observer).setBoard(newBoard);

                } else if (observer instanceof PlayerView) {
                    for (Player player : newBoard.getPlayers()) {
                        if (player == ((PlayerView) observer).getPlayer()) {
                            ((PlayerView) observer).setPlayer(player);
                        }

                    }

                }


            }


            for (int f = 0; f < newBoard.getPlayers().size(); f++) {
                newBoard.getPlayers().get(f).setObservers(oldBoard.getPlayers().get(f).getObservers());
                for (int i = 0; i < newBoard.getPlayers().get(f).getCards().length; i++) {
                    newBoard.getPlayers().get(f).getCards()[i].setObservers(oldBoard.getPlayers().get(f).getCards()[i].getObservers());
                    for (Observer observer : newBoard.getPlayers().get(f).getCards()[i].getObservers()) {
                        if (observer instanceof CardFieldView) {
                            ((CardFieldView) observer).setField(newBoard.getPlayers().get(f).getCards()[i]);
                        }
                    }
                }
                for (int i = 0; i < newBoard.getPlayers().get(f).getProgram().length; i++) {
                    newBoard.getPlayers().get(f).getProgram()[i].setObservers(oldBoard.getPlayers().get(f).getProgram()[i].getObservers());
                    for (Observer observer : newBoard.getPlayers().get(f).getProgram()[i].getObservers()) {
                        if (observer instanceof CardFieldView) {
                            ((CardFieldView) observer).setField(newBoard.getPlayers().get(f).getProgram()[i]);
                        }
                    }
                }
            }
        }
        return newBoard;
    }

    @Override
    public void waitForPlayersProgress() {
        while (true) {

            String status = client.getStatusProg(id, playerNumb);
            if (status.equals("Ready")) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void waitForPlayersAct() {
        while (true) {
            String status = client.getStatusAct(id, playerNumb);
            if (status.equals("Ready")) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void waitingToStart() {
        if (!startGame) {
            if (playerNumb == 1) {
                makeGameStartButton();
            } else {
                makeWaitButton();
            }
        }
    }


    @Override
    public void makeWaitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Lobby");
        alert.setHeaderText("Waiting for players  GameId=" + id + "\nPlayer 1 will start the game when lobby is full");
        alert.setContentText("Players joined: 1/" + maxPlayerNumb);

        ButtonType yesButton = new ButtonType("");
        ButtonType noButton = new ButtonType("");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Thread thread = new Thread(() -> {
            while (true) {
                if (startGame) {
                    Platform.runLater(() -> {
                        alert.setResult(yesButton);
                        System.out.println("Player 1 started the game");
                    });
                    break;
                } else {
                    Platform.runLater(() -> {
                        alert.setContentText("Players joined: " + getStartData() + "/" + maxPlayerNumb);
                    });
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        // show dialog and for user input
        thread.start();
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                if (!startGame) {
                    makeWaitButton();
                }
            } else {
                // click cancel or closed the game
                makeWaitButton();
                System.out.println();
            }
        });

    }

    @Override
    public void setStartTrue() {
        client.setStart(id);
    }

    @Override
    public void makeGameStartButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Lobby");
        alert.setHeaderText("Waiting for players  GameId=" + id + "\nPress start button when players has joined your game");
        alert.setContentText("Players joined: 1/" + maxPlayerNumb);


        ButtonType yesButton = new ButtonType("Start Game");
        ButtonType noButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Thread thread = new Thread(() -> {
            while (true) {
                if (startGame) {
                    Platform.runLater(() -> {
                        alert.close();
                        System.out.println("Player 1 started the game");
                    });
                    break;
                } else {
                    Platform.runLater(() -> {
                        alert.setContentText("Players joined: " + getStartData() + "/" + maxPlayerNumb);
                    });
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        // show dialog and wait for user input
        thread.start();
        Optional<ButtonType> result = alert.showAndWait();

        // process users choice
        result.ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                // clicked start game
                if (Integer.parseInt(getStartData()) == maxPlayerNumb) {
                    setStartTrue();
                    startGame = true;
                    System.out.println("User clicked Start Game");
                } else {
                    makeGameStartButton();
                }
            } else {
                // clicked cancel or closed the gamg
                System.out.println("User clicked Cancel or closed the dialog");
            }
        });
    }

    @Override
    public String getStartData() {
        String[] data = client.getStart(id);
        if (data.length == 2) {
            if (data[1].equals("true")) {
                startGame = true;
            }
            return data[0];
        }
        return null;
    }

}
