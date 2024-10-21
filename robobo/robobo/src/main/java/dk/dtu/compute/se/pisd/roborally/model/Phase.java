
package dk.dtu.compute.se.pisd.roborally.model;


public enum Phase {
    INITIALISATION, PROGRAMMING, ACTIVATION, PLAYER_INTERACTION, WINNER;

    public static Phase get(String phase) {
        for(Phase phase1 : values()) {
            if (phase1.toString().equals(phase)) {
                return phase1;
            }
        }
        return null;
    }
}
