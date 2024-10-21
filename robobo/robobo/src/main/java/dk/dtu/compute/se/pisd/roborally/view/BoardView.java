
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;


public class BoardView extends VBox implements ViewObserver {

    private Board board;

    private GridPane main_Board_Pane_Var;
    private SpaceView[][] spaces_Var;
    public void setBoard(Board board) {
        this.board = board;
    }
    public SpaceView[][] getSpaces() {
        return spaces_Var;
    }

    private PlayersView players_View_Var;

    private Label status_Label_Var;

    private SpaceEventHandler space_Event_Handler_Var;

    public BoardView(@NotNull Game_Controller gameController) {
        board = gameController.board_Var;

        main_Board_Pane_Var = new GridPane();
        players_View_Var = new PlayersView(gameController);
        status_Label_Var = new Label("<no status>");

        this.getChildren().add(main_Board_Pane_Var);
        this.getChildren().add(players_View_Var);
        this.getChildren().add(status_Label_Var);

        spaces_Var = new SpaceView[board.width][board.height];

        space_Event_Handler_Var = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces_Var[x][y] = spaceView;
                main_Board_Pane_Var.add(spaceView, x, y);
                spaceView.setOnMouseClicked(space_Event_Handler_Var);
            }
        }

        board.attach(this);
        update_M(board);
    }

    @Override
    public void update_View_M(Subject subject_Var) {
        if (subject_Var == board) {
            Phase phase = board.getPhase();
            status_Label_Var.setText(board.getStatusMessage());
        }
    }
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

        final public Game_Controller gameController;

        public SpaceEventHandler(@NotNull Game_Controller gameController) {
            this.gameController = gameController;
        }

        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board_Var) {
                    gameController.m_Curr_P_To_Space_M(space);
                    event.consume();
                }
            }
        }

    }

}
