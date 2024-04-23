package lsddevgame.main;

import lsddevgame.main.gamestates.GameState;
import lsddevgame.main.utils.ConstantValues;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow extends JFrame {
    public GameWindow(GamePanel gamePanel) {
        //Create the basic program's window with desired parameters
        JFrame gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(EXIT_ON_CLOSE); //press x to close
        gameWindow.add(gamePanel); //implement gamePanel
        gameWindow.pack(); //fit window to gamePanel (to all .add() up it)
        gameWindow.setResizable(false);
        gameWindow.setTitle("Little Cat goes the Knowledge path"); //window's name
        gameWindow.setLocationRelativeTo(null); //spawn middle screen
        gameWindow.setVisible(true);
        gameWindow.requestFocus();
        gameWindow.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowLostFocus();
            }
        });
    }
}
