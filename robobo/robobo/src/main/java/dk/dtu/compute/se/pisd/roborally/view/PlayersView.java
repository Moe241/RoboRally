
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.TabPane;

public class PlayersView extends TabPane implements ViewObserver {

    private Board board;
    public void setBoard(Board board) {
        this.board = board;
    }
    private PlayerView[] player_Views_Var;

    public PlayersView(Game_Controller gameController) {
        board = gameController.board_Var;

        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        player_Views_Var = new PlayerView[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber();  i++) {
            player_Views_Var[i] = new PlayerView(gameController, board.getPlayer(i));
            this.getTabs().add(player_Views_Var[i]);
        }
        board.attach(this);
        update_M(board);
    }

    @Override
    public void update_View_M(Subject subject_Var) {
        if (subject_Var == board) {
            Player current = board.getCurrentPlayer();
            this.getSelectionModel().select(board.getPlayerNumber(current));
        }
    }

}
