package dk.dtu.compute.se.pisd.roborally.model.Templates;

import dk.dtu.compute.se.pisd.roborally.model.Player;

    public class PlayerServerTemp {
        public String name;

        private BoardServerTemp board; // Add a board property

        public void setBoard(BoardServerTemp board) {
            this.board = board;
        }

        public Player toPlayer() {
            // Create a new Player object with the required parameters
            Player player = new Player(board.toBoard(), null, name);

            return player;
        }}

