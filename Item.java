/**
 * This class is part of the "World of Tomorrow" game.
 *
 * This class holds information about an Item. 
 * An "Item" has a displayed name, description and a weight.
 * 
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */

public class Item
{
    private String nameDisplayed, description;
    private double weight;
    private boolean canBePickedUp, canBeUsed, canBeUsedOn;
    
    /**
     * Create a new item with the given name, description and weight.
     */
    public Item(String nameDisplayed, String description, double weight)
    {
        this.nameDisplayed = nameDisplayed;
        this.description = description;
        this.weight = weight;
        this.canBePickedUp = canBePickedUp;
    }

    /**
     * @return the weight of the item.
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * @return the displayed name of the item.
     */
    public String getNameDisplayed() {
        return nameDisplayed;
    }
    
    /**
     * @return the name of the item in lower case
     */
    public String getName() {
        return nameDisplayed.toLowerCase();
    }
    
    /**
     * @return the description of the item.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets whether an item can be used or not.
     */
    public void setCanBeUsed(boolean param) {
        canBeUsed = param;
    }
    
    /**
     * @return whether item can be used or not.
     */
    public boolean canBeUsed() {
        return canBeUsed;
    }
    
    /**
     * Sets whether an item can be used on this item or not.
     */
    public void setCanBeUsedOn(boolean param) {
        canBeUsedOn = param;
    }
    
    /**
     * @return whether item can be used on this item or not.
     */
    public boolean canBeUsedOn() {
        return canBeUsedOn;
    }
}
