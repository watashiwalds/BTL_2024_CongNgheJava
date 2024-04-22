package lsddevgame.main.objects.entities;

import lsddevgame.main.managers.BlockManager;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.utils.ConstantValues;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class BlockEntity extends Entity {
    private int id, xTile, yTile, action;
    private boolean playerTouched = false;
    private int itemRequiredID = -1, blockIDFollowed = -1;
    private int actionXMap = -1, actionYMap = -1;
    String message;
    private long messageDuration = 3000;
    private boolean removeAfterAction = true;
    private int itemIDToGive = -1;
    private boolean playerTouchLeft = false;
    private boolean havePair = false, beingPressed = false, pairMain = false;
    private String pairID, afterActivated, npcIDAffected;
    private class Cords {
        int x, y;
        public Cords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private ArrayList<Cords> affectedCords = new ArrayList<>();
    private boolean blockEntitiesClear = false;
    private int cZ_x, cZ_y, cZ_w, cZ_h;

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
        if (actionInText.equalsIgnoreCase("gameFinish")) this.action = ConstantValues.BlockEntityAction.GAME_FINISHED; else
        if (actionInText.equalsIgnoreCase("pushable")) this.action = ConstantValues.BlockEntityAction.PUSHABLE; else
        if (actionInText.equalsIgnoreCase("weightSensing")) this.action = ConstantValues.BlockEntityAction.WEIGHT_SENSING;
    }

    //horrendous coding 'cause of lazy pushable and weight implementation
    public void update(Rectangle2D.Float playerHitbox) {
        super.updateHitbox();

        if (action == ConstantValues.BlockEntityAction.WEIGHT_SENSING) {
            if (levelManager.getLayerLevel(xTile, yTile) == 1) {
                beingPressed = true;
                levelManager.weightSensingActivated(this);
            }
        }
        if (hitbox.intersects(playerHitbox)) {
            playerTouched = true;
            if (action == ConstantValues.BlockEntityAction.PUSHABLE) {
                if (hitbox.contains(playerHitbox.x, playerHitbox.y) || hitbox.contains(playerHitbox.x, playerHitbox.y))
                    playerTouchLeft = false;
                else playerTouchLeft = true;
            }

            //only this is needed for all blockEntity. other codes just for pushable
            levelManager.interactionOnBlockEntity(this);

        } else {
            playerTouched = false;
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(srcImg, (int)(xCord-xLevelOffset), (int)(yCord-yLevelOffset), ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
        if (ConstantValues.GameParameters.HITBOX_DEBUG) drawHitbox(g, xLevelOffset, yLevelOffset);
    }

    //temporarily for pushable block
    public void doAction() {
        if (action == ConstantValues.BlockEntityAction.PUSHABLE) {
            if (playerTouchLeft && levelManager.getLayerLevel(xTile+1, yTile) != 1) {
                levelManager.pushableMoveFromPlace(this);
                xTile += 1;
            } else
            if (!playerTouchLeft && levelManager.getLayerLevel(xTile-1, yTile) != 1) {
                levelManager.pushableMoveFromPlace(this);
                xTile -= 1;
            }
            System.out.println(levelManager.getMapHeight());
            while (levelManager.getLayerLevel(xTile, yTile+1) != 1) {
                yTile++;
                if (yTile+1 >= levelManager.getMapHeight()) break;
            }
            xCord = xTile*ConstantValues.GameParameters.TILES_SIZE;
            yCord = yTile*ConstantValues.GameParameters.TILES_SIZE;
            levelManager.pushableMoveToPlace(this);
        }
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

    public void setId(int id, BlockManager blockManager) {
        this.id = id;
        this.srcImg = blockManager.getBlockSprite(id);
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

    public boolean isPlayerTouched() {
        return playerTouched;
    }

    public boolean isPlayerTouchLeft() {
        return playerTouchLeft;
    }

    public boolean isHavePair() {
        return havePair;
    }

    public void setHavePair(boolean havePair) {
        this.havePair = havePair;
    }

    public String getPairID() {
        return pairID;
    }

    public void setPairID(String pairID) {
        this.pairID = pairID;
    }

    public boolean isBeingPressed() {
        return beingPressed;
    }

    public boolean isPairMain() {
        return pairMain;
    }

    public void setPairMain(boolean pairMain) {
        this.pairMain = pairMain;
    }

    public String getAfterActivated() {
        return afterActivated;
    }

    public void setAfterActivated(String afterActivated) {
        this.afterActivated = afterActivated;
    }

    public String getNpcIDAffected() {
        return npcIDAffected;
    }

    public void setNpcIDAffected(String npcIDAffected) {
        this.npcIDAffected = npcIDAffected;
    }

    public void addAffectedCord(int x, int y) {
        affectedCords.add(new Cords(x, y));
    }
    public int affectedCordCount() {
        return affectedCords.size();
    }
    public int getAffectedCordX(int index) {
        return affectedCords.get(index).x;
    }
    public int getAffectedCordY(int index) {
        return affectedCords.get(index).y;
    }

    public boolean isBlockEntitiesClear() {
        return blockEntitiesClear;
    }

    public int getcZ_x() {
        return cZ_x;
    }

    public int getcZ_y() {
        return cZ_y;
    }

    public int getcZ_w() {
        return cZ_w;
    }

    public int getcZ_h() {
        return cZ_h;
    }

    public void setBlockEntitiesClear(boolean blockEntitiesClear) {
        this.blockEntitiesClear = blockEntitiesClear;
    }
    public void setClearZone(int x, int y, int w, int h) {
        cZ_x = x;
        cZ_y = y;
        cZ_w = w;
        cZ_h = h;
    }
}
