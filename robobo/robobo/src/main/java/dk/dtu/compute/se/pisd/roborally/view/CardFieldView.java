
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Game_Controller;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class CardFieldView extends GridPane implements ViewObserver {

    final public static  DataFormat ROBO_RALLY_CARD_Var = new DataFormat("games/roborally/cards");

    final public static int CARDFIELD_WIDTH_Var = 65;
    final public static int CARDFIELD_HEIGHT_VAr = 65;

    final public static Border BORDER_Var = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));

    final public static Background BG_DEFAULT_Var = new Background(new BackgroundFill(Color.WHITE, null, null));
    final public static Background BG_DRAG_Var = new Background(new BackgroundFill(Color.GRAY, null, null));
    final public static Background BG_DROP = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));

    final public static Background BG_ACTIVE_VAr = new Background(new BackgroundFill(Color.YELLOW, null, null));
    final public static Background BG_DONE_Var = new Background(new BackgroundFill(Color.GREENYELLOW,  null, null));

    private CommandCardField field_Var;
    private CommandCardField field;

    private Label label_Var;

    private Game_Controller game_Controller_Var;

    public CardFieldView(@NotNull Game_Controller game_Controller_Var, @NotNull CommandCardField field_Var) {
        this.game_Controller_Var = game_Controller_Var;
        this.field_Var = field_Var;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(5, 5, 5, 5));

        this.setBorder(BORDER_Var);
        this.setBackground(BG_DEFAULT_Var);

        this.setPrefWidth(CARDFIELD_WIDTH_Var);
        this.setMinWidth(CARDFIELD_WIDTH_Var);
        this.setMaxWidth(CARDFIELD_WIDTH_Var);
        this.setPrefHeight(CARDFIELD_HEIGHT_VAr);
        this.setMinHeight(CARDFIELD_HEIGHT_VAr);
        this.setMaxHeight(CARDFIELD_HEIGHT_VAr);

        label_Var = new Label("This is a slightly longer text");
        label_Var.setWrapText(true);
        label_Var.setMouseTransparent(true);
        this.add(label_Var, 0, 0);

        this.setOnDragDetected(new OnDragDetectedHandler());
        this.setOnDragOver(new OnDragOverHandler());
        this.setOnDragEntered(new OnDragEnteredHandler());
        this.setOnDragExited(new OnDragExitedHandler());
        this.setOnDragDropped(new OnDragDroppedHandler());
        this.setOnDragDone(new OnDragDoneHandler());

        field_Var.attach(this);
        update_M(field_Var);
    }

    private String cardFieldRepresentation(CommandCardField cardField) {
        if (cardField.player != null) {

            for (int i = 0; i < Player.NO_REGISTERS_Var; i++) {
                CommandCardField other = cardField.player.getProgramField(i);
                if (other == cardField) {
                    return "P," + i;
                }
            }

            for (int i = 0; i < Player.NO_CARDS_VAr; i++) {
                CommandCardField other = cardField.player.getCardField(i);
                if (other == cardField) {
                    return "C," + i;
                }
            }
        }
        return null;

    }
    public void setField(CommandCardField field) {
        this.field = field;
    }

    private CommandCardField card_Field_From_Represent(String rep) {
        if (rep != null && field_Var.player != null) {
            String[] strings_Var = rep.split(",");
            if (strings_Var.length == 2) {
                int i = Integer.parseInt(strings_Var[1]);
                if ("P".equals(strings_Var[0])) {
                    if (i < Player.NO_REGISTERS_Var) {
                        return field_Var.player.getProgramField(i);
                    }
                } else if ("C".equals(strings_Var[0])) {
                    if (i < Player.NO_CARDS_VAr) {
                        return field_Var.player.getCardField(i);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void update_View_M(Subject subject_Var) {
        if (subject_Var == field_Var && subject_Var != null) {
            CommandCard card_Var = field_Var.getCard();
            if (card_Var != null && field_Var.isVisible()) {
                label_Var.setText(card_Var.getName());
            } else {
                label_Var.setText("");
            }
        }
    }

    private class OnDragDetectedHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView source_Var = (CardFieldView) t;
                CommandCardField cardField_Var = source_Var.field_Var;
                if (cardField_Var != null &&
                        cardField_Var.getCard() != null &&
                        cardField_Var.player != null &&
                        cardField_Var.player.board != null &&
                        cardField_Var.player.board.getPhase().equals(Phase.PROGRAMMING)) {
                    Dragboard db_Var = source_Var.startDragAndDrop(TransferMode.MOVE);
                    Image image_Var = source_Var.snapshot(null, null);
                    db_Var.setDragView(image_Var);

                    ClipboardContent content_Var = new ClipboardContent();
                    content_Var.put(ROBO_RALLY_CARD_Var, cardFieldRepresentation(cardField_Var));

                    db_Var.setContent(content_Var);
                    source_Var.setBackground(BG_DRAG_Var);
                }
            }
            event.consume();
        }

    }

    private class OnDragOverHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target_Var = (CardFieldView) t;
                CommandCardField card_Field_Var = target_Var.field_Var;
                if (card_Field_Var != null &&
                        (card_Field_Var.getCard() == null || event.getGestureSource() == target_Var) &&
                        card_Field_Var.player != null &&
                        card_Field_Var.player.board != null) {
                    if (event.getDragboard().hasContent(ROBO_RALLY_CARD_Var)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
            }
            event.consume();
        }

    }

    private class OnDragEnteredHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target_Var = (CardFieldView) t;
                CommandCardField card_Field_Var = target_Var.field_Var;
                if (card_Field_Var != null &&
                        card_Field_Var.getCard() == null &&
                        card_Field_Var.player != null &&
                        card_Field_Var.player.board != null) {
                    if (event.getGestureSource() != target_Var &&
                            event.getDragboard().hasContent(ROBO_RALLY_CARD_Var)) {
                        target_Var.setBackground(BG_DROP);
                    }
                }
            }
            event.consume();
        }

    }

    private class OnDragExitedHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target_Var = (CardFieldView) t;
                CommandCardField card_Field_Var = target_Var.field_Var;
                if (card_Field_Var != null &&
                        card_Field_Var.getCard() == null &&
                        card_Field_Var.player != null &&
                        card_Field_Var.player.board != null) {
                    if (event.getGestureSource() != target_Var &&
                            event.getDragboard().hasContent(ROBO_RALLY_CARD_Var)) {
                        target_Var.setBackground(BG_DEFAULT_Var);
                    }
                }
            }
            event.consume();
        }

    }

    private class OnDragDroppedHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView target_Var = (CardFieldView) t;
                CommandCardField card_Field_Var = target_Var.field_Var;

                Dragboard db_Var = event.getDragboard();
                boolean success_var = false;
                if (card_Field_Var != null &&
                        card_Field_Var.getCard() == null &&
                        card_Field_Var.player != null &&
                        card_Field_Var.player.board != null) {
                    if (event.getGestureSource() != target_Var &&
                            db_Var.hasContent(ROBO_RALLY_CARD_Var)) {
                        Object object = db_Var.getContent(ROBO_RALLY_CARD_Var);
                        if (object instanceof String) {
                            CommandCardField source = card_Field_From_Represent((String) object);
                            if (source != null && game_Controller_Var.move_Cards_M(source, card_Field_Var)) {
                                    success_var = true;

                            }
                        }
                    }
                }
                event.setDropCompleted(success_var);
                target_Var.setBackground(BG_DEFAULT_Var);
            }
            event.consume();
        }

    }

    private class OnDragDoneHandler implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView) {
                CardFieldView source = (CardFieldView) t;
                source.setBackground(BG_DEFAULT_Var);
            }
            event.consume();
        }

    }

}




