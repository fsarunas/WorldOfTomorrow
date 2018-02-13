import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  This class is the main class of the "World of Tomorrow" game. 
 *  "World of Tomorrow" is a text based adventure game - with a twist;
 *  there is a GUI! 
 *  
 *  Users can walk around, talk to characters and interact with objects.
 * 
 *  This class creates and initialises all the others: it creates all
 *  rooms, characters, items, creates the parser and starts the game. 
 *  It also evaluates and executes the commands that the parser returns.
 * 
 * @author  Sarunas Djacenko
 * @version 2017.12.08
 */

public class Game 
{
    private GUI gui;
    private Parser parser;
    private Player player[];
    private Room room[], currentRoom, nextRoom, previousRoom;
    private Item item[];
    private Stack roomHistory = new Stack();
    private boolean fullscreenMode, isCreated, canUsePortal;
    private boolean bankerHS, chefHS, salesmanHS, scientistHS1, scientistHS2, thiefHS;
    private boolean childStory, guardStory, librarianStory;
    private long startTime, endTime;

    /**
     * Create the GUI, and print a Welcome message, asking the player
     * what they would like to be called.
     */
    public Game() 
    {
        gui = new GUI(this);
        printWelcome(); 
    }

    /**
     * Sets up the game by creating the parser, initialising the rooms
     * with characters and items. Updates the GUI frame and prints info.
     * A start time is also recorded, to later tell the user how long
     * they took to finish the game.
     */
    private void createGame(String text)
    {
        parser = new Parser();
        createRooms(text);
        gui.updateFrame(currentRoom);
        printInfo();
        startTime = System.nanoTime();
    }

    /**
     * Create all the rooms and link their exits together.
     * Creates charcters and items, and puts them into rooms.
     * Makes the player enter the first room.
     */
    private void createRooms(String playerName)
    {
        // create the rooms
        // parameters: room name, room image location.
        room = new Room[12];
        room[0] = new Room("Physics Lab", "images/rooms/physlab.png");
        room[1] = new Room("Hallway (Lower Ground)", "images/rooms/hallwaylower.png");
        room[2] = new Room("Computing Lab", "images/rooms/complab.png");
        room[3] = new Room("Library", "images/rooms/library.png");
        room[4] = new Room("Hallway (South Side)", "images/rooms/hallwaysouth.png");
        room[5] = new Room("Lobby", "images/rooms/lobby.png");
        room[6] = new Room("Garden", "images/rooms/garden.png");
        room[7] = new Room("Shop", "images/rooms/shop.png");
        room[8] = new Room("Hallway (North Side)", "images/rooms/hallwaynorth.png");
        room[9] = new Room("Cafeteria", "images/rooms/cafeteria.png");
        room[10] = new Room("Bank", "images/rooms/bank.png");
        room[11] = new Room("", "images/other/winscreen.png"); // win room

        // create the items and put some of them into rooms
        // parameters: item name, item description, item weight
        item = new Item[9];
        item[0] = new Item("Fuel", "strange looking, neon blue fuel", 50);
        item[1] = new Item("Machine", "", 100); //cannot pickup, maxWeight = 99
        item[2] = new Item("Ball", "a football", 30);
        item[3] = new Item("Book", "a heavy book", 35);
        item[4] = new Item("Cheese", "a fragrant block of cheese", 25);
        item[5] = new Item("Flashlight", "a bright flashlight", 15);
        item[6] = new Item("Food", "a mouth-watering pizza", 30);
        item[7] = new Item("Money", "a large amount of money", 15);
        item[8] = new Item("Wallet", "a lost wallet", 20);
        
        room[0].addItem(item[1]);
        room[2].addItem(item[2]);
        room[9].addItem(item[3]);
        room[7].addItem(item[4]);
        room[1].addItem(item[5]);
        
        // create the characters and put them into rooms
        // Parameters: Name, Image, ItemWanted, ItemOwned1, ItemOwned2
        player = new Player[9];
        player[0] = new Player(playerName, "images/characters/player.png", null, null, null);
        player[1] = new Player("Banker", "images/characters/banker.png", item[8], null, null);
        player[2] = new Player("Chef", "images/characters/chef.png", item[4], null, null);
        player[3] = new Player("Child", "images/characters/child.png", item[2], null, null);
        player[4] = new Player("Guard", "images/characters/guard.png", item[5], null, null);
        player[5] = new Player("Librarian", "images/characters/librarian.png", item[3], null, null);
        player[6] = new Player("Salesman", "images/characters/salesman.png", item[7], null, item[0]);
        player[7] = new Player("Scientist", "images/characters/scientist.png", item[0], null, null);
        player[8] = new Player("Thief", "images/characters/thief.png", item[6], null, item[8]);
        
        room[10].addPlayer(player[1]);
        room[9].addPlayer(player[2]);
        room[6].addPlayer(player[3]);
        room[10].addPlayer(player[4]);
        room[3].addPlayer(player[5]);
        room[7].addPlayer(player[6]);
        room[0].addPlayer(player[7]);
        room[4].addPlayer(player[8]);
        
        // initialise room exits
        // lower ground floor 
        room[0].setExit("east", room[1]);

        room[1].setExit("east", room[2]);
        room[1].setExit("west", room[0]);
        room[1].setExit("upstairs", room[4]);

        room[2].setExit("west", room[1]);

        // ground floor 
        room[3].setExit("east", room[4]);

        room[4].setExit("north", room[8]);
        room[4].setExit("east", room[5]);
        room[4].setExit("west", room[3]);
        room[4].setExit("downstairs", room[1]);

        room[5].setExit("north", room[9]);
        room[5].setExit("east", room[6]);
        room[5].setExit("west", room[4]);

        room[6].setExit("west", room[5]);
        room[6].setExit("hole", room[2]); // one-way

        room[7].setExit("east", room[8]);

        room[8].setExit("north", room[10]);
        room[8].setExit("east", room[9]);
        room[8].setExit("south", room[4]);
        room[8].setExit("west", room[7]);

        room[9].setExit("south", room[5]);
        room[9].setExit("west", room[8]);
        
        room[10].setExit("south", room[8]);

        // make user enter room[0]
        currentRoom = room[0];
        player[0].enterRoom(currentRoom);
    }

