# RoboRally Board Game

Welcome to the **RoboRally** digital board game project! This project is a digital recreation of the popular strategy board game **RoboRally**, where players program robots to navigate a complex board filled with obstacles and hazards. The project was initially developed as a single-player game and later enhanced with multiplayer functionality using a RESTful API, allowing multiple players to participate in real-time.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [How to Play](#how-to-play)
- [Multiplayer via REST API](#multiplayer-via-rest-api)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **Digital version of RoboRally**: A complete digital transformation of the classic board game.
- **Single-player and multiplayer modes**: Play against AI or other players in real-time.
- **RESTful API support**: Allows for smooth multiplayer gameplay with state persistence and real-time communication between clients.
- **Program robot moves**: Players program a series of movements, turns, and actions to navigate the game board.
- **Board hazards**: Includes game elements such as conveyor belts, lasers, and pits.
- **Turn-based gameplay**: All players program their robots simultaneously and watch the action unfold.
- **Persistent game states**: Game state is saved and can be resumed in future sessions.

## Technologies

- **Java**: Core language for the entire project, including game logic, API development, and multiplayer functionality.
- **Spring Boot**: Framework used for building the RESTful API.
- **RESTful API**: Enables multiple players to interact with the same game in real-time.
- **JavaFX**: For creating the graphical user interface (GUI) to visualize the game board.
- **H2/PostgreSQL/MySQL**: Used for storing game states, player information, and session data.
- **Maven/Gradle**: For project management and dependency handling.

## Getting Started

To get a local copy up and running, follow these steps.

### Prerequisites

- Java 11 or newer ([Download here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html))
- Maven or Gradle for building the project
- A relational database like PostgreSQL/MySQL or an embedded database like H2

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/RoboRally.git
   cd RoboRally
   ```

2. **Set up the database:**

   Depending on the database you choose (H2, PostgreSQL, or MySQL), configure the database connection in `application.properties` (or `application.yml`) under the `src/main/resources` directory. Example for PostgreSQL:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/roborally
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the project:**
   If using Maven:
   ```bash
   mvn clean install
   ```
   Or with Gradle:
   ```bash
   ./gradlew build
   ```

4. **Run the server:**
   ```bash
   java -jar target/roborally-0.0.1-SNAPSHOT.jar
   ```

5. **Start the game client:**
   The client uses JavaFX for the graphical interface. You can launch it using:
   ```bash
   java -cp target/roborally-0.0.1-SNAPSHOT.jar com.roborally.Main
   ```

## How to Play

In **RoboRally**, you control a robot that needs to navigate through various board elements to reach a specific goal. Each player programs their robot using a sequence of commands that determine how the robot moves during its turn. After all players have submitted their commands, the game plays out the round.

### Basic Gameplay:

1. **Set up a game session**: Either create a single-player game or join/create a multiplayer session.
2. **Program your robot**: Select a sequence of moves (forward, backward, turn left, turn right, etc.).
3. **Watch the round**: After all players have programmed their robots, the game round plays out simultaneously for all players.
4. **Hazards**: Robots may encounter conveyor belts, pits, and lasers as they move across the board.
5. **Winning the game**: The player who reaches the goal first, or the last robot standing, wins the game!

## Multiplayer via REST API

The RESTful API enables real-time multiplayer gameplay by facilitating communication between clients and the server. Each player’s moves and board state are handled via HTTP requests, and the API ensures synchronization and consistency between players.

### Multiplayer Features:
- Create or join game sessions.
- Program moves for your robot via API calls.
- Retrieve the current game state, including other players' positions and board elements.
- Automated turn-based system that processes actions after all players have submitted their moves.

## API Documentation

### Endpoints

- `POST /api/games`: Create a new game session.
- `GET /api/games/{gameId}`: Retrieve the current state of a game session.
- `POST /api/games/{gameId}/players`: Join a game session.
- `POST /api/games/{gameId}/players/{playerId}/moves`: Submit your moves for the current turn.
- `GET /api/games/{gameId}/players/{playerId}`: Get player-specific game data (robot position, health, etc.).

Detailed API documentation is available [here](path/to/api-docs).

## Project Structure

The project follows a modular structure for clarity and separation of concerns.

```
src/
├── main/
│   ├── java/com/roborally
│   │   ├── controller/      # REST API controllers
│   │   ├── model/           # Game models (robots, board elements, etc.)
│   │   ├── repository/      # Repositories for database access
│   │   ├── service/         # Game logic and services
│   │   └── Main.java        # Application entry point
│   └── resources/
│       ├── static/          # Static assets (if any)
│       ├── templates/       # HTML templates (if using Thymeleaf)
│       └── application.properties  # Configuration for the Spring Boot application
└── test/                    # Unit and integration tests
```

## Contributing

Contributions are welcome! To contribute:

1. Fork the project.
2. Create a new feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

Please make sure your code is well-documented and includes tests where appropriate.

