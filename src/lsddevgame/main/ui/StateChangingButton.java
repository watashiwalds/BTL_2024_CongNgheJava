package lsddevgame.main.ui;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.gamestates.GameState;
import lsddevgame.main.gamestates.State;
import lsddevgame.main.ui.volume.VolumeMaster;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StateChangingButton extends Button{
    private State theState;
    private int rowIndex, btnstate;
    private BufferedImage imgs[];
    private GameState state;

    public StateChangingButton(State currentStateScreen, int x, int y, int sizeNumber, BufferedImage srcImg, int rowIndex, GameState state, AudioPlayer audioPlayer) {
        super(x, y, sizeNumber, srcImg, audioPlayer);
        this.theState = currentStateScreen;
        this.rowIndex = rowIndex;
        this.state = state;
        loadSprite();
        touchboxInitialize();
    }

    @Override
    protected void loadSprite() {
        imgs = new BufferedImage[3];

        imgs[0] = srcImg.getSubimage(0, rowIndex*hdf, wdf, hdf);
        imgs[1] = srcImg.getSubimage(wdf, rowIndex*hdf, wdf, hdf);
        imgs[2] = srcImg.getSubimage(2*wdf, rowIndex*hdf, wdf, hdf);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(imgs[btnstate], x-xCenterAlign, y, w, h, null);
    }

    @Override
    public void update() {
        if (!mouseOver)
            btnstate = 0;
        else
        if (!mousePressed)
            btnstate = 1;
        else
            btnstate = 2;
    }

    @Override
    public void applyAction() {
        super.applyAction();
        theState.setGameState(this.state);
    }
}