    /**
     * The first time this is called, the user's input is recorded, and
     * set to the name of their player. Every time after the first,
     * this method pdates the game based on the user's command.
     * If a user chooses to quit, the JVM is terminated after 3 seconds.
     */
    public void updateGameState(String text)
    {
        boolean quit = false;

        if(isCreated == false) {
            isCreated = true;
            createGame(text);
        } else {
            Command command = parser.getCommand(text);
            quit = processCommand(command);
            if (quit == true) {
                System.out.print("Thank you for playing. Good bye.");
                new Timer().schedule( 
                    new TimerTask() {
                        public void run() {
                            System.exit(0);
                        }
                    }, 
                    3000 
                );
            }
        }
    }

    /**
     * Print out the opening message for the player, asking them what
     * they would like to be called.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Tomorrow, stranger.");
        System.out.println("What would you like to be called?");   
    }

    /**
     * Prints initial information for the user.
     */
    private void printInfo()
    {
        System.out.println("Enjoy your stay in the World of Tomorrow, " + player[0].getNameDisplayed() + "!");
        System.out.println("Type 'fullscreen' to toggle Fullscreen mode.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player[0].getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
            System.out.println("I don't know what you mean...");
            break;

            case GO:
            goRoom(command);
            break;

            case BACK:
            goBack(command);
            break;

            case LOOK:
            look();
            break;

            case TALK:
            talk(command);
            break;

            case BAG:
            printItems();
            break;

            case TAKE:
            take(command);
            break;

            case DROP:
            drop(command);
            break;

            case GIVE:
            give(command);
            break;

            case USE:
            use(command);
            break;

            case QUIT:
            wantToQuit = quit(command);
            break;

            case HELP:
            printHelp();
            break;

            case FULLSCREEN:
            fullscreen();
            break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Toggles between fullscreen mode and windowed mode.
     */
    private void fullscreen()
    {
        fullscreenMode = gui.toggleFullscreen(fullscreenMode);
        if (fullscreenMode == true) {
            System.out.println("Entered Fullscreen Mode.");
        } else if (fullscreenMode == false) {
            System.out.println("Entered Windowed Mode.");
        }
    }

    /**
     * Print out a message, and a list of all commands the player has.
     */
    private void printHelp()
    {
        System.out.println("You are lost in a strange world, hoping to find a way back to the real world.");
        System.out.println();
        System.out.println("Your command words are: " + parser.getCommandList()); 
    }

    /** 
     * Try to leave the room, through an door. The player is told if there
     * is no door in that direction.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        nextRoom = player[0].getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            if (nextRoom == room[11]) {
                win();
            } else {
                roomHistory.push(player[0].getCurrentRoom());
                player[0].enterRoom(nextRoom);
                gui.updateFrame(nextRoom);
                System.out.println(player[0].getLongDescription());
            }
        }
    }

    /**
     * Go back to the previous room. Obtained from the stack of rooms
     * previously visited.
     * If there is no previous room, the user is told this.
     */
    private void goBack(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Back where?");
            return;
        }

        if (roomHistory.isEmpty()) {
            System.out.println("You can't go back to nothing");
        }
        else {
            previousRoom = (Room) roomHistory.pop();
            player[0].enterRoom(previousRoom);
            gui.updateFrame(previousRoom);
            System.out.println(player[0].getLongDescription());
        }
    }

    /**
     * Look around in the room, and print a description of the room.
     */
    private void look()
    {
        System.out.println(player[0].getLongDescription());
    }

    /**
     * Prints out the items that the player is currently carrying.
     */
    private void printItems() {
        System.out.println(player[0].getItemsString());
    }

    /** 
     * Try to pick up an item from the current room, if the item is there.
     * The user is told if the item is not there.
     */
    private void take(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("What do you want to take?");
            return;
        }

        String itemName = command.getSecondWord();
        
        if(player[0].canPickItem(itemName) == true) {
            Item item = player[0].pickUpItem(itemName);
            if(item == null) {
                System.out.println("There is no item called " + itemName + " here.");
            } else {
                System.out.println("Picked up " + item.getDescription() + ".");
            }
        } else {
            System.out.println("You cannot fit this item into your bag.");
        }
    }

