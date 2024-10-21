package dk.dtu.compute.se.pisd.roborally.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dk.dtu.compute.se.pisd.roborally.fieldActions.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Templates.BoardServerTemp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class Client {
    private static final String API_BASE_URL = "http://localhost:8080/game"; // Replace with your API's base URL so the ip4 for your server http://[IP]:8080/game

    public static Board LoadGameInstance(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = gsonBuilder.create();

        try (JsonReader reader = new JsonReader(new StringReader(jsonString))) {
            BoardServerTemp template = gson.fromJson(reader, BoardServerTemp.class);
            return template.toBoard();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


   public static String GetGameInstance(Board board) {
        BoardServerTemp template = new BoardServerTemp().fromBoard(board);

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>())
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(template);
    }


    public String DataToAPI(String API_BASE_URL, String jsonData) {
        try {

            // send POST to API with JSON content
            URL url = new URL(API_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // JSON content for body
            try (OutputStream outputStream = connection.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                writer.write(jsonData);
            }

            // response from API
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // process response
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                return response.toString();
            } else {
                // for error response
                System.out.println("Error response: " + responseCode);
                return Integer.toString(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStatusProg(int id, int playernumb) {
        return getDataFromApi("GET", API_BASE_URL + "/" + id + "/statpost/" + playernumb);

    }

    public String getFileFromName(String fileName) {
        return getDataFromApi("GET", API_BASE_URL + "/files/" + fileName);
    }

    public String[] getStart(int id) {
        String data = getDataFromApi("GET", API_BASE_URL + "/" + id + "/players");
        String[] datas = data.split(",");
        return datas;
    }

    public void setStart(int id) {
        DataToAPI(API_BASE_URL + "/" + id + "/" + "start", "true");

    }

    public String[] getFilesString() {
        String data = getDataFromApi("GET", API_BASE_URL + "/files");
        String[] files = data.split(",");
        return files;
    }

    public String getStatusAct(int id, int playernumb) {
        return getDataFromApi("GET", API_BASE_URL + "/" + id + "/statact/" + playernumb);
    }

    public void postGameInstanceActivationPhase(int id, int playerNumb, String jsonData) {
        DataToAPI(API_BASE_URL + "/" + id + "/step/" + playerNumb, jsonData);
    }

    public void postGameInstanceProgrammingPhase(int id, String jsonData, int playerNumb) {
        DataToAPI(API_BASE_URL + "/" + id + "/prog/" + playerNumb, jsonData);
    }

    public void postGameInstance(int id, String jsonData) {
        DataToAPI(API_BASE_URL + "/" + id, jsonData);
    }

    public void postGameSaveInstance(String jsonData, String gameName) {
        DataToAPI(API_BASE_URL + "/savegame/" + gameName, jsonData);
    }

    public int CreateGameInstance(int maxNumbOfPlayers) {
        return Integer.parseInt(getDataFromApi("GET", API_BASE_URL + "/id/" + maxNumbOfPlayers));
    }

    public String getGameInstance(int id) {
        String data = getDataFromApi("GET", API_BASE_URL + "/" + id);
        return data;
    }

    //returns player number you are
    public int joinGame(int id) {
        return Integer.parseInt(getDataFromApi("GET", API_BASE_URL + "/" + id + "/join"));
    }

    public String getDataFromApi(String request, String API_BASE_URL) {
        try {
            // get request for game state
            URL url = new URL(API_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request);

            // responce from api
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // process response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);

                }
                reader.close();
                return response.toString();

            } else {
                // for error respone
                System.out.println("Error response: " + responseCode);
                return Integer.toString(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

