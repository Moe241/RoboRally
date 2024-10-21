
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

public class SpaceView extends StackPane implements ViewObserver {

    final public static int PREF_SPACE_HEIGHT_VAR = 44;
    final public static int PREF_SPACE_WIDTH_Var = 50;
    public final Space space;
    public void setSpace(Space space) {

    }




    public SpaceView(@NotNull Space space) {
        this.space = space;

        this.setPrefWidth(PREF_SPACE_WIDTH_Var);
        this.setMinWidth(PREF_SPACE_WIDTH_Var);
        this.setMaxWidth(PREF_SPACE_WIDTH_Var);

        this.setPrefHeight(PREF_SPACE_HEIGHT_VAR);
        this.setMinHeight(PREF_SPACE_HEIGHT_VAR);
        this.setMaxHeight(PREF_SPACE_HEIGHT_VAR);

        this.setBackground(space.getType().Background);
        this.setBorder(space.getType().Borders);
        space.attach(this);
        update_M(space);
    }

    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    @Override
    public void update_View_M(Subject subject_Var) {
        if (subject_Var == this.space) {
            updatePlayer();
        }
    }

}
