package lsddevgame.main.ui.volume;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.HelperPack;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class VolumeMaster {
    private AudioPlayer audioPlayer;
    private int masterValue, musicValue, sfxValue;
    private IncDecButton masterInc, masterDec;
    private FiveStageButton musicBtn, sfxBtn;
    private BufferedImage[] displayBar = new BufferedImage[11];
    private int x_dsplBar, y_dsplBar, w_dsplBar, h_dsplBar;
    private BufferedImage textbg;
    private int textbg_x, textbg_y;

    public VolumeMaster(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;

        masterValue = 70;
        musicValue = 100;
        sfxValue = 100;
        audioPlayer.setBgMusicVolume(seekFloatVolumeValue(musicValue));
        audioPlayer.setSFXVolume(seekFloatVolumeValue(sfxValue));
        audioPlayer.setMasterVolume(seekFloatVolumeValue(masterValue));

        textbg = LoadData.GetSpriteImage("/gui/volume/textbg.png");
        textbg_x = ConstantValues.GameParameters.GAME_WIDTH/2 - (int)(textbg.getWidth()*ConstantValues.GameParameters.SCALING)/2;
        textbg_y = ConstantValues.GameParameters.GAME_HEIGHT/2 - (int)(textbg.getHeight()*ConstantValues.GameParameters.SCALING)/2;

        buttonInitialize();
    }

    private void buttonInitialize() {
        BufferedImage srcImg = LoadData.GetSpriteImage(LoadData.VOLUMEMASTER_ATLAS);

        musicBtn = new FiveStageButton((int)((ConstantValues.GameParameters.GAME_WIDTH/2)+(20*ConstantValues.GameParameters.SCALING)), (ConstantValues.GameParameters.GAME_HEIGHT/2)-(int)(20*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, 0, srcImg, 1, this, audioPlayer);
        sfxBtn = new FiveStageButton((int)((ConstantValues.GameParameters.GAME_WIDTH/2)+(20*ConstantValues.GameParameters.SCALING)), (ConstantValues.GameParameters.GAME_HEIGHT/2)-(int)(6*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, 0, srcImg, 2, this, audioPlayer);

        masterInc = new IncDecButton((int)((ConstantValues.GameParameters.GAME_WIDTH/2)+(32*ConstantValues.GameParameters.SCALING)), (ConstantValues.GameParameters.GAME_HEIGHT/2)+(int)(20*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, srcImg, true, this, audioPlayer);
        masterDec = new IncDecButton((int)((ConstantValues.GameParameters.GAME_WIDTH/2)-(32*ConstantValues.GameParameters.SCALING)), (ConstantValues.GameParameters.GAME_HEIGHT/2)+(int)(20*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, srcImg, false, this, audioPlayer);

        srcImg = LoadData.GetSpriteImage(LoadData.VOLUMEMASTER_DISPLAYBAR);
        for (int i=0; i<11; i++) {
            displayBar[i] = srcImg.getSubimage(0, 16*i, 64, 16);
        }
        x_dsplBar = (ConstantValues.GameParameters.GAME_WIDTH/2) - (int)((32)*ConstantValues.GameParameters.SCALING);
        y_dsplBar = (ConstantValues.GameParameters.GAME_HEIGHT/2) + (int)(16*ConstantValues.GameParameters.SCALING);
        w_dsplBar = (int)(displayBar[0].getWidth()*ConstantValues.GameParameters.SCALING);
        h_dsplBar = (int)(displayBar[0].getHeight()*ConstantValues.GameParameters.SCALING);
    }

    public void draw(Graphics g) {
        musicBtn.draw(g);
        sfxBtn.draw(g);
        masterInc.draw(g);
        masterDec.draw(g);
        g.drawImage(displayBar[masterValue /10], x_dsplBar, y_dsplBar, w_dsplBar, h_dsplBar, null);

        g.drawImage(textbg, textbg_x, textbg_y, (int)(textbg.getWidth()*ConstantValues.GameParameters.SCALING), (int)(textbg.getHeight()*ConstantValues.GameParameters.SCALING), null);
    }

    private float seekFloatVolumeValue(int valueInt) {
        if (valueInt != 0) return (0.01f*valueInt); else return 0;
    }

    void masterDecrease() {
        if (masterValue > 0) masterValue -= 10;
        audioPlayer.setMasterVolume(seekFloatVolumeValue(masterValue));
    }
    void masterIncrease() {
        if (masterValue < 100) masterValue += 10;
        audioPlayer.setMasterVolume(seekFloatVolumeValue(masterValue));
    }
    void musicChange() {
        musicValue -= 25;
        if (musicValue < 0) musicValue = 100;
        audioPlayer.setBgMusicVolume(seekFloatVolumeValue(musicValue));
    }
    void sfxChange() {
        sfxValue -= 25;
        if (sfxValue < 0) sfxValue = 100;
        audioPlayer.setSFXVolume(seekFloatVolumeValue(sfxValue));
    }


    public void mousePressed(MouseEvent e) {
        masterInc.setMousePressed(masterInc.isMouseOver());
        masterDec.setMousePressed(masterDec.isMouseOver());
        musicBtn.setMousePressed(musicBtn.isMouseOver());
        sfxBtn.setMousePressed(sfxBtn.isMouseOver());
    }

    public void mouseReleased(MouseEvent e) {
        masterInc.setMouseOver(HelperPack.IsMouseOnSensableArea(masterInc.getTouchbox(), e));
        if (masterInc.isMouseOver()) {
            masterInc.applyAction();
            masterInc.resetValues();
            return;
        }
        masterDec.setMouseOver(HelperPack.IsMouseOnSensableArea(masterDec.getTouchbox(), e));
        if (masterDec.isMouseOver()) {
            masterDec.applyAction();
            masterDec.resetValues();
            return;
        }
        musicBtn.setMouseOver(HelperPack.IsMouseOnSensableArea(musicBtn.getTouchbox(), e));
        if (musicBtn.isMouseOver()) {
            musicBtn.applyAction();
            musicBtn.resetValues();
            return;
        }
        sfxBtn.setMouseOver(HelperPack.IsMouseOnSensableArea(sfxBtn.getTouchbox(), e));
        if (sfxBtn.isMouseOver()) {
            sfxBtn.applyAction();
            sfxBtn.resetValues();
            return;
        }
    }

    public void mouseMoved(MouseEvent e) {
        masterInc.setMouseOver(HelperPack.IsMouseOnSensableArea(masterInc.getTouchbox(), e));
        masterDec.setMouseOver(HelperPack.IsMouseOnSensableArea(masterDec.getTouchbox(), e));
        musicBtn.setMouseOver(HelperPack.IsMouseOnSensableArea(musicBtn.getTouchbox(), e));
        sfxBtn.setMouseOver(HelperPack.IsMouseOnSensableArea(sfxBtn.getTouchbox(), e));
    }
}
