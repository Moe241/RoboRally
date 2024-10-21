
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;


public class Player extends Subject {

    @Expose
    final public static int NO_REGISTERS_Var = 5;
    @Expose
    final public static int NO_CARDS_VAr = 9;

    final public Board board;
    @Expose
    private String name;
    @Expose
    private String color;
    @Expose
    private boolean isInPit;
    @Expose
    private int checkpoints;
    @Expose
    private Space space;
    @Expose
    private Heading heading = SOUTH;

    @Expose
    private int spamCards = 0;

    @Expose
    private CommandCardField[] program;

    @Expose
    private CommandCardField[] cards;


    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;
        this.checkpoints = 0;
        this.isInPit = true;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS_Var];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS_VAr];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
         }
    }

    public void raiseCheckpoints() {
        checkpoints++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifychange();
            if (space != null) {
                space.player_Change();
            }
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifychange();
        if (space != null) {
            space.player_Change();
        }
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifychange();
        }
    }
    public int getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(int checkpoints) {
        this.checkpoints = checkpoints;
    }
    public Heading getHeading() {
        return heading;
    }

    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifychange();
            if (space != null) {
                space.player_Change();
            }
        }
    }

    public boolean getIsInPit() { return isInPit; }
    public void setIsInPit(boolean inPit) { isInPit = inPit; }
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public CommandCardField[] getProgram() {
        return program;
    }

    public CommandCardField[] getCards() {
        return cards;
    }

    public void decSpamCards() {
        if (spamCards>=1) {
            spamCards--;
        }
    }

    public void addSpamCards(int number) {
        for (int i = 0; i < number; i++)
            if (spamCards == 9) {
                break;
            }
            else {
                spamCards++;
            }
    }

    public int getSpamCards() {
        return spamCards;
    }
    public void setSpamCards(int amountOfSpam) { this.spamCards=amountOfSpam; }
}
