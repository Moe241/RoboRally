package dk.dtu.compute.se.pisd.roborally.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public abstract class FieldAction {
    public abstract void do_run_Action_M(Game_Controller gameController, Space space);

    static void move_Player(Game_Controller gameController, Space space, Heading direction) {
        Space target_Var = gameController.board_Var.getNeighbour(space, direction);
        if (target_Var != null && space.getPlayer() != null && space.getPlayer().board == gameController.board_Var &&
                !space.getType().BorderHeadings.contains(direction) &&
                !target_Var.getType().BorderHeadings.contains(gameController.reverse_Heading_M(direction))) {
            Player targetPlayer = target_Var.getPlayer();
            if (targetPlayer == null) {
                target_Var.setPlayer(space.getPlayer());
            } else {
                Space pushSpace = gameController.board_Var.getNeighbour(target_Var, direction);
                if (pushSpace != null && !target_Var.getType().BorderHeadings.contains(direction) &&
                        !pushSpace.getType().BorderHeadings.contains(gameController.reverse_Heading_M(direction))) {
                    pushSpace.setPlayer(targetPlayer);
                    target_Var.setPlayer(space.getPlayer());
                }
            }
        }
    }
}
