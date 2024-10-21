package dk.dtu.compute.se.pisd.roborally.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;


public class GreenConveyor extends FieldAction{

    private final Heading Direction;

    public GreenConveyor(Heading direction) {
        this.Direction = direction;
    }
    @Override
    public void do_run_Action_M(Game_Controller gameController, Space space) {
        move_Player(gameController, space, this.Direction);
    }
}
