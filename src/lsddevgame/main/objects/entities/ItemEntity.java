package lsddevgame.main.objects.entities;

import lsddevgame.main.managers.ItemManager;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.mechanics.Inventory;
import lsddevgame.main.utils.ConstantValues;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ItemEntity extends Entity {
    private int id;

    public ItemEntity(int id, int xTile, int yTile, LevelManager levelManager, ItemManager itemManager) {
        super(itemManager.getItemSprite(id), xTile*ConstantValues.GameParameters.TILES_SIZE, yTile*ConstantValues.GameParameters.TILES_SIZE, levelManager);
        this.id = id;
        xCord = xTile*ConstantValues.GameParameters.TILES_SIZE;
        yCord = yTile*ConstantValues.GameParameters.TILES_SIZE;
        super.hitboxInitialize(4* ConstantValues.GameParameters.SCALING, ConstantValues.GameParameters.SCALING, 8*ConstantValues.GameParameters.SCALING, 14*ConstantValues.GameParameters.SCALING);
    }

    public void update(Rectangle2D.Float playerHitbox) {
        if (this.hitbox.intersects(playerHitbox)) {
            levelManager.itemPickup(this);
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(srcImg, (int)xCord-xLevelOffset, (int)yCord-yLevelOffset, ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
        if (ConstantValues.GameParameters.HITBOX_DEBUG) drawHitbox(g, xLevelOffset, yLevelOffset);
    }

    public int getId() {return id;}
}