    /** 
     * Drops an item into the current room if the player has that item.
     * Otherwise, the player is told they do not have that item.
     */
    private void drop(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("What do you want to drop?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player[0].dropItem(itemName);

        if(item == null) {
            System.out.println("You don't carry the item: " + itemName + ".");
        } else {
            System.out.println("Dropped " + itemName + ".");
        }
    }

    /** 
     * Give an item to a character if the character exists
     * in the room, and the player has the item.
     * Otherwise, print the corresponding error message.
     */
    private void give(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to give...
            System.out.println("What do you want to give?");
            return;
        }
        if(!command.hasThirdWord()) {
            // if there is no third word, we don't know who to give the item to...
            System.out.println("Who do you want to give the item to?");
            return;
        }

        String itemName = command.getSecondWord();
        String characterName = command.getThirdWord();

        currentRoom = player[0].getCurrentRoom();
        String roomCharacterList = currentRoom.getCharacterNames().toLowerCase();
        boolean characterFound = false;

        if(roomCharacterList.contains(characterName)) {
            for(Player character : player) {
                if(characterName.equals(character.getName())) {
                    characterFound = true;
                    Item itemWanted = character.getItemWanted();
                    if(itemWanted == null) {
                        System.out.println(character.getNameDisplayed() + " does not want any item.");
                    } else {
                        if(itemName.equals(itemWanted.getName())) {
                            Item item = player[0].giveItem(itemName, character);
                            if(item == null){
                                System.out.println("You do not have " + itemName + ".");
                            } else {
                                System.out.println("You have given " + 
                                    character.getItemWanted().getName() + " to " + 
                                    character.getNameDisplayed() + ".");
                                character.setItemOwned1(character.getItemWanted()); //sets ItemOwned1 to ItemWanted (received)
                                character.setItemWanted(null); //set ItemWanted to null (as it is owned)
                            }
                        } else {
                            System.out.println(character.getNameDisplayed() +
                                " does not want " + itemName + ".");
                        }
                    }
                }
            }
        }
        if(characterFound == false) {
            System.out.println("There is no-one called " + characterName + " in this room.");
        }
    }

