/**
 * This class is part of the "World of Tomorrow" game. 
 * 
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */
public class Player
{
    private String nameDisplayed, inventory, fileName;
    private Room currentRoom;
    private Item itemWanted, itemOwned1, itemOwned2;
    private Items items = new Items();
    private int maxWeight = 99;
    private boolean itemPlacedRoom;
    
    /**
     * Constructor for objects of class Player.
     * Parameters are displayed name, image file name, item wanted, item owned (1) and item owned (2).
     */
    public Player(String nameDisplayed, String fileName, Item itemWanted, Item itemOwned1, Item itemOwned2)
    {
        this.nameDisplayed = nameDisplayed;
        this.fileName = fileName;
        this.itemWanted = itemWanted;
        this.itemOwned1 = itemOwned1;
        this.itemOwned2 = itemOwned2;
        
        if(itemOwned1 != null) {
            items.put(itemOwned1.getName(), itemOwned1);
        }
        if(itemOwned2 != null) {
            items.put(itemOwned2.getName(), itemOwned2);
        }
    }

    /**
     * Enter the given room.
     */
    public void enterRoom(Room room) {
        currentRoom = room;
    }
    
    /**
     * @return the room in which the player is currently located.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    /**
     * @return the Item wanted
     */
    public Item getItemWanted() {
        return itemWanted;
    }
    
    /**
     * @return the Item owned (1)
     */
    public Item getItemOwned1() {
        return itemOwned1;
    }
    
    /**
     * @return the Item owned (2)
     */
    public Item getItemOwned2() {
        return itemOwned2;
    }

    /**
     * set Item wanted to null.
     */
    public void setItemWanted(Item item) {
        itemWanted = item;
    }

    /**
     * set Item Owned (1) to Item Wanted (when received).
     */
    public void setItemOwned1(Item item) {
        itemOwned1 = item;
        if(itemOwned1 != null){
            items.put(itemOwned1.getName(), itemOwned1);
        }
    }
    
    /**
     * set Item Owned (2) to "item"
     * "item" passed to this method as null (when given away).
     */
    public void setItemOwned2(Item item) {
        itemOwned2 = item;
        if(itemOwned2 != null){
            items.put(itemOwned2.getName(), itemOwned2);
        }
    }
    
    /**
     * @return the file name of the player image.
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @return the displayed name of the player.
     */
    public String getNameDisplayed() {
        return nameDisplayed;
    }
    
    /**
     * @return the name of the player in lower case.
     */
    public String getName() {
        return nameDisplayed.toLowerCase();
    }
    
    /**
     * @return the items the player owns.
     */
    public String getItems() {
        return items.getNames();
    }
    
    /**
     * @return a string describing the items that the player carries.
     */
    public String getItemsString() {
        if (items.getNames() == "") {
            inventory = "You are not carrying any items.";
        } else {
            inventory = "You are carrying:" + items.getNames();
        }
        return inventory;
    }
    
    /**
     * @return a string describing the players current location and which
     * items the player carries.
     */
    public String getLongDescription() {
        String returnString = currentRoom.getLongDescription();
        returnString += "\n" + getItemsString();
        return returnString;
    }

    /**
     * Tries to pick up the item from the current room.
     * @return If successful, this method will return the item that was picked up.
     */
    public Item pickUpItem(String itemName) {
        Item item = currentRoom.removeItem(itemName);
        if(item != null) {
            items.put(item.getName(), item);
        }
        return item;
    }
    
    /**
     * Tries to drop an item into the current room.
     * @return If successful, this method will return the item that was dropped.
     */
    public Item dropItem(String itemName) {
        Item item = items.remove(itemName);
        if(item != null) {
            currentRoom.addItem(item);
        }
        return item;
    }
    
    /**
     * Tries to give an item to a character.
     * @return If successful, this method will return the item that was given away.
     */
    public Item giveItem(String itemName, Player character) {
        Item item = items.remove(itemName);
        if(item != null) {
            character.items.put(item.getName(), item);
        }
        return item;
    }
    
    /**
     * Tries to receive an item from a character.
     * @return If successful, this method will return the item that was received.
     */
    public Item getItemBack(Player character) {
        itemPlacedRoom = false;
        Item item = character.items.get(character.getItemOwned2().getName());
        if(item != null) {
            if(canReceiveItem(character) == true) {
                items.put(item.getName(), item);
            } else {
                currentRoom.addItem(item);
                itemPlacedRoom = true;
            }
            character.setItemOwned2(null);
        }
        return item;
    }
    
    /**
     * @return whether the item was placed in the room when
     * receiving the item from a character
     */
    public boolean isItemDropped()
    {
        return itemPlacedRoom;
    }
    
    /**
     * Uses the item.
     */
    public Item useItem(String itemName) {
        Item item = items.remove(itemName);
        return item;
    }
    
    /**
     * Checks if we can pick up the item. This depends on whether the item 
     * actually is in the current room and if it is not too heavy.
     */
    public boolean canPickItem(String itemName) {
        boolean canPick = true;
        Item item = currentRoom.getItem(itemName);
        if(item != null) {
            double totalWeight = items.getTotalWeight() + item.getWeight();
            if(totalWeight > maxWeight) {
                canPick = false;
            }
        }
        return canPick;         
    }
    
    /**
     * Checks if we can receive the item. This depends on whether the item 
     * is not too heavy.
     */
    public boolean canReceiveItem(Player character) {
        boolean canPick = true;
        Item item = character.items.get(character.getItemOwned2().getName());
        double totalWeight = items.getTotalWeight() + item.getWeight();
        if(totalWeight > maxWeight) {
            canPick = false;
        }
        return canPick;         
    }
}