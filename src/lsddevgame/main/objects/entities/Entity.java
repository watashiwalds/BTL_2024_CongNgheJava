package lsddevgame.main.objects.entities;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    protected float xCord, yCord;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected BufferedImage srcImg;
    protected LevelManager levelManager;
    protected AudioPlayer audioPlayer;


    public Entity(BufferedImage srcImg, float xCord, float yCord, LevelManager levelManager) {
        this.srcImg = srcImg;
        this.xCord = xCord;
        this.yCord = yCord;
        this.levelManager = levelManager;
        this.audioPlayer = levelManager.getGsPlaying().getGame().getAudioPlayer();
    }

    protected float offsetX, offsetY;
    protected void hitboxInitialize(float offsetX, float offsetY, float width, float height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        hitbox = new Rectangle2D.Float(xCord+offsetX, yCord+offsetY, width, height);
    }

    //serve only for moveable entity (player)
    protected void updateHitbox() {
        hitbox.x = (int)(xCord+offsetX);
        hitbox.y = (int)(yCord+offsetY);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    protected void drawHitbox(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.setColor(Color.orange);
        g.drawRect((int)(hitbox.x-xLevelOffset), (int)(hitbox.y-yLevelOffset), (int)hitbox.width, (int)hitbox.height);
    }
}
