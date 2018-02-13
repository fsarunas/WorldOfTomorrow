import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is part of the "World of Tomorrow" game. 
 * 
 * This class stores characters in a HashMap, and
 * returns displayed names and image file names.
 * 
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */

public class Items
{
    private HashMap items = new HashMap();
    
    /**
     * Create a new item list.
     */
    public Items()
    {
    }
    
    /**
     * Removes an Item from the HashMap
     */
    public Item remove(String name) {
        return (Item) items.remove(name);
    }
    
    /**
     * Puts an Item in the HashMap, with key item name, and value Item.
     */
    public void put(String name, Item value) {
        items.put(name, value);
    }
    
    /**
     * @return Item from the HashMap with the name "name".
     */
    public Item get(String name) {
        return (Item) items.get(name);
    }
        
    /**
     * @return a string listing the items in the list.
     */
    public String getNames() 
    {
        String returnString = "";
        String itemString = "";
        for(Iterator iter = items.values().iterator(); iter.hasNext(); ) {
            itemString = ((Item) iter.next()).getName();
            returnString += " " + itemString.substring(0,1).toUpperCase() + itemString.substring(1);
        }
        
        return returnString;     
    }
    
    /**
     * @return the total weight that the player carries.
     */
    public double getTotalWeight() {
        double weight = 0;
        for(Iterator iter = items.values().iterator(); iter.hasNext(); )
            weight += ((Item) iter.next()).getWeight();
        return weight;        
    }
}
