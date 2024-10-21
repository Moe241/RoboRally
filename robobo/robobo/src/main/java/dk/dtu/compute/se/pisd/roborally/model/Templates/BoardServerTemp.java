package dk.dtu.compute.se.pisd.roborally.model.Templates;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * ...
 *
 * author Ekkart Kindler, ekki@dtu.dk
 */
public class BoardServerTemp {

    public int width;
    public int height;
    public int step;
    public Phase phase;
    public boolean stepMode;
    public List<PlayerServerTemp> players = new ArrayList<>();
    public ArrayList<String> playerOrder = new ArrayList<>();





    public Board toBoard() {
        // Create a new Board instance with the required constructor arguments
        Board board = new Board(width, height, step, phase, stepMode);

        // Add players to the board
        for (PlayerServerTemp playerServerTemp : players) {
            Player player = playerServerTemp.toPlayer();
            board.addPlayer(player);
        }

        // Set player order
        List<Player> orderedPlayerList = new ArrayList<>();
        for (String playerName : playerOrder) {
            for (Player player : board.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    orderedPlayerList.add(player);
                    break;
                }
            }
        }
        board.setPlayerOrder(orderedPlayerList);

        return board;
    }

    public BoardServerTemp fromBoard(Board board) {
        BoardServerTemp template = new BoardServerTemp();

        // Set board properties
        board.setStep(board.getStep());
        board.setPhase(board.getPhase());
        board.setStepMode(board.isStepMode());


        return template;
    }



}



