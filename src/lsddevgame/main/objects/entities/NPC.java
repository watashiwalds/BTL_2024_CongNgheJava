package lsddevgame.main.objects.entities;

import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.mechanics.Dialogue;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class NPC extends Entity{

    private BufferedImage keyHint;
    private Dialogue dialogue;
    private boolean keyHintShow = false;

    public NPC(BufferedImage srcImg, int xTile, int yTile, Dialogue dialogue, LevelManager levelManager) {
        super(srcImg, xTile*ConstantValues.GameParameters.TILES_SIZE, yTile*ConstantValues.GameParameters.TILES_SIZE, levelManager);
        super.hitboxInitialize(1, 1, ConstantValues.GameParameters.TILES_SIZE-2, ConstantValues.GameParameters.TILES_SIZE-2);
        keyHint = LoadData.GetSpriteImage(LoadData.KEYCAP_PATH + "key_e.png");
        System.out.println(keyHint == null);
    }

    public void update(Rectangle2D.Float playerHitbox) {
        if (this.hitbox.intersects(playerHitbox)) {
            keyHintShow = true;
        } else {
            keyHintShow = false;
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(srcImg, (int)xCord-xLevelOffset, (int)yCord-yLevelOffset, ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
        if (keyHintShow) g.drawImage(keyHint, (int)xCord-xLevelOffset, (int)yCord-ConstantValues.GameParameters.TILES_SIZE-yLevelOffset, ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
        if (ConstantValues.GameParameters.HITBOX_DEBUG) drawHitbox(g, xLevelOffset, yLevelOffset);
    }
}
