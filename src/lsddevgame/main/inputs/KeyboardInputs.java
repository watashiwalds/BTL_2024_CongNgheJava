package lsddevgame.main.inputs;

import lsddevgame.main.GamePanel;
import lsddevgame.main.gamestates.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private GamePanel gP;

    public KeyboardInputs(GamePanel gP) {
        this.gP = gP;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { //when press keyboard, this listen
        switch(GameState.state) {
            case MENU -> gP.getGame().getGsMenu().keyPressed(e);
            case PLAYING -> gP.getGame().getGsPlaying().keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { //when release key, this listen
        switch(GameState.state) {
            case MENU -> gP.getGame().getGsMenu().keyReleased(e);
            case PLAYING -> gP.getGame().getGsPlaying().keyReleased(e);
        }
    }
}
