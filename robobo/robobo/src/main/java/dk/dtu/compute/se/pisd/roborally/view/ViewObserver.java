
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import javafx.application.Platform;


public interface ViewObserver extends Observer {

    void update_View_M(Subject subject_Var);

    @Override
    default void update_M(Subject subject) {
        if (Platform.isFxApplicationThread()) {
            update_View_M(subject);
        } else {
            Platform.runLater(() -> update_View_M(subject));
        }
    }

}
