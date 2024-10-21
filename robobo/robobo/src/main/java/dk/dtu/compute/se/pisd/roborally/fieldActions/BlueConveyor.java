package dk.dtu.compute.se.pisd.roborally.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class BlueConveyor extends FieldAction{

    private final Heading Direction_Var;

    public BlueConveyor(Heading directionVar) {
        this.Direction_Var = directionVar;
    }
    @Override
    public void do_run_Action_M(Game_Controller gameController, Space space) {
        move_Player(gameController, space, this.Direction_Var);
        Space newSpace = gameController.board_Var.getNeighbour(space, Direction_Var);
        move_Player(gameController, newSpace, this.Direction_Var);
    }
}
