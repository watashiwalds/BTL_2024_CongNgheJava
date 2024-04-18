package lsddevgame.main.ui.volume;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.ui.Button;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FiveStageButton extends Button {
    private VolumeMaster volumeMaster;
    private int stage = 0;
    private int audioType;
    private BufferedImage[] sprite = new BufferedImage[5];

    public FiveStageButton(int x, int y, int sizeNumber, int defaultStage, BufferedImage srcImg, int audioType, VolumeMaster volumeMaster, AudioPlayer audioPlayer) {
        super(x, y, sizeNumber, srcImg, audioPlayer);
        this.volumeMaster = volumeMaster;
        this.stage = defaultStage;
        this.audioType = audioType;
        loadSprite();
    }

    @Override
    protected void loadSprite() {
        sprite[0] = srcImg.getSubimage(0, 0, 16, 16);
        sprite[1] = srcImg.getSubimage(16, 0, 16, 16);
        sprite[2] = srcImg.getSubimage(32, 0, 16, 16);
        sprite[3] = srcImg.getSubimage(48, 0, 16, 16);
        sprite[4] = srcImg.getSubimage(64, 0, 16, 16);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(sprite[stage], x-xCenterAlign, y, w, h, null);
    }

    @Override
    public void update() {

    }

    @Override
    public void applyAction() {
        super.applyAction();
        stage++;
        if (stage > 4) stage = 0;
        if (audioType == 1) volumeMaster.musicChange();
        if (audioType == 2) volumeMaster.sfxChange();
    }
}
