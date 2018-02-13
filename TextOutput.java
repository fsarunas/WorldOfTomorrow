import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * This class is part of the "World of Tomorrow" game. 
 * The class redirects output to a JTextArea.
 * 
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */
public class TextOutput extends OutputStream {
    private JTextArea textArea;

    /**
     * Creates a new instance of TextOutput Stream.
     */
    public TextOutput(JTextArea jtfOutput) {
        textArea = jtfOutput;
    }

    /**
     * Redirects data to the textArea.
     */
    public void write(int b) throws IOException {
        // append the data as characters to the JTextArea control
        textArea.append(String.valueOf((char)b));
    }  
}