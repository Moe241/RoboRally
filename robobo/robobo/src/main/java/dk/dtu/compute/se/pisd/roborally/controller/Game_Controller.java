
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.Api.Repository;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Game_Controller {

    final public Board board_Var;
    public Repository repository = null;

    public Game_Controller(@NotNull Board board_Var) {
        this.board_Var = board_Var;
    }

    public void m_Curr_P_To_Space_M(@NotNull Space space_Var)  {
        if (space_Var != null && space_Var.board == board_Var) {
            Player current_Player_Var = board_Var.getCurrentPlayer();
            if (current_Player_Var != null && space_Var.getPlayer() == null) {
                current_Player_Var.setSpace(space_Var);
                int player_Number_Var = (board_Var.getPlayerNumber(current_Player_Var) + 1) % board_Var.getPlayersNumber();
                board_Var.setCurrentPlayer(board_Var.getPlayer(player_Number_Var));
            }
        }
    }

    public boolean is_Wall_M(Space space_Var, Heading heading_Var) {
        switch (heading_Var) {
            case NORTH, SOUTH -> {
                return space_Var.getType().BorderHeadings.contains(Heading.NORTH) || space_Var.getType().BorderHeadings.contains(Heading.SOUTH);
            }
            case EAST, WEST -> {
                return space_Var.getType().BorderHeadings.contains(Heading.EAST) || space_Var.getType().BorderHeadings.contains(Heading.WEST);
            }
            default -> {
                return false;
            }
        }
    }

    public boolean is_Out_Of_Map_M(Space space_Var, Heading heading_Var) {
        switch (heading_Var) {
            case NORTH -> {
                return space_Var.y >= this.board_Var.height - 1;
            }
            case SOUTH -> {
                return space_Var.y <= 0;
            }
            case EAST -> {
                return space_Var.x <= 0;
            }
            case WEST -> {
                return space_Var.x >= this.board_Var.width - 1;
            }
            default -> {
                return false;
            }
        }
    }

    public void player_Shoot_Laser_M(Player player_Var) {
        Space player_Space_Var = player_Var.getSpace();
        Heading player_Heading_Var = player_Var.getHeading();
        Space neighbor_Space_Var = player_Space_Var.board.getNeighbour(player_Space_Var, player_Heading_Var);
        while (true) {
            if(is_Out_Of_Map_M(neighbor_Space_Var, player_Heading_Var)) {
                System.out.println("Laser reached outside of map");
                break;
            }
            if(neighbor_Space_Var.isPlayerOnSpace()) {
                System.out.println("Hit " + neighbor_Space_Var.getPlayer() + " and added 1 spam card!");
                neighbor_Space_Var.getPlayer().addSpamCards(1);
                break;
            }
            if(is_Wall_M(neighbor_Space_Var, player_Heading_Var)) {
                System.out.println("PLAYER LASER HIT A WALL");
                break;
            }
            neighbor_Space_Var = neighbor_Space_Var.board.getNeighbour(neighbor_Space_Var, player_Heading_Var);
        }
    }

    public void start_Programming_Phase() {
        board_Var.setPhase(Phase.PROGRAMMING);
        board_Var.setCurrentPlayer(board_Var.getPlayer(0));
        board_Var.setStep(0);
        spawn_Players();

        for (int i = 0; i < board_Var.getPlayersNumber(); i++) {
            Player player_Var = board_Var.getPlayer(i);
            Command[] commands_Var = Command.values();
            if (player_Var != null) {
                for (int j = 0; j < Player.NO_REGISTERS_Var; j++) {
                    CommandCardField field_Var = player_Var.getProgramField(j);
                    field_Var.setCard(null);
                    field_Var.setVisible(true);
                }
                for (int j = 0; j < player_Var.getSpamCards(); j++) {
                    CommandCardField field_Var = player_Var.getCardField(j);
                    field_Var.setCard(new CommandCard(commands_Var[5]));
                    field_Var.setVisible(true);
                }
                for (int j = player_Var.getSpamCards(); j < Player.NO_CARDS_VAr; j++) {
                    CommandCardField field_Var = player_Var.getCardField(j);
                    field_Var.setCard(generate_Random_Command_Card());
                    field_Var.setVisible(true);
                }
            }
        }
    }

    private CommandCard generate_Random_Command_Card() {
        Command[] commands_Var = Command.values();
        int random_var = (int) (Math.random() * commands_Var.length - 1);
        return new CommandCard(commands_Var[random_var]);
    }

    public void f_Program_Phase_m() {
        make_P_Field_Invisible_M();
        make_PField_Visible_M(0);
        board_Var.setPhase(Phase.ACTIVATION);
        board_Var.setCurrentPlayer(board_Var.getPlayer(0));
        board_Var.setStep(0);
    }

    private void make_PField_Visible_M(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS_Var) {
            for (int i = 0; i < board_Var.getPlayersNumber(); i++) {
                Player player_Var = board_Var.getPlayer(i);
                CommandCardField field_Var = player_Var.getProgramField(register);
                field_Var.setVisible(true);
            }
        }
    }


    private void make_P_Field_Invisible_M() {
        for (int i = 0; i < board_Var.getPlayersNumber(); i++) {
            Player player_Var = board_Var.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS_Var; j++) {
                CommandCardField field_var = player_Var.getProgramField(j);
                field_var.setVisible(false);
            }
        }
    }


    public void execute_Program() {
        board_Var.setStepMode(false);
        continue_Programs_M();
    }


    public void execute_Step_M() {
        board_Var.setStepMode(true);
        continue_Programs_M();
    }

    private void continue_Programs_M() {
        do {
            execute_Next_Step_M();
        } while (board_Var.getPhase() == Phase.ACTIVATION && !board_Var.isStepMode());
    }

    public void next_Player_M() {
        Player current_Player_Var = board_Var.getCurrentPlayer();
        int step_Var = board_Var.getStep();
        int next_Player_Number_Var = board_Var.getPlayerNumber(current_Player_Var) + 1;
        if (next_Player_Number_Var < board_Var.getPlayersNumber()) {
            board_Var.setCurrentPlayer(board_Var.getPlayer(next_Player_Number_Var));
        } else {
            step_Var++;
            if (step_Var < Player.NO_REGISTERS_Var) {
                make_PField_Visible_M(step_Var);
                board_Var.setStep(step_Var);
                board_Var.setCurrentPlayer(board_Var.getPlayer(0));
            } else {
                start_Programming_Phase();
            }
        }
    }
    public void addRepository() {
        this.repository = Repository.getInstance();

    }

    private void execute_Next_Step_M() {
        Player current_Player_Var = board_Var.getCurrentPlayer();
        int step_var = board_Var.getStep();
        if (board_Var.getPhase() == Phase.ACTIVATION && current_Player_Var != null && step_var >= 0 && step_var < Player.NO_REGISTERS_Var) {
            CommandCard card_Var = current_Player_Var.getProgramField(step_var).getCard();
            if (!current_Player_Var.getIsInPit()){
                if (card_Var != null) {
                    Command command_Var = card_Var.command;
                    execute_Command_M(current_Player_Var, command_Var);
                }
            }

            System.out.println(board_Var.getPlayersNumber() + " " + board_Var.getPlayerNumber(current_Player_Var));
            if ((board_Var.getPlayerNumber(current_Player_Var)+1) == board_Var.getPlayersNumber()) {
                execute_Board_Elements_M();
            }
            if (board_Var.getPhase() != Phase.PLAYER_INTERACTION) {
                next_Player_M();
            }

        } else {
            assert false;
        }
        if (check_For_Winner()) {
            this.board_Var.setPhase(Phase.WINNER);
        }
    }


    public void execute_Board_Elements_M() {
        for (Player player : board_Var.getPlayers()) {
            if(this.board_Var.isSpaceTypeLaser(player.getSpace().getType())) {

            } else {
                player.getSpace().ex_FieldAction_M(this);
            }
        }
        for (int i = 0; i < this.board_Var.getSpaces().length; i++) {
            for (int j = 0; j < this.board_Var.getSpaces()[i].length; j++) {
                Space current_Space_Var = this.board_Var.getSpace(i,j);
                if(this.board_Var.isSpaceTypeLaser(current_Space_Var.getType())) {
                    this.board_Var.getSpaces()[i][j].ex_FieldAction_M(this);
                }
            }
        }
        for (int i = 0; i < this.board_Var.getPlayersNumber(); i++) {
            player_Shoot_Laser_M(board_Var.getPlayer(i));
        }
    }

    public void spawn_Players() {
        for (Space spawnSpace : board_Var.getSpawnSpaces()) {
            spawnSpace.ex_FieldAction_M(this);
        }
    }

    public void execute_Command_M(@NotNull Player player, Command command_Var) {
        if (player != null && player.board == board_Var && command_Var != null) {
            switch (command_Var) {
                case FORWARD:
                    this.move_Forward_m(player);
                    break;
                case RIGHT:
                    this.turn_Right_M(player);
                    break;
                case LEFT:
                    this.turn_Left_M(player);
                    break;
                case FAST_FORWARD:
                    this.fast_Forward_M(player);
                    break;
                case SPAM:
                    this.SPAM_M(player);
                    break;
                default:
                    this.board_Var.setPhase(Phase.PLAYER_INTERACTION);
            }
        }
    }

    private boolean check_For_Winner() {
        for (Player player : board_Var.getPlayers()) {
            if (player.getCheckpoints() >= board_Var.getAmountOfCheckpoints()) {
                board_Var.setWinner(player);
                return true;
            }
        }
        return false;
    }

    public void move_Forward_m(@NotNull Player player) {
        Space space_vAR = player.getSpace();
        Heading heading_vAR = player.getHeading();
        Space target_Var = board_Var.getNeighbour(space_vAR, heading_vAR);
        if (target_Var != null && player != null && player.board == board_Var && space_vAR != null &&
                !space_vAR.getType().BorderHeadings.contains(heading_vAR) &&
                !target_Var.getType().BorderHeadings.contains(reverse_Heading_M(heading_vAR))) {
            Player target_Player_Var = target_Var.getPlayer();
            if (target_Player_Var == null) {
                target_Var.setPlayer(player);
            } else {
                Space push_Space_var = board_Var.getNeighbour(target_Var, heading_vAR);
                if (push_Space_var != null && !target_Var.getType().BorderHeadings.contains(heading_vAR) &&
                        !push_Space_var.getType().BorderHeadings.contains(reverse_Heading_M(heading_vAR))) {
                    push_Space_var.setPlayer(target_Player_Var);
                    target_Var.setPlayer(player);
                }
            }
        }
    }


    public Heading reverse_Heading_M(Heading heading_Var) {
        return switch (heading_Var) {
            case NORTH -> Heading.SOUTH;
            case SOUTH -> Heading.NORTH;
            case EAST -> Heading.WEST;
            case WEST -> Heading.EAST;
        };
    }


    public void fast_Forward_M(@NotNull Player player) {
        move_Forward_m(player);
        move_Forward_m(player);
    }

    public void turn_Right_M(@NotNull Player player) {
        if (player != null && player.board == board_Var) {
            player.setHeading(player.getHeading().next());
        }
        board_Var.setPhase(Phase.ACTIVATION);
    }

    public void turn_Left_M(@NotNull Player player) {
        if (player != null && player.board == board_Var) {
            player.setHeading(player.getHeading().prev());
        }
        board_Var.setPhase(Phase.ACTIVATION);
    }

    public void SPAM_M(@NotNull Player player) {
        Random ra_Var = new Random();
        int randomNum = ra_Var.nextInt(5);
        player.decSpamCards();
        switch (randomNum) {
            case 0:
                fast_Forward_M(player);
                break;
            case 1:
                turn_Right_M(player);
                break;
            case 2:
                turn_Left_M(player);
                break;
            case 3:
                move_Forward_m(player);
                break;
            case 4:
                board_Var.setPhase(Phase.PLAYER_INTERACTION);
                break;
            default:
                System.out.println("");
        }
    }


    public boolean move_Cards_M(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard source_Card_Var = source.getCard();
        CommandCard target_Card_Var = target.getCard();
        if (source_Card_Var != null && target_Card_Var == null) {
            target.setCard(source_Card_Var);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }



}
