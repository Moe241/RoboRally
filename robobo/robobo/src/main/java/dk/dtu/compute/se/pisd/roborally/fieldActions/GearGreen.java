package dk.dtu.compute.se.pisd.roborally.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class GearGreen extends FieldAction{
    @Override
    public void do_run_Action_M(Game_Controller gameController, Space space) {
        gameController.turn_Left_M(space.getPlayer());
    }
}
