package lsddevgame.main.ui.volume;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.ui.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IncDecButton extends Button {

    private VolumeMaster volumeMaster;
    private boolean increasing;

    public IncDecButton(int x, int y, int sizeNumber, BufferedImage srcImg, boolean isIncreaseVolume, VolumeMaster volumeMaster, AudioPlayer audioPlayer) {
        super(x, y, sizeNumber, srcImg, audioPlayer);
        this.volumeMaster = volumeMaster;
        increasing = isIncreaseVolume;
        loadSprite(srcImg);
    }

    @Override
    protected void loadSprite() {}

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(srcImg, x-xCenterAlign, y, w, h, null);
    }

    private void loadSprite(BufferedImage toCrop) {
        if (increasing) srcImg = toCrop.getSubimage(0, 16, 16, 16); else srcImg = toCrop.getSubimage(16, 16, 16, 16);
    }


    @Override
    public void update() {

    }

    @Override
    public void applyAction() {
        super.applyAction();
        if (increasing) volumeMaster.masterIncrease(); else volumeMaster.masterDecrease();
    }
}
