
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RoboRally extends Application {

    private static final int MIN_APP_WIDTH_VAr = 600;

    private Stage stage_Var;
    private BorderPane board_Root_Var;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primary_Stage_Var) throws IOException {


        stage_Var = primary_Stage_Var;

        AppController app_Controller_Var = new AppController(this);

        RoboRallyMenuBar menu_Bar_Var = new RoboRallyMenuBar(app_Controller_Var);
        board_Root_Var = new BorderPane();
        VBox vbox_Var = new VBox(menu_Bar_Var, board_Root_Var);
        vbox_Var.setMinWidth(MIN_APP_WIDTH_VAr);
        Scene primary_Scene_Var = new Scene(vbox_Var);

        stage_Var.setScene(primary_Scene_Var);
        stage_Var.setTitle("RoboRally");
        stage_Var.setOnCloseRequest(
                e -> {
                    e.consume();
                    app_Controller_Var.exit_M();} );
        stage_Var.setResizable(false);
        stage_Var.sizeToScene();
        stage_Var.show();
    }

    public void create_Board_View_M(Game_Controller game_Controller_Var) {
        board_Root_Var.getChildren().clear();

        if (game_Controller_Var != null) {
            BoardView board_View_Var = new BoardView(game_Controller_Var);
            board_Root_Var.setCenter(board_View_Var);
        }

        stage_Var.sizeToScene();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}