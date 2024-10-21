
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;


public class CommandCardField extends Subject {

    final public Player player;


    @Expose
    private CommandCard card;


    @Expose
    private boolean visible;


    public CommandCardField(Player player) {
        this.player = player;
        this. card = null;
        this.visible = true;
    }


    public CommandCard getCard() {
        return card;
    }

    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifychange();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifychange();
        }

    }
}
