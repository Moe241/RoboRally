
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PlayerView extends Tab implements ViewObserver {

    private Player player_Var;
    private VBox top;

    private Label program_Label_Var;
    private GridPane program_Pane_Var;
    private Label cardsLabel;
    private GridPane cardsPane;

    private CardFieldView[] program_Card_Views_Var;
    private CardFieldView[] cardViews;

    private VBox buttonPanel;

    private Button finishButton;
    private Button executeButton;
    private Button stepButton;

    private VBox playerInteractionPanel;

    private Game_Controller gameController;
    public Player getPlayer() {
        return player_Var;
    }
    public void setPlayer(Player player) {
        this.player_Var = player;
    }

    public PlayerView(@NotNull Game_Controller gameController, @NotNull Player player_Var) {
        super(player_Var.getName());
        this.setStyle("-fx-text-base-color: " + player_Var.getColor() + ";");

        top = new VBox();
        this.setContent(top);

        this.gameController = gameController;
        this.player_Var = player_Var;

        program_Label_Var = new Label("Program");

        program_Pane_Var = new GridPane();
        program_Pane_Var.setVgap(2.0);
        program_Pane_Var.setHgap(2.0);
        program_Card_Views_Var = new CardFieldView[Player.NO_REGISTERS_Var];
        for (int i = 0; i < Player.NO_REGISTERS_Var; i++) {
            CommandCardField card_Field_Var = player_Var.getProgramField(i);
            if (card_Field_Var != null) {
                program_Card_Views_Var[i] = new CardFieldView(gameController, card_Field_Var);
                program_Pane_Var.add(program_Card_Views_Var[i], i, 0);
            }
        }



        finishButton = new Button("Finish Programming");
        finishButton.setOnAction( e -> gameController.f_Program_Phase_m());

        executeButton = new Button("Execute Program");
        executeButton.setOnAction( e-> gameController.execute_Program());

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction( e-> gameController.execute_Step_M());

        buttonPanel = new VBox(finishButton, executeButton, stepButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS_VAr];
        for (int i = 0; i < Player.NO_CARDS_VAr; i++) {
            CommandCardField cardField = player_Var.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(program_Label_Var);
        top.getChildren().add(program_Pane_Var);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        if (player_Var.board != null) {
            player_Var.board.attach(this);
            update_M(player_Var.board);
        }
    }

    @Override
    public void update_View_M(Subject subject_Var) {
        if (subject_Var == player_Var.board) {
            for (int i = 0; i < Player.NO_REGISTERS_Var; i++) {
                CardFieldView cardFieldView = program_Card_Views_Var[i];
                if (cardFieldView != null) {
                    if (player_Var.board.getPhase() == Phase.PROGRAMMING ) {
                        cardFieldView.setBackground(CardFieldView.BG_DEFAULT_Var);
                    } else {
                        if (i < player_Var.board.getStep()) {
                            cardFieldView.setBackground(CardFieldView.BG_DONE_Var);
                        } else if (i == player_Var.board.getStep()) {
                            if (player_Var.board.getCurrentPlayer() == player_Var) {
                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE_VAr);
                            } else if (player_Var.board.getPlayerNumber(player_Var.board.getCurrentPlayer()) > player_Var.board.getPlayerNumber(player_Var)) {
                                cardFieldView.setBackground(CardFieldView.BG_DONE_Var);
                            } else {
                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT_Var);
                            }
                        } else {
                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT_Var);
                        }
                    }
                }
            }

            if (player_Var.board.getPhase() != Phase.PLAYER_INTERACTION) {
                if (!program_Pane_Var.getChildren().contains(buttonPanel)) {
                    program_Pane_Var.getChildren().remove(playerInteractionPanel);
                    program_Pane_Var.add(buttonPanel, Player.NO_REGISTERS_Var, 0);
                }
                switch (player_Var.board.getPhase()) {
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(true);
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(false);
                        break;

                    default:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                }


            } else {
                if (!program_Pane_Var.getChildren().contains(playerInteractionPanel)) {
                    program_Pane_Var.getChildren().remove(buttonPanel);
                    program_Pane_Var.add(playerInteractionPanel, Player.NO_REGISTERS_Var, 0);
                }
                playerInteractionPanel.getChildren().clear();

                if (player_Var.board.getCurrentPlayer() == player_Var) {
                    Command currentCommand = player_Var.getProgramField(player_Var.board.getStep()).getCard().command;
                    List<Command> commandOptions = currentCommand.getOptions();
                    for (Command currentOption: commandOptions) {
                        Button optionButton = new Button(currentOption.displayName);
                        optionButton.setOnAction( e -> {
                            gameController.execute_Command_M(player_Var, currentOption);
                            gameController.next_Player_M();
                        });
                        optionButton.setDisable(false);
                        playerInteractionPanel.getChildren().add(optionButton);
                    }
                }
            }

            if (player_Var.board.getPhase() == Phase.WINNER) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("A Winner is found");
                String s = "Exit the game and restart, the winner is " + player_Var.board.getWinner().getName();
                alert.setContentText(s);
                alert.showAndWait();
            }
        }
    }

}
