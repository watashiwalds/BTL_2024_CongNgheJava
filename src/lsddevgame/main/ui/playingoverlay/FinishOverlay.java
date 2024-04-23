package lsddevgame.main.ui.playingoverlay;

import lsddevgame.main.Game;
import lsddevgame.main.gamestates.GameState;
import lsddevgame.main.ui.StateChangingButton;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.HelperPack;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class FinishOverlay {
    private BufferedImage background;
    private int bgXAlign, bgYAlign;
    private StateChangingButton backToMenu;
    private Game game;

    public FinishOverlay(Game game) {
        this.game = game;
        loadImages();
        buttonInitialize();
    }

    private void loadImages() {
        background = LoadData.GetSpriteImage(LoadData.FINISHOVERLAY_BACKGROUND);
        bgXAlign = (ConstantValues.GameParameters.GAME_WIDTH/2)-(background.getWidth()/2)*(int)ConstantValues.GameParameters.SCALING;
        bgYAlign = (ConstantValues.GameParameters.GAME_HEIGHT/2)-(background.getHeight()/2)*(int)ConstantValues.GameParameters.SCALING;
    }

    private void buttonInitialize() {
        BufferedImage srcImg = LoadData.GetSpriteImage(LoadData.PAUSEOVERLAY_STATEBUTTONATLAS);

        backToMenu = new StateChangingButton(game.getGsPlaying(),(ConstantValues.GameParameters.GAME_WIDTH/2), (ConstantValues.GameParameters.GAME_HEIGHT/2)+(int)(24*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, srcImg, 0, GameState.MENU, game.getAudioPlayer());

        backToMenu.setFixScaling(2);
    }

    public void update() {
        backToMenu.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(255, 153, 51, 100));
        g.fillRect(0, 0, ConstantValues.GameParameters.GAME_WIDTH, ConstantValues.GameParameters.GAME_HEIGHT);
        g.drawImage(background, bgXAlign, bgYAlign, background.getWidth()*(int)ConstantValues.GameParameters.SCALING, background.getHeight()*(int)ConstantValues.GameParameters.SCALING, null);
        backToMenu.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        backToMenu.setMousePressed(backToMenu.isMouseOver());
    }

    public void mouseReleased(MouseEvent e) {
        backToMenu.setMouseOver(HelperPack.IsMouseOnSensableArea(backToMenu.getTouchbox(), e));
        if (backToMenu.isMouseOver()) {
            backToMenu.applyAction();
            backToMenu.resetValues();
            game.playingInitialize();
        }
    }

    public void mouseMoved(MouseEvent e) {
        backToMenu.setMouseOver(HelperPack.IsMouseOnSensableArea(backToMenu.getTouchbox(), e));
    }
}
