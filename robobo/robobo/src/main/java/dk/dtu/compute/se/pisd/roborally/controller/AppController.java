
package dk.dtu.compute.se.pisd.roborally.controller;

import com.google.gson.*;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.Api.Repository;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.fileaccess.JsonFileHandler;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.PremadeMaps;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import static dk.dtu.compute.se.pisd.roborally.view.PremadeMaps.loadMapFromJson;


public class AppController implements Observer {

    final private List<Integer> P_NUMBER_OP_VAR = Arrays.asList(2, 3, 4, 5, 6);


    final private List<String> P_COLORS_Var = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");


    final private RoboRally robo_Rally_Var;


    private Game_Controller game_Controller_Var;
    public Repository repository = null;
    private PremadeMaps map_Var;
    private final JsonFileHandler j_File_Handler_Var = new JsonFileHandler();

    public AppController(@NotNull RoboRally robo_Rally_Var) throws IOException {
        this.robo_Rally_Var = robo_Rally_Var;
    }


    public void newGame() {
        // Create a dialog to choose the number of players
        ChoiceDialog<Integer> p_Number_Dialog_Var = new ChoiceDialog<>(P_NUMBER_OP_VAR.get(0), P_NUMBER_OP_VAR);
        p_Number_Dialog_Var.setTitle("Player number");
        p_Number_Dialog_Var.setHeaderText("Select number of players");

        // Show the dialog and get the chosen number
        Optional<Integer> pNumber_Result_Var = p_Number_Dialog_Var.showAndWait();

        // A list of file paths for all the map JSON files
        ArrayList<String> filePaths = new ArrayList<>(Arrays.asList(
                "src/main/java/dk/dtu/compute/se/pisd/Maps/testmap.json",
                "src/main/java/dk/dtu/compute/se/pisd/Maps/corridorblitz.json",
                "src/main/java/dk/dtu/compute/se/pisd/Maps/sprintcramp.json"
        ));

        // A map that associates each map name with its file path
        Map<String, String> mapNamesToFilePaths = new HashMap<>();

        // For each file path, load the map data, and if it's valid, add an entry to the mapNamesToFilePaths map.
        // The key of the entry is the map's name, and the value is the file path to the map's JSON file.
        for (String filePath : filePaths) {
            PremadeMaps tempMap = loadMapFromJson(filePath);
            if (tempMap != null) {
                mapNamesToFilePaths.put(tempMap.getMapName(), filePath);
            }
        }

        // Create a dialog for choosing a map, using the map names
        ChoiceDialog<String> map_Choice_Dialog_Var = new ChoiceDialog<>(mapNamesToFilePaths.keySet().iterator().next(), mapNamesToFilePaths.keySet());
        map_Choice_Dialog_Var.setTitle("Map type");
        map_Choice_Dialog_Var.setHeaderText("Select map type");

        // Show the dialog and get the chosen map name
        Optional<String> map_Choice_Result_Name_Var = map_Choice_Dialog_Var.showAndWait();

        PremadeMaps map_Choice_Result_Var = null;
        if (map_Choice_Result_Name_Var.isPresent()) {
            // Get the file path associated with the chosen map name
            String filePath = mapNamesToFilePaths.get(map_Choice_Result_Name_Var.get());
            map_Choice_Result_Var = loadMapFromJson(filePath);
        }

        this.map_Var = map_Choice_Result_Var;

        // If a valid player number and map were chosen, start a new game
        if (pNumber_Result_Var.isPresent() && map_Choice_Result_Name_Var.isPresent() && map_Choice_Result_Var != null) {
            if (game_Controller_Var != null) {
                if (!stop_Game()) {
                    return;
                }
            }

            // Create a new board with the chosen map
            Board board_Var = new Board(map_Choice_Result_Var.getMapArray(), map_Choice_Result_Var.getMapName());

            // Create a new game controller with the new board
            game_Controller_Var = new Game_Controller(board_Var);

            // Create the players
            int no_Var = pNumber_Result_Var.get();
            for (int i = 0; i < no_Var; i++) {
                Player player_Var = new Player(board_Var, P_COLORS_Var.get(i), "Player " + (i + 1));
                board_Var.addPlayer(player_Var);
            }

            // Start the game
            game_Controller_Var.spawn_Players();
            game_Controller_Var.start_Programming_Phase();

            game_Controller_Var.addRepository();
            repository.createGame(game_Controller_Var.board_Var.getPlayers().size());

            // Update the view to reflect the new game
            robo_Rally_Var.create_Board_View_M(game_Controller_Var);
            if (repository != null) {
                repository.postGameInstance(board_Var);
                repository.waitingToStart();
        }
        }
    }



    public void save_Game_M() {
        j_File_Handler_Var.save_To_New_File(this.game_Controller_Var.board_Var);
    }

