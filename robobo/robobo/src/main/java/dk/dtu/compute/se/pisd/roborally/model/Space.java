
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;


public class Space extends Subject {

    public final Board board;
    @Expose
    public final int x;
    @Expose
    public final int y;
    @Expose
    public final SpaceType type;
    private Player player;

    public Space(Board board, int x, int y, SpaceType spaceType) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.type = spaceType;
        player = null;
    }

    public void ex_FieldAction_M(Game_Controller gameController) {
        this.type.fieldAction.do_run_Action_M(gameController, this);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPlayerOnSpace() {
        return getPlayer() != null;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifychange();
        }
    }


    public Space getSpace() { return this; }


    public SpaceType getType() {
        return type;
    }

    void player_Change() {
        notifychange();
    }
}
