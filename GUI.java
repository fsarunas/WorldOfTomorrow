import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is for the GUI element of the "World of Tomorrow" game.
 * The frame is created, and later updated.
 *
 * @author Sarunas Djacenko
 * @version 2017.12.08
 */
public class GUI
{
    private JFrame frame;
    private BorderLayout layout;
    private JLayeredPane layers;
    
    private String roomFileName, characterFileNames;
    
    private JLabel label[];
    private JPanel container, containerImage, containerText;
    private JTextField jtfInput;
    private JTextArea jtaOutput;
    private ImageIcon icon[];
    private BufferedImage picture[];
    
    private Game game;
    private Rectangle previousBounds;
    private Font defaultFont;
    private boolean fullscreenMode, isCreated;
    private int heightRoomDefault, heightCharDefault, heightRoom, heightChar, heightMax, i, fileCount, fontSize, fontSizeDefault;
    private double resMultiplier;
    
    private Room currentRoom;

    /**
     * Calls a method to create the frame.
     */
    public GUI(Game game) {
        this.game = game;
        makeFrame();
    }
    
    /**
     * Calls a method to update the game, based on what the user has entered.
     */
    private void updateGame(String text)
    {
        game.updateGameState(text);
    }
    
    /**
     * Set up initial display.
     * Default aspect ratio (for windowed app) is 16:9.
     */
    private void makeFrame() {
        //At any time, there is 1 room.
        //So total characters + 1 room = maximum number of images.
        File directoryChar = new File("images/characters");
        fileCount = directoryChar.list().length;
        
        picture = new BufferedImage[fileCount + 1];
        icon = new ImageIcon[fileCount + 1];
        label = new JLabel[fileCount + 1];
        
        try {
            picture[1] = ImageIO.read(new File("images/characters/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        label[1] = new JLabel();
        
        //Define constants (not overwritten) and initial values.
        heightRoomDefault = 540;
        heightCharDefault = 100;
        fontSizeDefault = 13;
        
        fontSize = fontSizeDefault;
        heightRoom = heightRoomDefault;
        heightChar = heightCharDefault;
        heightMax = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        
        resMultiplier = ((1.0*heightMax)/(1.0*heightRoomDefault));
        
        defaultFont = new Font("Courier New", Font.ITALIC, fontSizeDefault);
        
        //Create and set up the window.
        frame = new JFrame("World of Tomorrow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        //Add output stream
        jtaOutput = new JTextArea();
        jtaOutput.setFont(defaultFont);
        jtaOutput.setLineWrap(true);
        jtaOutput.setWrapStyleWord(true);
        jtaOutput.setBackground(new Color(238,238,238));
        jtaOutput.setEditable(false);
        PrintStream out = new PrintStream(new TextOutput(jtaOutput));
        System.setOut(out);
        System.setErr(out);
        
        JScrollPane scrollPane = new JScrollPane(jtaOutput);
        //scrollbar disabled. If the user wants to see room description, they can type "look".
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        
        //Add text field
        jtfInput = new JTextField();
        jtfInput.setFont(defaultFont);        
        
        jtfInput.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String text = jtfInput.getText();
                                jtfInput.setText("");
                                jtaOutput.append("\n" + text + "\n\n");
                                
                                //Scrolls to bottom each time there is input.
                                int len = jtaOutput.getDocument().getLength();
                                jtaOutput.setCaretPosition(len);
                                
                                updateGame(text);
                            }
                        });
        
                        
        //Set up GUI layout.
        //Create Image container.
        containerImage = new JPanel();
        layers = new JLayeredPane();
        //Add images.
        loadFiles(null);
        containerImage.setLayout(null);
        containerImage.add(layers);
        
        //create Text container.
        containerText = new JPanel(new BorderLayout());
        containerText.add(scrollPane, BorderLayout.CENTER);
        containerText.add(jtfInput, BorderLayout.SOUTH);
        
        //create layout containing Images and Text.
        container = new JPanel();
        layout = new BorderLayout();
        container.setLayout(layout);
        container.add(containerImage, BorderLayout.WEST);
        container.add(containerText, BorderLayout.CENTER);
        
        //Display the window in a 16:9 aspect ratio.
        frame.getContentPane().setPreferredSize(new Dimension((int) ((16.0/9.0)*heightRoomDefault), heightRoomDefault));
        frame.getContentPane().add(container);
        frame.pack();
        frame.setVisible(true);
        jtfInput.requestFocusInWindow();
    }
    
    private void loadFiles(Room currentRoom)
    {
        if(isCreated == false) {
            isCreated = true;
            try {
                containerImage.setPreferredSize(new Dimension(heightRoom, heightRoom));
                layers.removeAll();
                layers.setBounds(0,0,heightRoom,heightRoom);
                
                picture[0] = ImageIO.read(new File("images/other/welcome.png"));
                icon[0] = new ImageIcon(picture[0].getScaledInstance(heightRoom, heightRoom, Image.SCALE_SMOOTH));
                label[0] = new JLabel(icon[0]);
                frame.setIconImage(icon[0].getImage());
                
                label[0].setBounds(0, 0, heightRoom, heightRoom);
                layers.add(label[0], new Integer(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                roomFileName = currentRoom.getFileName();
                characterFileNames = currentRoom.getCharacterFileNames();
            
                containerImage.setPreferredSize(new Dimension(heightRoom, heightRoom));
                layers.removeAll();
                layers.setBounds(0,0,heightRoom,heightRoom);
            
                picture[0] = ImageIO.read(new File(roomFileName));
                icon[0] = new ImageIcon(picture[0].getScaledInstance(heightRoom, heightRoom, Image.SCALE_SMOOTH));
                label[0] = new JLabel(icon[0]);
            
                label[0].setBounds(0, 0, heightRoom, heightRoom);
                layers.add(label[0], new Integer(0));
            
                
                String[] lines = characterFileNames.split(" ");
                int nCharacters = lines.length + 1; // characters in room + player
                double location = ((heightRoom - 1.0*heightChar*(nCharacters + (nCharacters - 1.0)/nCharacters))/2.0);
                
                if(currentRoom.getShortDescription() != "") {
                    icon[1] = new ImageIcon(picture[1].getScaledInstance(heightChar, heightChar, Image.SCALE_SMOOTH));
                    label[1].setIcon(icon[1]);

                    label[1].setBounds((int) (location), (int) ((heightRoom - heightChar)/2.0), icon[1].getIconWidth(), icon[1].getIconHeight());
                    layers.add(label[1], new Integer(1));
                }
                
                i = 1;
                for (String fileName: lines) {
                    i++;
                    if (fileName != "") {
                        picture[i] = ImageIO.read(new File(fileName));
                
                        label[i] = new JLabel();
                        icon[i] = new ImageIcon(picture[i].getScaledInstance(heightChar, heightChar, Image.SCALE_SMOOTH));
                        label[i].setIcon(icon[i]);
                
                        label[i].setBounds((int) (location + ((i-1.0)*heightChar*(1.0 + 1.0/nCharacters))), (int) ((heightRoom - heightChar)/2.0), icon[i].getIconWidth(), icon[i].getIconHeight());
                        layers.add(label[i], new Integer(i));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
    
    /**
     * Updates the image in the frame, based on the room that the player goes to.
     */
    public void updateFrame(Room currentRoom)
    {
        this.currentRoom = currentRoom;
        loadFiles(currentRoom);
    }
    
    /**
     * Toggle between Fullscreen and Windowed mode.
     * 
     * Bottom of GUI is cut off on Linux due to Linux limitations of
     * not allowing a true fullscreen (hiding taskbar and menu bar).
     * Works fine on Windows.
     */
    public boolean toggleFullscreen(boolean fullscreen)
    {
        if (fullscreen == false) {
            //images and text scaled up.
            heightChar = (int) (heightChar * resMultiplier);
            heightRoom = (int) (heightRoom * resMultiplier);
            fontSize = (int) (fontSizeDefault * resMultiplier);
            jtaOutput.setFont(defaultFont.deriveFont((float) fontSize));
            jtfInput.setFont(defaultFont.deriveFont((float) fontSize));
            
            frame.dispose();
            previousBounds = frame.getBounds();
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            loadFiles(currentRoom);
            
            frame.pack();
            frame.setVisible(true);
            jtfInput.requestFocusInWindow();
            
            fullscreen = true;
            fullscreenMode = fullscreen;
        } else {
            //images and text reverted back to default.
            heightChar = heightCharDefault;
            heightRoom = heightRoomDefault;        
            jtaOutput.setFont(defaultFont);
            jtfInput.setFont(defaultFont);
            
            frame.dispose();
            frame.setBounds(previousBounds);
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            loadFiles(currentRoom);
            
            frame.pack();
            frame.setVisible(true);
            jtfInput.requestFocusInWindow();
            
            fullscreen = false;
            fullscreenMode = fullscreen; 
        }
        return fullscreen;
    }
    
    /**
     * this method is called when the player gets to the
     * "winning room".
     */
    public void winScreen()
    {
        frame.dispose();
        containerText.remove(jtfInput);
        jtaOutput.setText("");
        frame.pack();
        frame.setVisible(true);
    }
}
