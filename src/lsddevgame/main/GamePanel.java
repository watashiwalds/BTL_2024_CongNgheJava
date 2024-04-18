package lsddevgame.main;

import lsddevgame.main.inputs.KeyboardInputs;
import lsddevgame.main.inputs.MouseInputs;
import javax.swing.*;
import java.awt.*;
import static lsddevgame.main.utils.ConstantValues.GameParameters.*;

public class GamePanel extends JPanel {

    private Game game;
    private MouseInputs mouseInputs;

    public GamePanel(Game game) {
        //hold to the father Game
        this.game = game;

        //assign parameters first value
        mouseInputs = new MouseInputs(this); //mouse have 2 separate listener type (state and motion) so it gets a parameter

        //add control listener (keyboard n mouse)
        addKeyListener(new KeyboardInputs(this)); //implement keyboard input listener of KeyboardInput
        addMouseListener(mouseInputs); //for click (down > up), press (down), release (up)
        addMouseMotionListener(mouseInputs); //for move (up move), drag (down move)

        //set this panel's size
        setPanelSize();
    }

    private void setPanelSize() {
        Dimension dms = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMinimumSize(dms);
        setPreferredSize(dms);
        setMaximumSize(dms);
    }

    //required for GamePanel to display stuff on GameWindow
    //this being called *FPS* time per second
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //maid who clean the frame for us to draw - first-hand frame regen of JComponent
        game.render(g); //call everything that need to be painted in Game
    }

    public Game getGame() {
        return this.game;
    }
}
