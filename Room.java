import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Tomorrow" game.   
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west, upstairs, downstairs and portal.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */
public class Room 
{
    private String description, fileName, itemList, characterList;
    private HashMap<String, Room> exits;
    private Items items;
    private Characters characters;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "physics lab".
     * @param description The room's description.
     * @param fileName The file name of the image of the room.
     */
    public Room(String description, String fileName) 
    {
        this.description = description;
        this.fileName = fileName;
        exits = new HashMap<>();
        items = new Items();
        characters = new Characters();
    }
    
    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param direction The direction of the exit.
     * @param neighbor The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Get the file name of the room image.
     */
    public String getFileName()
    {
        return fileName;
    }
    
    /**
     * @return String of the names of the characters in the room.
     * Does not include player's name.
     */
    public String getCharacterNames()
    {
        characterList = characters.getNamesDisplayed();
        return characterList;
    }
    
    /**
     * @return The description of the room.
     * (the one that was defined in the constructor)
     */
    public String getShortDescription()
    {
        return description;
    }
    
    /**
     * @return String of items in the room.
     */
    public String getItems() {
        return items.getNames();
    }
    
    /**
     * Return a description of the room in the form:
     *     You are in the lobby.
     *     Exits: north, east, west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        if (items.getNames() == "") {
            itemList = "\nNo items in the room.";
        } else {
            itemList = "\nItems in the room:" + items.getNames();
        }
        if (characters.getNamesDisplayed() == "") {
            characterList = "\nNo characters in the room.";
        } else {
            characterList = "\nCharacters in the room:" + characters.getNamesDisplayed();
        }
        
        return "You are in the " + description + ".\n" + getExitString() + itemList + characterList;
    }
    
    /**
     * @return String of image file names for characters in the room.
     */
    public String getCharacterFileNames()
    {
        return characters.getFileNames();
    }
    
    /**
    * Return a description of the room’s exits,
    * for example, "Exits: north west".
    * @return A description of the available exits.
    */
    public String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            String capitalizedExit = exit.substring(0,1).toUpperCase() + exit.substring(1);
            returnString += " " + capitalizedExit;
        }
        return returnString;
    }
    
    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

    /**
     * Puts a character to this room.
     */
    public void addPlayer(Player player) {
        characters.put(player.getName(), player);
    }
    
    /**
     * Puts an item into this room.
     */
    public void addItem(Item item) {
        items.put(item.getName(), item);
    }
    
    /**
     * Returns the item if it is available, otherwise it returns null.
     */
    public Item getItem(String name) {
        return (Item) items.get(name);
    }    
    
    /**
     * Removes and returns the item if it is available, otherwise it returns null.
     */
    public Item removeItem(String name) {
        return (Item) items.remove(name);
    }
}
