
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

public class CommandCard extends Subject {

    @Expose
    final public Command command;

    public CommandCard(@NotNull Command command) {
        this.command = command;
    }

    public String getName() {
        return command.displayName;
    }


}
