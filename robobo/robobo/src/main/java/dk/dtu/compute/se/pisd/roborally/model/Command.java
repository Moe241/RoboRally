
package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public enum Command {


    FORWARD("Forward"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FAST_FORWARD("Fast Forward"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT),
    SPAM("Spam Card", LEFT, RIGHT);

    final public String displayName;


    final private List<Command> options;
    public static Command get(String command) {
        for(Command command1 : values()) {
            if (command1.toString().equals(command)) {
                return command1;
            }
        }
        return null;
    }

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }



    public List<Command> getOptions() {
        return options;
    }

}
