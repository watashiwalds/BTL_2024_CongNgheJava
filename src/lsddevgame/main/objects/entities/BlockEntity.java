package lsddevgame.main.objects.entities;

import lsddevgame.main.managers.BlockManager;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.utils.ConstantValues;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BlockEntity extends Entity {
    private int id, itemRequiredID, blockIDFollowed, action;
    private int xTile, yTile;
    private int actionXMap = -1, actionYMap = -1;
    String message;
    private long messageDuration = 3000;
    private boolean removeAfterAction = true;
    private int itemIDToGive;



    public BlockEntity(int id, int xTile, int yTile, int itemRequiredID, String action, int blockIDFollowed, String message, LevelManager levelManager, BlockManager blockManager) {
        super(blockManager.getBlockSprite(id), xTile*ConstantValues.GameParameters.TILES_SIZE, yTile*ConstantValues.GameParameters.TILES_SIZE, levelManager);
        this.id = id;
        this.xTile = xTile;
        this.yTile = yTile;
        xCord = xTile*ConstantValues.GameParameters.TILES_SIZE;
        yCord = yTile*ConstantValues.GameParameters.TILES_SIZE;
        this.itemRequiredID = itemRequiredID;
        defineAction(action);
        this.blockIDFollowed = blockIDFollowed;
        this.message = message;
        super.hitboxInitialize(-ConstantValues.GameParameters.SCALING, -ConstantValues.GameParameters.SCALING, ConstantValues.GameParameters.TILES_SIZE+(2*ConstantValues.GameParameters.SCALING), ConstantValues.GameParameters.TILES_SIZE+(2*ConstantValues.GameParameters.SCALING));
    }
    public BlockEntity(int id, int xTile, int yTile, int itemRequiredID, String action, int blockIDFollowed, String message, int actionXMap, int actionYMap, LevelManager levelManager, BlockManager blockManager) {
        this(id, xTile, yTile, itemRequiredID, action, blockIDFollowed, message, levelManager, blockManager);
        this.actionXMap = actionXMap;
        this.actionYMap = actionYMap;
    }
    public BlockEntity(int id, int xTile, int yTile, int itemRequiredID, String action, int blockIDFollowed, String message, long duration, boolean removeAfterAction, LevelManager levelManager, BlockManager blockManager) {
        this(id, xTile, yTile, itemRequiredID, action, blockIDFollowed, message, levelManager, blockManager);
        this.messageDuration = duration;
        this.removeAfterAction = removeAfterAction;
    }
    public BlockEntity(int id, int xTile, int yTile, int itemRequiredID, String action, int blockIDFollowed, String message, int giveItemID, LevelManager levelManager, BlockManager blockManager) {
        this(id, xTile, yTile, itemRequiredID, action, blockIDFollowed, message, levelManager, blockManager);
        this.itemIDToGive = giveItemID;
    }

    private void defineAction(String actionInText) {
        if (actionInText.equalsIgnoreCase("disappear")) this.action = 0;
        if (actionInText.equalsIgnoreCase("appear")) this.action = 1;
        if (actionInText.equalsIgnoreCase("dialogue")) this.action = 2;
        if (actionInText.equalsIgnoreCase("nextLevel")) this.action = 3;
        if (actionInText.equalsIgnoreCase("gameFinish")) this.action = 4;
        if (actionInText.equalsIgnoreCase("giveItem")) this.action = 5;
    }

    public void update(Rectangle2D.Float playerHitbox) {
        //the blockentity is a locked door with keyhole (id:9), using key (id:10) to unlock
        if (hitbox.intersects(playerHitbox)) {
            levelManager.interactionOnBlockEntity(this);
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (ConstantValues.GameParameters.HITBOX_DEBUG) drawHitbox(g, xLevelOffset, yLevelOffset);
    }

    public int getItemRequiredID() {
        return itemRequiredID;
    }

    public int getBlockIDFollowed() {
        return blockIDFollowed;
    }

    public int getAction() {
        return action;
    }

    public int getActionXMap() {
        return actionXMap;
    }

    public int getActionYMap() {
        return actionYMap;
    }

    public int getId() {return id;}
    public int getXTile() {return xTile;}
    public int getYTile() {return yTile;}
    public String getMessage() {return message;}
    public long getMessageDuration() {
        return messageDuration;
    }
    public boolean needRemoveAfterAction() {
        return removeAfterAction;
    }

    public int getItemIDToGive() {
        return itemIDToGive;
    }
}
