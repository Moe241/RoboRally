package dk.dtu.compute.se.pisd.roborally.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class SpawnSpace extends FieldAction{
    int ownerPlayer;
    public SpawnSpace(int ownerPlayer) { this.ownerPlayer = ownerPlayer; }
    @Override
    public void do_run_Action_M(Game_Controller gameController, Space space) {
        for (int i = 0; i<gameController.board_Var.getPlayersNumber(); i++) {
            if (gameController.board_Var.getPlayer(i).getIsInPit()) {
                if (i == ownerPlayer) {
                    gameController.board_Var.getPlayer(i).setSpace(space);
                    gameController.board_Var.getPlayer(i).setIsInPit(false);
                }
            }
        }
    }

    public int getOwnerPlayer() { return this.ownerPlayer; }
}
