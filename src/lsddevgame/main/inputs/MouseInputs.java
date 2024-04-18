package lsddevgame.main.inputs;

import lsddevgame.main.GamePanel;
import lsddevgame.main.gamestates.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gP;
    public MouseInputs(GamePanel gP) {
        this.gP = gP;
    }

    @Override
    public void mouseClicked(MouseEvent e) { //when click mouse

    }

    @Override
    public void mousePressed(MouseEvent e) { //when hold mouse click
        switch (GameState.state) {
            case MENU :
                gP.getGame().getGsMenu().mousePressed(e);
                break;
            case PLAYING :
                gP.getGame().getGsPlaying().mousePressed(e);
                break;
            case SETTINGS:
                gP.getGame().getGsSetting().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { //when release mouse that being pressed
        switch(GameState.state) {
            case MENU :
                gP.getGame().getGsMenu().mouseReleased(e);
                break;
            case PLAYING :
                gP.getGame().getGsPlaying().mouseReleased(e);
                break;
            case SETTINGS:
                gP.getGame().getGsSetting().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { //when the mouse is approaching the panel

    }

    @Override
    public void mouseExited(MouseEvent e) { //when it goes out of the panel

    }

    @Override
    public void mouseDragged(MouseEvent e) { //when mouse hold and drag

    }

    @Override
    public void mouseMoved(MouseEvent e) { //when mouse not hold but moved
        switch(GameState.state) {
            case MENU :
                gP.getGame().getGsMenu().mouseMoved(e);
                break;
            case PLAYING :
                gP.getGame().getGsPlaying().mouseMoved(e);
                break;
            case SETTINGS:
                gP.getGame().getGsSetting().mouseMoved(e);
        }
    }
}
