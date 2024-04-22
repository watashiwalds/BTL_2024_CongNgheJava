package lsddevgame.main.objects.entities;

import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.mechanics.Dialogue;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class NPC extends Entity{
    private String name, id;
    private BufferedImage keyHint;
    private Dialogue dialogue;
    private boolean playerTouched = false;
    private int action = 0;
    private int togiveItemID = -1;
    private boolean removeAfterAction = false;
    private String affectType;

    public NPC(BufferedImage srcImg, int xTile, int yTile, String id, String name, Dialogue dialogue, String actionInText, LevelManager levelManager) {
        super(srcImg, xTile*ConstantValues.GameParameters.TILES_SIZE, yTile*ConstantValues.GameParameters.TILES_SIZE, levelManager);
        super.hitboxInitialize(1, 1, ConstantValues.GameParameters.TILES_SIZE-2, ConstantValues.GameParameters.TILES_SIZE-2);
        this.id = id;
        this.name = name;
        this.dialogue = dialogue;
        action = defineActionInText(actionInText);
        keyHint = LoadData.GetSpriteImage(LoadData.KEYCAP_PATH + "key_e.png");
    }
    public NPC(BufferedImage srcImg, int xTile, int yTile, String id, String name, Dialogue dialogue, String actionInText, int itemID, boolean removeAfterAction, LevelManager levelManager) {
        this(srcImg, xTile, yTile, id, name, dialogue, actionInText, levelManager);
        action = defineActionInText(actionInText);
        this.togiveItemID = itemID;
        this.removeAfterAction = removeAfterAction;
    }
    public NPC(BufferedImage srcImg, int xTile, int yTile, String id, String name, Dialogue dialogue, String actionInText, String affectType, boolean removeAfterAction, LevelManager levelManager) {
        this(srcImg, xTile, yTile, id, name, dialogue, actionInText, levelManager);
        action = defineActionInText(actionInText);
        this.affectType = affectType;
        this.removeAfterAction = removeAfterAction;
    }

    private int defineActionInText(String actionInText) {
        if (actionInText.equalsIgnoreCase("playerAffect")) return ConstantValues.NPCAction.PLAYER_AFFECT;
        if (actionInText.equalsIgnoreCase("giveItem")) return ConstantValues.NPCAction.GIVE_ITEM;
        return ConstantValues.NPCAction.TALK_ONLY;
    }

    public void update(Rectangle2D.Float playerHitbox) {
        if (this.hitbox.intersects(playerHitbox)) {
            playerTouched = true;
        } else {
            playerTouched = false;
        }
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(srcImg, (int)xCord-xLevelOffset, (int)yCord-yLevelOffset, ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
        if (playerTouched) g.drawImage(keyHint, (int)xCord-xLevelOffset, (int)yCord-ConstantValues.GameParameters.TILES_SIZE-yLevelOffset, ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
        if (ConstantValues.GameParameters.HITBOX_DEBUG) drawHitbox(g, xLevelOffset, yLevelOffset);
    }

    public boolean getPlayerTouched() {
        return playerTouched;
    }

    public void doInteraction() {
        levelManager.getGsPlaying().getDialogueOverlay().playDialogue(this);
    }

    public void doActionAfterInteraction() {
        levelManager.finishNPCInteraction(this);
    }

    public Dialogue getDialogue() {
        return dialogue;
    }
    public BufferedImage getSprite() {
        return srcImg;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public int getAction() {return action;}
    public int getTogiveItemID() {return togiveItemID;}
    public boolean needRemoveAfterAction() {return removeAfterAction;}

    public String getAffectType() {
        return affectType;
    }
}
