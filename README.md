# Ship game application

## overview
Collect a full ship before a storm attack!
This console application is an implementation of a simple card game involving ships, storms, coins, and cannons. 
Players take turns drawing cards, making strategic decisions and collecting ships. The game is designed to be run in a console environment.

## How to play
This project offers both console and graphical user interface (GUI) versions of the Ship Game 
User can choose preferred mode of play based on the provided launchers

1. Choose the launcher based on your preference
2. Clone or download the repository to your local machine
3. Open the project in your preferred Java IDE
4. Locate the DesktopLauncherConsole class
5. Run the main method in DesktopLauncherConsole

## Gameplay
Players can start a new game or restore a saved one
During a turn, players can draw cards, buy ships, end their turn, or save the game
The game involves collecting ship cards and making strategic decisions, such as dealing with storms and managing resources (coins and cannons)

## GUI Client (work in progress) - author: 
The GUI client utilizes the LibGDX framework to create an immersive graphical interface. 
It includes various visual components, such as card actors, collected ship groups, counter actors, enhancing the overall gaming experience

## Technologies Used
<li>Java
<li>Event-driven architecture
<li>Console-based user interface
<li>LibGDX framework (for GUI)

## Game Structure
Launcher: Initializes the game and starts the gameplay loop
Controller: Manages user input and controls the flow of the game
Game: Represents the game state, including players, stacks of cards, and the main gameplay logic
CardFactory: Generates various types of cards used in the game
EventBus: Implements a simple event system for communication between components
JDBC: Provides a basic example of connecting to a MySQL database for storing game data

## GUI Client Structure
In addition to the console application, there is a basic work-in-progress GUI client to enhance the gaming experience. 
The GUI client includes graphical components, such as:

<li>CardActor: Represents a card in the GUI, displaying textures and handling rendering
<li>CollectedShipGroup: Displays the ships collected by a player in a visually appealing format
<li>CounterActor: Visual representation of counters for different resources
<li>CounterGroup: Groups counters for efficient rendering and display
<li>GameScreen: Utilizes the LibGDX framework to create the graphical game interface, including players, ships, resources, and the stack
<li>GUIParams: Stores constants for GUI dimensions
<li>PlayerGroup: Groups the collected ships, resources, and trade groups for each player
<li>StackGroup: Represents the stack of cards in the GUI


