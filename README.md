# Ship game

## Overview
Collect a full ship before a storm strikes!

This application serves as a learning implementation of a card game designed for kids, featuring ships, storms, coins, and cannons.  
Two players aim to gain a 6 pieces ship of chosen color. A project offers both console and graphical user interface (GUI) versions.  
Choose preferred mode of play based on the provided launchers.

## Gameplay
During a turn, players can draw cards, buy ships, end their turn, or save the game.
The game revolves around collecting ship cards and making strategic decisions, including dealing with storms, buying missing ship pieces and managing resources (coins and cannons).
Both versions of the game (console/GUI) share the same core gameplay mechanics. 
The GUI version, currently in progress, will offer an interface featuring graphical components such as cards, counters, and player groups.

## Game flow
Turn Structure  
On a player's turn, a player choose to draw a card, purchase a ship or pass.

Card Drawing  
When a player draws a ship card, the chosen ship's color becomes the target for collection.
Drawing ship, coin or cannon cards adds them to the player's account.

Storm  
If a storm card is drawn, the player must decide to surrender three cards or sacrifice a cannon.
After resolving the storm event, play passes to the next player.

Ship Purchase  
Once a player accumulates three coins, they can opt to purchase a ship card of their color choice from any player's stack.
The objective is to collect all six ship pieces of the color of the first drawn ship card.

Winning the Game  
The game continues until a player collects a six pieces ship of chosen color, becoming the winner.

## How to run the application
1. Clone or download the repository to your local machine
2. Open the project in your preferred Java IDE
3. Choose the launcher based on your preference
4. Run the main method in DesktopLauncherConsole or use the GUI launcher

## Technologies Used
<li>Java
<li>Event-driven architecture
<li>Console-based user interface
<li>LibGDX framework (for GUI)

## Game Structure
**Launcher:** Initializes the game and starts the gameplay loop  
**Controller:** Manages user input and controls the flow of the game  
**Game:** Represents the game state, including players, stacks of cards, and the main gameplay logic  
**CardFactory:** Generates various types of cards used in the game  
**EventBus:** Implements a simple event system for communication between components  
**JDBC connection classes:** Provides a basic example of connecting to a MySQL database for storing game data  

## GUI Client (work in progress)
Author: https://github.com/xuvei  
The GUI client utilizes the LibGDX framework to create an immersive graphical interface. 
It includes various visual components, such as card actors, collected ship groups, counter actors.

## GUI Client Structure
Author: https://github.com/xuvei  
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

Current visual component sample: 

![image](https://github.com/AkademiaProgramowania/ship-game-libgdx/assets/110561199/cba544fd-f111-40ab-a455-993547518078)





