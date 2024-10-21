package dk.dtu.compute.se.pisd.roborally.fieldActions;

import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class CheckPoint extends FieldAction {

    int checkpointNumber;

    public CheckPoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    @Override
    public void do_run_Action_M(Game_Controller gameController, Space space) {
        if (space.getPlayer().getCheckpoints() == checkpointNumber-1) {
            space.getPlayer().raiseCheckpoints();
            System.out.println(space.getPlayer().getCheckpoints());
        }

    }

}
