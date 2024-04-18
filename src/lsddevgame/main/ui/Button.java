package lsddevgame.main.ui;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.utils.ConstantValues;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Button {
    protected AudioPlayer audioPlayer;
    protected int xCenterAlign;
    protected float fixScaling = 1f;
    protected boolean mouseOver = false, mousePressed = false;
    protected Rectangle touchbox;
    protected int x, y, w, h, wdf, hdf;
    protected BufferedImage srcImg;

    public Button(int x, int y, int sizeNumber, BufferedImage srcImg, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = x;
        this.y = y;
        this.wdf = (int)(ConstantValues.UIParameters.Buttons.GetWidthDefault(sizeNumber)*fixScaling);
        this.hdf = (int)(ConstantValues.UIParameters.Buttons.GetHeightDefault(sizeNumber)*fixScaling);
        this.w = (int)(ConstantValues.UIParameters.Buttons.GetWidthAfterScaling(sizeNumber)*fixScaling);
        this.h = (int)(ConstantValues.UIParameters.Buttons.GetHeightAfterScaling(sizeNumber)*fixScaling);
        this.srcImg = srcImg;
        xCenterAlign = (wdf/2) * (int) ConstantValues.GameParameters.SCALING/2;
        touchboxInitialize();
    }

    protected abstract void loadSprite();

    protected void touchboxInitialize() {
        touchbox = new Rectangle(x-xCenterAlign, y, w, h);
    };

    public void draw(Graphics g) {
        if (ConstantValues.GameParameters.HITBOX_DEBUG) drawTouchbox(g);
    };

    public abstract void update();

    public void setMouseOver(boolean value) {
        this.mouseOver = value;
    }

    public boolean isMouseOver() {
        return this.mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getTouchbox() {
        return touchbox;
    }

    public void applyAction() {
        audioPlayer.playSFX(AudioPlayer.BUTTON_CLICKED);
    };

    public void setFixScaling(float value) {
        this.fixScaling = value;
        this.wdf = (int)(this.wdf*fixScaling);
        this.hdf = (int)(this.hdf*fixScaling);
        this.w = (int)(this.w*fixScaling);
        this.h = (int)(this.h*fixScaling);
        xCenterAlign = (wdf/2) * (int) ConstantValues.GameParameters.SCALING/2;
        touchboxInitialize();
    }

    public void resetValues() {
        mouseOver = false;
        mousePressed = false;
    }

    protected void drawTouchbox(Graphics g) {
        g.setColor(Color.orange);
        g.drawRect(touchbox.x, touchbox.y, touchbox.width, touchbox.height);
    }
}