    /**
     * Try to use a carried item on another item in the room, provided
     * both items exist.
     * Otherwise, print the corresponding error message.
     */
    private void use(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to use...
            System.out.println("What do you want to use?");
            return;
        }
        if(!command.hasThirdWord()) {
            // if there is no third word, we don't know what to use it on...
            System.out.println("What do you want to use the item on?");
            return;
        }
        
        String item1Name = command.getSecondWord();
        String item2Name = command.getThirdWord();
        
        currentRoom = player[0].getCurrentRoom();
        String roomItemList = currentRoom.getItems().toLowerCase();
        String bagItemList = player[0].getItems().toLowerCase();
        
        boolean item1Found = false;
        boolean item2Found = false;
        boolean canBeUsed = false;
        boolean canBeUsedOn = false;
        
        if(bagItemList.contains(item1Name)) {
            for(Item object1 : item) {
                if(item1Name.equals(object1.getName())) {
                    item1Found = true;
                    canBeUsed = object1.canBeUsed();
                    if(canBeUsed == true) {
                        if(roomItemList.contains(item2Name)) {
                            for(Item object2 : item) {
                                if(item2Name.equals(object2.getName())) {
                                    item2Found = true;
                                    canBeUsedOn = object2.canBeUsedOn();
                                    if(canBeUsedOn == true) {
                                        player[0].useItem(item1Name);
                                        if(object1 == item[0] && object2 == item[1]) {
                                            room[0].setExit("portal", room[11]);
                                            System.out.println("You have created a portal!");
                                        }
                                    } else {
                                        System.out.println("You cannot use an item on " + object2.getName() + ".");
                                    }
                                }
                            }
                        }
                        if(item2Found == false) {
                            System.out.println("The room does not contain " + item2Name + ".");
                        }
                    } else {
                        System.out.println(object1.getName() + " cannot be used.");
                    }
                }
            }
        }
        if(item1Found == false) {
            System.out.println("You do not have " + item1Name + ".");
        }
    }
    
    /**
     * Talk to a character if the character exists in the room.
     */
    private void talk(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know who to talk to...
            System.out.println("Who do you want to talk to?");
            return;
        }

        String characterName = command.getSecondWord();

        currentRoom = player[0].getCurrentRoom();
        String roomCharacterList = currentRoom.getCharacterNames().toLowerCase();
        boolean characterFound = false;

        if(roomCharacterList.contains(characterName)) {
            for(Player character : player) {
                if(characterName.equals(character.getName())) {
                    characterFound = true;
                    switch (character.getName()) {
                        case "banker":
                            if(character.getItemWanted() != null) {
                                System.out.println(character.getNameDisplayed() +
                                    ": Sorry, I can't talk right now. Someone has stolen my wallet!!");
                            } else {
                                if(bankerHS == false) {
                                    bankerHS = true;
                                    character.setItemOwned2(item[7]); //banker now has money
                                    Item object = player[0].getItemBack(character);

                                    System.out.println(character.getNameDisplayed() +
                                        ": Thank you so much, " + player[0].getNameDisplayed() +
                                        "! My wallet is my life!! You can have some money as "+
                                        "a reward! \n\n ** The " + character.getNameDisplayed() + 
                                        " gives you some " + object.getName() + " **");
                                    if(player[0].isItemDropped() == true) {
                                        System.out.println("\nYou do not have space in your bag. " +
                                            "You leave the " + object.getName() + " on the floor.");
                                    } else {
                                        System.out.println("You receive " + object.getDescription() + ".");
                                    }
                                } else {
                                    System.out.println(character.getNameDisplayed() +
                                        ": Thanks for finding my wallet.");
                                }
                            }
                            break;

                        case "chef":
                            if(character.getItemWanted() != null) {
                                System.out.println("Mamma mia! I have no cheese.");
                            } else {
                                if(chefHS == false) {
                                    chefHS = true;
                                    character.setItemOwned2(item[6]); //chef now has food
                                    Item object = player[0].getItemBack(character);

                                    System.out.println(character.getNameDisplayed() +
                                        ": Thank you so much, " + player[0].getNameDisplayed()
                                        + ". I can now make delicious pizza!! \n\n " +
                                        "** The " + character.getNameDisplayed() + " " +
                                        "gives you some " + object.getName() + " **");
                                    if(player[0].isItemDropped() == true) {
                                        System.out.println("\nYou do not have space in your bag. " +
                                            "You leave the " + object.getName() + " on the floor.");
                                    } else {
                                        System.out.println("You receive " + object.getDescription() + ".");
                                    }
                                } else {
                                    System.out.println(character.getNameDisplayed() +
                                        ": I hope you enjoyed the pizza, " +
                                        player[0].getNameDisplayed() + ".");
                                }
                            }
                            break;

                        case "child":
                            if(character.getItemWanted() != null) {
                                System.out.println(character.getNameDisplayed() +
                                    ": I've lost my ball! Waaaaah \n\n ** cries **");
                            } else {
                                childStory = true;
                                System.out.println(character.getNameDisplayed() +
                                    ": ** plays football ** ");
                            }
                            break;

                        case "guard":
                            if(character.getItemWanted() != null) {
                                System.out.println(character.getNameDisplayed() +
                                    ": I've lost my flashlight. How will I be " +
                                    "able to keep watch during the night?");
                            } else {
                                guardStory = true;
                                System.out.println(character.getNameDisplayed() +
                                    ": Thanks for saving my job, " +
                                    player[0].getNameDisplayed() + "! " +
                                    "I don't think I can ever repay you.");
                            } 
                            break;

                        case "librarian":
                            if(character.getItemWanted() != null) {
                                System.out.println("Some books are missing. Let me know if " +
                                    "you find any.");
                            } else {
                                librarianStory = true;
                                System.out.println("Thanks for restoring order in this place.");
                            }
                            break;

                        case "scientist":
                            if(character.getItemWanted() != null) {
                                if(scientistHS1 == false) {
                                    scientistHS1 = true;

                                    System.out.println(character.getNameDisplayed() +
                                        ": Hi there. I can tell that you are not from around here." +
                                        " It seems you have somehow ended up in our world. Well, " +
                                        "I have some good news. I recently built a portal-making " +
                                        "machine, which would be able to create a portal back to " + 
                                        "your world. However, I can't find any more fuel. If you " +
                                        "bring me some, I will be happy to let you use the portal.");
                                } else {
                                    System.out.println(character.getNameDisplayed() +
                                        ": Hello, " + player[0].getNameDisplayed() + ". I'm very " +
                                        "confused as to how the last portal I created brought you " +
                                        "here. I haven't found any fuel yet. If you bring me some," +
                                        " you can use the portal to get back to your world.");
                                }
                            } else {
                                if(scientistHS2 == false) {
                                    scientistHS2 = true;
                                    character.setItemOwned2(character.getItemOwned1());
                                    character.setItemOwned1(null);
                                    Item object = player[0].getItemBack(character);
                                    object.setCanBeUsed(true);
                                    item[1].setCanBeUsedOn(true);
                                    canUsePortal = true;
                                
                                    System.out.println(character.getNameDisplayed() +
                                        ": I can't believe you got the fuel. As a thank you, " +
                                        "I will let you do the honours. Go ahead, create the portal!!!"
                                        + " \n\n ** The " + character.getNameDisplayed() +
                                        " hands you back the " + object.getName() + " **");
                                    if(player[0].isItemDropped() == true) {
                                        System.out.println("\nYou do not have space in your bag. " +
                                            "You leave the " + object.getName() + " on the floor.");
                                    } else {
                                        System.out.println("You receive " + object.getDescription() + ".");
                                    }
                                } else {
                                    System.out.println(character.getNameDisplayed() +
                                        ": Thank you again, " + player[0].getNameDisplayed() +
                                        ". You have allowed us all to use the portal creator.");
                                }
                            }
                            break;

                        case "salesman":
                            if(character.getItemWanted() != null) {
                                System.out.println(character.getNameDisplayed() +
                                    ": We have lots of things to buy. Houses, cars, fuel, " +
                                    "TVs, computers, robots,g anything you can think of!");
                            } else {
                                if(salesmanHS == false) {
                                    salesmanHS = true;
                                    Item object = player[0].getItemBack(character);
                                    
                                    System.out.println(character.getNameDisplayed() +
                                        ": Enjoy the fuel, " + player[0].getNameDisplayed() +
                                        ". \n\n ** The " + character.getNameDisplayed() + " " +
                                        "sells you some " + object.getName() + " **");
                                    if(player[0].isItemDropped() == true) {
                                        System.out.println("\nYou do not have space in your bag. " +
                                            "You leave the " + object.getName() + " on the floor.");
                                    } else {
                                        System.out.println("You receive " + object.getDescription() + ".");
                                    }
                                } else {
                                    System.out.println(character.getNameDisplayed() +
                                        ": We're currently out of stock.");
                                }
                            }
                            break;

                        case "thief":
                            if(character.getItemWanted() != null) {
                                System.out.println(character.getNameDisplayed() +
                                    ": Hi, there. I accidentally took someone else's wallet, and " +
                                    "I don't know who to return it to. I'm very hungry, so " +
                                    "I would exchange it for some food.");
                            } else {
                                if(thiefHS == false) {
                                    thiefHS = true;
                                    Item object = player[0].getItemBack(character);
                                    
                                    System.out.println(character.getNameDisplayed() +
                                        ": Thanks for the food, " + player[0].getNameDisplayed() +
                                        ". I was starving! Here is the wallet. \n\n" +
                                        "** The " + character.getNameDisplayed() + " " +
                                        "hands over the " + object.getName() + " **");
                                } else {
                                    System.out.println(character.getNameDisplayed() +
                                        ": Thanks for the food, " + player[0].getNameDisplayed() +
                                        ". I was starving!");
                                }
                            }
                            break;
                    }
                }
            }
        }
        if(characterFound == false) {
            System.out.println("There is no-one called " + characterName + " in this room.");
        }
    }

    /** 
     * If the user types in quit, followed by any other words, ask again
     * if they truly want to quit. Otherwise, return true for wanting to 
     * quit the game.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    // win screen
    
    /**
     * Check if the user has completed the side-story. Return a string
     * saying either "Yes" or "No".
     */
    private String isSideStoryCompleted()
    {
        if(childStory == true && guardStory == true && librarianStory == true) {
            return "Yes";
        } else {
            return "No";
        }
    }
    
    /**
     * If the player can use the portal, enter the room to win the game.
     * The user is congratulated, and told statistics on whether or not
     * they completed the side-story, and how long they took to win.
     * 
     * Schedules a task to close the game after 7 seconds.
     */
    private void win()
    {
        if (canUsePortal == false) {
            System.out.println("The machine has no fuel.");
        } else {
            roomHistory.push(player[0].getCurrentRoom());
            player[0].enterRoom(nextRoom);
            gui.updateFrame(nextRoom);
            
            gui.winScreen();

            System.out.println("Congratulations!!!!!!!");
            System.out.println("You have escaped the World of Tomorrow "+
                "and returned to your world!\n\nSidestory completed: " + isSideStoryCompleted());

            endTime = System.nanoTime();
            int timeTaken = (int) ((endTime - startTime) * 1.0e-9);
            String hours = String.format("%02d", (int) (timeTaken/3600.0));
            String minutes = String.format("%02d", (((int) (timeTaken/60.0)) % 60));
            String seconds = String.format("%02d", (int) (timeTaken % 60));
            System.out.println("\n\nTime taken: " + hours + ":" + minutes + ":" + seconds);
            
            System.out.println("\n\nThank you for playing. Good bye.");
            new Timer().schedule( 
                new TimerTask() {
                    public void run() {
                        System.exit(0);
                    }
                }, 
                7000
            );
        }
    }
}