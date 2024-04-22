package lsddevgame.main.ui.playingoverlay;

import lsddevgame.main.Game;
import lsddevgame.main.gamestates.GameState;
import lsddevgame.main.ui.StateChangingButton;
import lsddevgame.main.ui.volume.VolumeMaster;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.HelperPack;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay {
    private BufferedImage background;
    private int bgCenterXAlign, bgCenterYAlign;
    private StateChangingButton statebtn[] = new StateChangingButton[3];
    private Game game;
    private VolumeMaster volumeMaster;

    public PauseOverlay(Game game) {
        this.game = game;
        this.volumeMaster = game.getVolumeMaster();
        loadImages();
        buttonInitialize();
    }

    private void loadImages() {
        background = LoadData.GetSpriteImage(LoadData.PAUSEOVERLAY_BACKGROUND);
        bgCenterXAlign = (ConstantValues.GameParameters.GAME_WIDTH/2)-(background.getWidth()/2)*(int)ConstantValues.GameParameters.SCALING;
        bgCenterYAlign = (ConstantValues.GameParameters.GAME_HEIGHT/2)-(background.getHeight()/2)*(int)ConstantValues.GameParameters.SCALING;
    }

    private void buttonInitialize() {
        BufferedImage srcImg = LoadData.GetSpriteImage(LoadData.PAUSEOVERLAY_STATEBUTTONATLAS);

        statebtn[0] = new StateChangingButton(game.getGsPlaying(), (ConstantValues.GameParameters.GAME_WIDTH/2)+(int)(-24*ConstantValues.GameParameters.SCALING), (ConstantValues.GameParameters.GAME_HEIGHT/2)+(int)(38*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, srcImg, 0, GameState.MENU, game.getAudioPlayer());
        statebtn[1] = new StateChangingButton(game.getGsPlaying(), ConstantValues.GameParameters.GAME_WIDTH/2, (ConstantValues.GameParameters.GAME_HEIGHT/2)+(int)(38*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, srcImg, 2, GameState.PLAYING, game.getAudioPlayer());
        statebtn[2] = new StateChangingButton(game.getGsPlaying(), (ConstantValues.GameParameters.GAME_WIDTH/2)+(int)(24*ConstantValues.GameParameters.SCALING), (ConstantValues.GameParameters.GAME_HEIGHT/2)+(int)(38*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, srcImg, 1, GameState.RESET, game.getAudioPlayer());

        for (StateChangingButton x : statebtn) x.setFixScaling(2);
    }

    public void update() {
        for (StateChangingButton x : statebtn) x.update();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, ConstantValues.GameParameters.GAME_WIDTH, ConstantValues.GameParameters.GAME_HEIGHT);
        g.drawImage(background, bgCenterXAlign, bgCenterYAlign, background.getWidth()*(int)ConstantValues.GameParameters.SCALING, background.getHeight()*(int)ConstantValues.GameParameters.SCALING, null);
        for (StateChangingButton x : statebtn) x.draw(g);
        volumeMaster.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        for (StateChangingButton x : statebtn) {
            x.setMousePressed(x.isMouseOver());
        }
        volumeMaster.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        for (StateChangingButton x : statebtn) {
            x.setMouseOver(HelperPack.IsMouseOnSensableArea(x.getTouchbox(), e));
            if (x.isMouseOver()) {
                x.applyAction();
                x.resetValues();
                if (GameState.state == GameState.RESET) {
                    game.playingInitialize();
                    game.getGsPlaying().setGameState(GameState.PLAYING);
                    game.getGsPlaying().backToPlaying();
                } else if (GameState.state == GameState.PLAYING) {
                    game.getGsPlaying().unPause();
                }
                break;
            }
            x.resetValues();
        }
        volumeMaster.mouseReleased(e);
    }

    public void mouseMoved(MouseEvent e) {
        for (StateChangingButton x : statebtn)
            x.setMouseOver(HelperPack.IsMouseOnSensableArea(x.getTouchbox(), e));
        volumeMaster.mouseMoved(e);
    }
}
