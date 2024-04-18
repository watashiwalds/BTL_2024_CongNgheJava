package lsddevgame.main.gamestates;

import lsddevgame.main.Game;
import lsddevgame.main.ui.StateChangingButton;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.HelperPack;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State {
    private StateChangingButton btn[] = new StateChangingButton[3];
    private BufferedImage logo, bg;
    private int logoCenterXAlign, logoCenterYAlign;

    public Menu(Game game) {
        super(game);
        decorLoad();
        buttonInitialize();
    }

    private void decorLoad() {
        logo = LoadData.GetSpriteImage(LoadData.FULLLOGO_ATLAS);
        logoCenterXAlign = (ConstantValues.GameParameters.GAME_WIDTH/2)-((logo.getWidth()/2)*(int)ConstantValues.GameParameters.SCALING/2);
        logoCenterYAlign = (ConstantValues.GameParameters.GAME_HEIGHT/2)-((logo.getHeight()/2)*(int)ConstantValues.GameParameters.SCALING/2);
        bg = LoadData.GetSpriteImage(LoadData.MAINMENU_BACKGROUND);
    }
    private void buttonInitialize() {
        BufferedImage btnatlas = LoadData.GetSpriteImage(LoadData.MAINMENU_BUTTONS);
        btn[0] = new StateChangingButton(this, ConstantValues.GameParameters.GAME_WIDTH/2, (int)(ConstantValues.GameParameters.GAME_HEIGHT/1.75f), 3, btnatlas, 0, GameState.PLAYING, game.getAudioPlayer());
        btn[1] = new StateChangingButton(this, ConstantValues.GameParameters.GAME_WIDTH/2, (int)((ConstantValues.GameParameters.GAME_HEIGHT/1.75f)+(int)(ConstantValues.UIParameters.Buttons.HEIGHT*1.25f)), 3, btnatlas, 1, GameState.SETTINGS, game.getAudioPlayer());
        btn[2] = new StateChangingButton(this, ConstantValues.GameParameters.GAME_WIDTH/2, (int)((ConstantValues.GameParameters.GAME_HEIGHT/1.75f)+(int)(ConstantValues.UIParameters.Buttons.HEIGHT*2.5f)), 3, btnatlas, 2, GameState.QUIT, game.getAudioPlayer());
    }


    @Override
    public void update() {
        for (StateChangingButton x : btn) x.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bg, 0, 0, ConstantValues.GameParameters.GAME_WIDTH, ConstantValues.GameParameters.GAME_HEIGHT, null);
        g.drawImage(logo, logoCenterXAlign, ConstantValues.GameParameters.GAME_HEIGHT/10, logo.getWidth()*(int)ConstantValues.GameParameters.SCALING/2, logo.getHeight()*(int)ConstantValues.GameParameters.SCALING/2, null);
        for (StateChangingButton x : btn) x.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (StateChangingButton x : btn) {
            x.setMousePressed(x.isMouseOver());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (StateChangingButton x : btn) {
            x.setMouseOver(HelperPack.IsMouseOnSensableArea(x.getTouchbox(), e));
            if (x.isMouseOver()) {
                x.applyAction();
                x.resetValues();
                if (GameState.state == GameState.PLAYING && game.getGsPlaying() != null) {
                    game.playingInitialize();
                }
                break;
            }
            x.resetValues();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (StateChangingButton x : btn)
            x.setMouseOver(HelperPack.IsMouseOnSensableArea(x.getTouchbox(), e));
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
