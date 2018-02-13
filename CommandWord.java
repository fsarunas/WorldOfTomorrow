/**
 * This class is part of the "World of Tomorrow" game.
 * Representations for all the valid command words for the game.
 * 
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
    UNKNOWN("?"), GO("go"), BACK("back"), LOOK("look"), TALK("talk"), BAG("bag"), TAKE("take"),
    GIVE("give"), DROP("drop"), QUIT("quit"), HELP("help"), FULLSCREEN("fullscreen"), USE("use");
    
    // The command string.
    private String commandString;
    
    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    /**
     * @return The command word as a string.
     */
    public String toString()
    {
        return commandString;
    }
}