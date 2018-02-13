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
public class Characters
{
    private HashMap characters = new HashMap();
    
    public Characters()
    {
    }
    
    /**
     *  Stores characters in a HashMap.
     */
    public void put(String name, Player player) {
        characters.put(name, player);
    }

    /**
     * Returns displayed names of characters in the HashMap.
     */
    public String getNamesDisplayed() 
    {
        String returnString = "";
        for(Iterator iter = characters.values().iterator(); iter.hasNext(); )
            returnString += " " + ((Player) iter.next()).getNameDisplayed();
        
        return returnString;
    }
    
    /**
     * Returns image file names of characters in the HashMap.
     */
    public String getFileNames()
    {
        String returnString = "";
        for(Iterator iter = characters.values().iterator(); iter.hasNext(); )
            returnString += ((Player) iter.next()).getFileName() + " ";
        if(returnString == "") {
            returnString = " ";
        }
        
        return returnString;
    }
}