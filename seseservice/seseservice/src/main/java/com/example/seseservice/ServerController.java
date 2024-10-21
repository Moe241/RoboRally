package com.example.seseservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@RestController
public class ServerController {
    ArrayList<Game> games = new ArrayList<>();
    private final String gameSavePath ="src/main/resources/gamesavesonline";
    int currId = 10;
    @PostMapping("/game/savegame/{gamename}")
    public void saveGame(@PathVariable String gamename,  @RequestBody String gameInstance) {

        saveStringToFile(gameInstance,gamename+".json");

    }

    public ResponseEntity<Object> saveStringToFile(String content, String fileName) {

        try {
            // If it does not exist create the folder
            File folder = new File(gameSavePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Writes the games data to file
            String filePath = gameSavePath + "/" + fileName;
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();

            return ResponseEntity.status(HttpStatus.CREATED).build(); // Returns HTTP 201 for successful
        } catch (IOException e) {
            // For any error while writing
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Returns HTTP 500 Internal Server Error for error
        }
    }
    @GetMapping("/game/files/{name}")
    public String getFileFromName(@PathVariable String name)
    {
        try {
            byte[] fileBytes = Files.readAllBytes(Path.of(gameSavePath+"/"+name));
            return new String(fileBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Handle potential IO exceptions
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/game/files")
    public String getFileNames()
    {
        String files="";
        File folder = new File(gameSavePath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                files = files+file.getName()+",";
            }
        }
        return files;
    }
    @GetMapping("/game/{id}/statpost/{player}")
    public String getGameStatusPost(@PathVariable final String id,@PathVariable final String player) {
        //This one does not work
        Game exists = gameExist(id);
        if (exists != null) {
            if (exists.ReadyProgramming(player) == true) {
                return "Ready";
            } else {
                return "not";
            }
        }
        return "game dosent exist";
    }
    @GetMapping("/game/{id}/statact/{player}")
    public String getGameStatusAct(@PathVariable final String id,@PathVariable final String player) {
        //This one does not work
        Game exists = gameExist(id);
        if (exists != null) {
            if (exists.ReadyActivation(player) == true) {
                return "Ready";
            } else {
                return "not";
            }
        }
        return "game dosent exist";
    }
    @GetMapping("/game/{id}/players")
    public String getGameStatusAct(@PathVariable final String id) {
        //This one does not work
        Game exists = gameExist(id);
        if (exists != null) {
            String toReturn = exists.players+","+exists.start;
            return toReturn;
        }
        return null;

    }
    @PostMapping("/game/{id}/start")
    public void postStart(@PathVariable final String id,@RequestBody String start) {
        Game exists = gameExist(id);
        if (exists != null&&start.equals("true")) {
            exists.start=true;
        }
        else if (exists != null&&start.equals("false"))
        {
            exists.start =false;
        }
    }
    @PostMapping("/game/{id}")
    public void postGameInstanceStartPhase(@PathVariable final String id,@RequestBody String gameInstance)
    {
        Game exists = gameExist(id);
        if (exists == null) {

        }
        else
        {
            exists.setJsonFile(gameInstance);
        }
    }
    @PostMapping("/game/{id}/step/{player}")
    public void postGameInstanceExcecuteStep(@PathVariable final String id,@PathVariable final  String player, @RequestBody String gameInstance)
    {

        Thread thread = new Thread(() -> {
            // Lengthy operation
            executeStepIfAllHaveRecivedStatus(id,player,gameInstance);
        });
        thread.start();

    }
    public void executeStepIfAllHaveRecivedStatus(String id, String player, String gameInstance)
    {

        Game exists = gameExist(id);
        if (exists!= null)
        {
            while (true)
            {
                if (exists.lastPlayers.size() == exists.maxPlayers||exists.lastPlayers.size()==0)
                {
                    exists.resetTheLastPlayers();
                    break;
                }
            }
            System.out.println("Recieved board from "+player);

            exists.addTheLastPlayers(player);
            exists.readyAct=true;
            exists.setJsonFile(gameInstance);
        }
    }
    @PostMapping("/game/{id}/prog/{player}")
    public void postGameInstanceProgrammingFase(@PathVariable final String id,@PathVariable final String player,@RequestBody String gameInstance)
    {
        Game exists = gameExist(id);
        if (exists == null) {

        }
        else
        {
            exists.resetTheLastPlayers();
            exists.setThePlayersPosted(Integer.parseInt(player));
            exists.setJsonFile(gameInstance);
        }
    }
    @GetMapping("/game/{id}")
    public String getGameInstance(@PathVariable final String id)
    {
        Game exists = gameExist(id);

        if (exists != null) {
            return exists.getJsonFile();
        }
        else
        {
            return "Game dosent exist";
        }


    }

    @GetMapping("/game/{id}/join")
    public String getGamePlayerNumb(@PathVariable final String id)
    {
        Game exists = gameExist(id);

        if (exists!=null) {
            int players = exists.getPlayers();
            if (players + 1 > exists.maxPlayers) {
                return "Game is full";
            } else {
                exists.setPlayers(players + 1);
                return Integer.toString(players + 1);
            }

        }
        else
        {
            return "Game dosent exist";
        }



    }
    @GetMapping("/game/id/{maxPlayers}")
    public String createGame(@PathVariable final String maxPlayers){
        int id =currId;
        currId = currId+1;
        System.out.println(id);
        System.out.println(maxPlayers);
        Game newgame = new Game(Integer.toString(id),Integer.parseInt(maxPlayers));
        games.add(newgame);
        return Integer.toString(id);
    }

    public Game gameExist(String id)
    {
        Game exists = null;
        for (Game game : games)
        {
            if (game.getId().equals(id))
            {
                exists =game;
                break;
            }
        }
        return exists;
    }

}
