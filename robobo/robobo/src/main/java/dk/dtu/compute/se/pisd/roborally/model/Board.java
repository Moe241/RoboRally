
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;


public class Board extends Subject {


    @Expose
    public final int width;

    @Expose
    public final int height;
    private List<Player> playerOrder;

    @Expose
    public final String boardName;

    @Expose
    private Integer gameId;

    private final Space[][] spaces;

    private ArrayList<Space> spawnSpaces;

    @Expose
    private final List<Player> players = new ArrayList<>();
    private Set<Observer> observers = new HashSet<>();

    @Expose
    private Player current;

    @Expose
    private Player winner = null;

    @Expose
    private Phase phase = INITIALISATION;


    @Expose
    private int amountOfCheckpoints = 0;

    @Expose
    private int step = 0;

    @Expose
    private boolean stepMode;

    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                createSpace(x,y,SpaceType.EMPTY_SPACE);
            }
        }
        this.stepMode = false;
    }

    public Board(int width, int height, int step, Phase phase, boolean stepMode) {
        this(width, height, "defaultboard");
    }

    public Board(int[][] boardArray, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = boardArray[0].length;
        this.height = boardArray.length;
        this.spaces = new Space[width][height];
        this.spawnSpaces = new ArrayList<>();
        for (int y = 0; y < width; y++) {
            for(int x = 0; x < height; x++) {
                createSpace(x,y,SpaceType.get(boardArray[y][x]));
                if (SpaceType.get(boardArray[y][x]).equals(SpaceType.CHECKPOINT1) || SpaceType.get(boardArray[y][x]).equals(SpaceType.CHECKPOINT2) ||
                        SpaceType.get(boardArray[y][x]).equals(SpaceType.CHECKPOINT3) || SpaceType.get(boardArray[y][x]).equals(SpaceType.CHECKPOINT4) ||
                        SpaceType.get(boardArray[y][x]).equals(SpaceType.CHECKPOINT5)) {
                    amountOfCheckpoints++;
                }
                if (SpaceType.get(boardArray[y][x]).equals(SpaceType.SPAWN_SPACE_PLAYER1) || SpaceType.get(boardArray[y][x]).equals(SpaceType.SPAWN_SPACE_PLAYER2) ||
                        SpaceType.get(boardArray[y][x]).equals(SpaceType.SPAWN_SPACE_PLAYER3) || SpaceType.get(boardArray[y][x]).equals(SpaceType.SPAWN_SPACE_PLAYER4) ||
                        SpaceType.get(boardArray[y][x]).equals(SpaceType.SPAWN_SPACE_PLAYER5) || SpaceType.get(boardArray[y][x]).equals(SpaceType.SPAWN_SPACE_PLAYER6)) {
                    spawnSpaces.add(this.getSpace(x,y));
                }
            }
        }
    }
    private void createSpace(int x, int y, SpaceType spaceType) {
        Space space = new Space(this, x, y, spaceType);
        this.spaces[x][y] = space;
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update_M(this); // Pass the necessary argument here
        }
    }


    public void setPlayerOrder(List<Player> playerOrder) {
        this.playerOrder = playerOrder;
        notifyObservers();
    }


    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public boolean isSpaceTypeLaser(SpaceType spaceType) {
        switch (spaceType) {
            case ONE_LASER_UP, TWO_LASER_UP, THREE_LASER_UP, ONE_LASER_DOWN, TWO_LASER_DOWN, THREE_LASER_DOWN, ONE_LASER_LEFT, TWO_LASER_LEFT, THREE_LASER_LEFT,
                    ONE_LASER_RIGHT, TWO_LASER_RIGHT, THREE_LASER_RIGHT:
                return true;
            default:
                return false;
        }
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifychange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifychange();
        }
    }

    public int getAmountOfCheckpoints() {
        return amountOfCheckpoints;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifychange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifychange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifychange();
        }
    }

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }


    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    public String getStatusMessage() {
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep() +
                ", Next Checkpoint: " + (getCurrentPlayer().getCheckpoints()+1);
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Space[][] getSpaces() {
        return spaces;
    }
    public ArrayList<Space> getSpawnSpaces() { return spawnSpaces; }


}
