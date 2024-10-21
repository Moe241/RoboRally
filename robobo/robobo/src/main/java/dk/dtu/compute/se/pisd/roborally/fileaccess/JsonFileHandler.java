package dk.dtu.compute.se.pisd.roborally.fileaccess;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import javafx.scene.control.ChoiceDialog;
import java.io.*;
import java.util.Optional;

public class JsonFileHandler {

    private final Gson Gson_Var = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public JsonFileHandler() {
    }

    public void save_To_New_File(Board board) {
        String JSONString_var = Gson_Var.toJson(board);
        String[] save_Files_Var = new String[]{"Save 1", "Save 2", "Save 3", "Save 4", "Save 5"};
        ChoiceDialog<String> saveFileDialog = new ChoiceDialog<>(save_Files_Var[0], save_Files_Var);
        saveFileDialog.setTitle("Save current game");
        saveFileDialog.setHeaderText("Which save do you want to save your game on.");
        Optional<String> save_File_Name_Option_Var = saveFileDialog.showAndWait();
        if (save_File_Name_Option_Var.isPresent()) {
            String save_File_Name_Var = save_File_Name_Option_Var.get();
            String Save_File_Path_Var = save_File_Switch_M(save_File_Name_Var);
            write_To_JSON_File_M(JSONString_var, Save_File_Path_Var);
        }
    }


    public String read_From_File() {
        String JSONString_Var = "";
        String[] save_Files_Var = new String[]{"Save 1", "Save 2", "Save 3", "Save 4", "Save 5"};
        ChoiceDialog<String> saveFileDialog = new ChoiceDialog<>(save_Files_Var[0], save_Files_Var);
        saveFileDialog.setTitle("Load from save file");
        saveFileDialog.setHeaderText("Which save game do you want to load");
        Optional<String> save_File_Name_Option_Var = saveFileDialog.showAndWait();
        if (save_File_Name_Option_Var.isPresent()) {
            String saveFileName = save_File_Name_Option_Var.get();
            String SaveFilePath = save_File_Switch_M(saveFileName);
            JSONString_Var = read_From_JSON_File_M(SaveFilePath);
        }
        return JSONString_Var;
    }
    String save_File_Switch_M(String NameOption_Var) {
        String saveFile1 = "src/main/java/dk/dtu/compute/se/pisd/SaveFiles/SaveFile1.json";
        String saveFile2 = "src/main/java/dk/dtu/compute/se/pisd/SaveFiles/SaveFile2.json";
        String saveFile3 = "src/main/java/dk/dtu/compute/se/pisd/SaveFiles/SaveFile3.json";
        String saveFile4 = "src/main/java/dk/dtu/compute/se/pisd/SaveFiles/SaveFile4.json";
        String saveFile5 = "src/main/java/dk/dtu/compute/se/pisd/SaveFiles/SaveFile5.json";
        String SaveFilePath = saveFile1;
        switch (NameOption_Var) {
            case "Save 1" -> SaveFilePath = saveFile1;
            case "Save 2" -> SaveFilePath = saveFile2;
            case "Save 3" -> SaveFilePath = saveFile3;
            case "Save 4" -> SaveFilePath = saveFile4;
            case "Save 5" -> SaveFilePath = saveFile5;
        }
        return SaveFilePath;
    }


    static void write_To_JSON_File_M(String JSON_String_Var, String write_File_Path_Var) {
        try {
            try (FileWriter file_Writer_Var = new FileWriter(write_File_Path_Var)) {
                file_Writer_Var.write(JSON_String_Var);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    static String read_From_JSON_File_M(String Path_Var) {
        String JSON_String_Var;
        try (FileReader file_Reader_Var = new FileReader(Path_Var)){

            BufferedReader buffered_Reader_Var = new BufferedReader(file_Reader_Var);
            JSON_String_Var = buffered_Reader_Var.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return JSON_String_Var;
    }

}