    public void load_Game_M() {
        String string_Save_File_Var = j_File_Handler_Var.read_From_File();
        if (!string_Save_File_Var.equals("")) {
            JsonObject Save_File_Var = new JsonParser().parse(string_Save_File_Var).getAsJsonObject();
            PremadeMaps map_Var = PremadeMaps.get(Save_File_Var.get("boardName").getAsString());
            Board board_Var = new Board(map_Var.getMapArray(), map_Var.getMapName());
            this.game_Controller_Var = new Game_Controller(board_Var);
            this.map_Var = map_Var;
            JsonArray json_Array_Var = Save_File_Var.getAsJsonArray("players");
            for(JsonElement jsonplayer : json_Array_Var) {

                JsonObject temp_Json_Player_Var = jsonplayer.getAsJsonObject();

                Player player_Var = new Player(board_Var, temp_Json_Player_Var.get("color").getAsString(), temp_Json_Player_Var.get("name").getAsString());
                 board_Var.addPlayer(player_Var);
                player_Var.setHeading(Heading.get(temp_Json_Player_Var.get("heading").getAsString()));
                player_Var.setSpace(board_Var.getSpace(
                        temp_Json_Player_Var.get("space").getAsJsonObject().get("x").getAsInt(),
                        temp_Json_Player_Var.get("space").getAsJsonObject().get("y").getAsInt()));
                player_Var.setCheckpoints(temp_Json_Player_Var.get("checkpoints").getAsInt());
                player_Var.setIsInPit(temp_Json_Player_Var.get("isInPit").getAsBoolean());
                player_Var.setSpamCards(temp_Json_Player_Var.get("spamCards").getAsInt());
                 get_Card_And_To_Field_From_Json_M(temp_Json_Player_Var.getAsJsonArray("program"), player_Var, "program");
                get_Card_And_To_Field_From_Json_M(temp_Json_Player_Var.getAsJsonArray("cards"), player_Var, "cards");
            }
             this.game_Controller_Var.board_Var.setPhase(Phase.get(Save_File_Var.get("phase").getAsString()));
            this.game_Controller_Var.board_Var.setCurrentPlayer(this.game_Controller_Var.board_Var.getPlayer(0));
            this.game_Controller_Var.board_Var.setStep(Save_File_Var.get("step").getAsInt());
            robo_Rally_Var.create_Board_View_M(this.game_Controller_Var);
        }
    }
    public void playOnline() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", "Join Game", "Create Game");
        dialog.setTitle("Join or create game");
        dialog.setHeaderText("Select a option");
        Optional<String> result = dialog.showAndWait();
        if (result.get().equals("Create Game")) {
            repository = Repository.getInstance();
            newGame();
        } else if (result.get().equals("Join Game")) {
            repository = Repository.getInstance();
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setContentText("Write the id of the game you want to join");
            inputDialog.showAndWait();


            repository.joinGame(Integer.parseInt(inputDialog.getResult()));

            Board loadedBoard = repository.getGameInstance(null);
            game_Controller_Var = new Game_Controller(loadedBoard);
            game_Controller_Var.addRepository();
            robo_Rally_Var.create_Board_View_M(game_Controller_Var);
            repository.waitingToStart();


        } else {

        }


    }
    private void get_Card_And_To_Field_From_Json_M(JsonArray cards_Json_Var, Player player_Var, String program_Or_Cards_Var) {

        int card_Counter_Var = 0;
        CommandCardField field_Var;
        for (JsonElement cardJson : cards_Json_Var) {
            if (program_Or_Cards_Var.equals("program")) {
                field_Var = player_Var.getProgramField(card_Counter_Var);
            } else {
                field_Var = player_Var.getCardField(card_Counter_Var);
            }
            if (cardJson.getAsJsonObject().get("card") != null) {
                Command saved_Command_Var = Command.get(cardJson.getAsJsonObject().get("card").getAsJsonObject().get("command").getAsString());
                CommandCard saved_Command_Card_Var = new CommandCard(Objects.requireNonNull(saved_Command_Var));
                field_Var.setCard(saved_Command_Card_Var);
            } else {
                field_Var.setCard(null);
            }
            field_Var.setVisible(true);
            card_Counter_Var++;
        }
    }

    public boolean stop_Game() {
        if (game_Controller_Var != null) {

            save_Game_M();

            game_Controller_Var = null;
            robo_Rally_Var.create_Board_View_M(null);
            return true;
        }
        return false;
    }
    public void exit_M() {
        if (game_Controller_Var != null) {
            Alert alert_Var = new Alert(AlertType.CONFIRMATION);
            alert_Var.setTitle("Exit RoboRally?");
            alert_Var.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result_Var = alert_Var.showAndWait();

            if (!result_Var.isPresent() || result_Var.get() != ButtonType.OK) {
                return;
            }
        }

        if (game_Controller_Var == null || stop_Game()) {
            Platform.exit();
        }
    }

    public boolean Check_Game_Running() {
        return game_Controller_Var != null;
    }


    @Override
    public void update_M(Subject subject) {

    }

}
