# Ship game application

## Overview
Collect a full ship before a storm attacks!

This application is a trainingg implementation of a simple card game involving ships, storms, coins, and cannons. 
Players take turns drawing cards, making strategic decisions and collecting ships. 
This project offers both console and graphical user interface (GUI) versions, choose preferred mode of play based on the provided launchers.

## Gameplay
Players can start a new game or restore a saved one.
During a turn, players can draw cards, buy ships, end their turn, or save the game
The game involves collecting ship cards and making strategic decisions, such as dealing with storms and managing resources (coins and cannons)
Both versions of the game share the same core gameplay mechanics, involving ship collection, resource management, and strategic decision-making. 
The GUI version (in progress) will provide an interface with graphical components such as cards, counters, and player groups.

## Game flow
Turn Structure  
On a player's turn, a player can either draw a card, purchase a ship or pass

Card Drawing  
First drawn ship card - this ship color is to be collected.
If drawn ship, coin or cannon, cards are added to the player's account.

Storm  
If a storm card is drawn, the player must choose to surrender three cards or sacrifice a cannon.
After resolving the storm event, play passes to the next player.

Ship Purchase  
Once a player accumulates three coins, he can choose to purchase a ship card of his color choice from any player's stack.
A goal is to collect all 6 ship pieces of a color of first drawn ship card.

Winning the Game  
The game continues until a player collects a 6 pieces ship of chosen color, becoming the winner.

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

Current GUI sample: 

![image](https://github.com/AkademiaProgramowania/ship-game-libgdx/assets/110561199/cba544fd-f111-40ab-a455-993547518078)





