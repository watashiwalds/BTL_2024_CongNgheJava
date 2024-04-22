package lsddevgame.main.objects.entities;

import lsddevgame.main.managers.BlockManager;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.utils.ConstantValues;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BlockEntity extends Entity {
    private int id, xTile, yTile, action;
    private int itemRequiredID = -1, blockIDFollowed = -1;
    private int actionXMap = -1, actionYMap = -1;
    String message;
    private long messageDuration = 3000;
    private boolean removeAfterAction = true;
    private int itemIDToGive = -1;

    public BlockEntity(int id, int xTile, int yTile, String action, LevelManager levelManager, BlockManager blockManager) {
        super(blockManager.getBlockSprite(id), xTile*ConstantValues.GameParameters.TILES_SIZE, yTile*ConstantValues.GameParameters.TILES_SIZE, levelManager);
        this.id = id;
        this.xTile = xTile;
        this.yTile = yTile;
        defineAction(action);
        super.hitboxInitialize(-ConstantValues.GameParameters.SCALING, -ConstantValues.GameParameters.SCALING, ConstantValues.GameParameters.TILES_SIZE+(2*ConstantValues.GameParameters.SCALING), ConstantValues.GameParameters.TILES_SIZE+(2*ConstantValues.GameParameters.SCALING));
    }

    private void defineAction(String actionInText) {
        if (actionInText.equalsIgnoreCase("disappear")) this.action = ConstantValues.BlockEntityAction.DISAPPEAR; else
        if (actionInText.equalsIgnoreCase("appear")) this.action = ConstantValues.BlockEntityAction.APPEAR; else
        if (actionInText.equalsIgnoreCase("dialogue")) this.action = ConstantValues.BlockEntityAction.DIALOGUE; else
        if (actionInText.equalsIgnoreCase("giveItem")) this.action = ConstantValues.BlockEntityAction.GIVE_ITEM_CONDITION_MET; else
        if (actionInText.equalsIgnoreCase("nextLevel")) this.action = ConstantValues.BlockEntityAction.NEXT_LEVEL; else
        if (actionInText.equalsIgnoreCase("gameFinish")) this.action = ConstantValues.BlockEntityAction.GAME_FINISHED;
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

    public void setItemRequiredID(int itemRequiredID) {
        this.itemRequiredID = itemRequiredID;
    }

    public void setBlockIDFollowed(int blockIDFollowed) {
        this.blockIDFollowed = blockIDFollowed;
    }

    public void setActionXMap(int actionXMap) {
        this.actionXMap = actionXMap;
    }

    public void setActionYMap(int actionYMap) {
        this.actionYMap = actionYMap;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageDuration(long messageDuration) {
        this.messageDuration = messageDuration;
    }

    public void setRemoveAfterAction(boolean removeAfterAction) {
        this.removeAfterAction = removeAfterAction;
    }

    public void setItemIDToGive(int itemIDToGive) {
        this.itemIDToGive = itemIDToGive;
    }
}
