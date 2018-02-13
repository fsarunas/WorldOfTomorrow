import java.util.Scanner;

/**
 * This class is part of the "World of Tomorrow" application. 
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Sarunas Djacenko
 * @version 2017.12.08
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        commands = new CommandWords();
    }

    /**
     * Returns valid commands from method getCommandList.
     */
    public String getCommandList() {
        return commands.getCommandList();
    }
    
    /**
     * @return The next command from the user.
     */
    public Command getCommand(String text) 
    {
        String word1 = null;
        String word2 = null;
        String word3 = null;

        String inputLine = text;

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next().toLowerCase(); // get first word
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next().toLowerCase(); // get second word
                if(tokenizer.hasNext()) {
                    word3 = tokenizer.next().toLowerCase(); // get third word
                    // note: we just ignore the rest of the input line.
                }
            }
        }

        return new Command(commands.getCommandWord(word1), word2, word3);
    }
}
