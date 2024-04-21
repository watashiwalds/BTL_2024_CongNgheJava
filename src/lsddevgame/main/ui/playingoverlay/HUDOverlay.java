package lsddevgame.main.ui.playingoverlay;

import lsddevgame.main.managers.ItemManager;
import lsddevgame.main.mechanics.Inventory;
import lsddevgame.main.objects.entities.Player;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HUDOverlay {
    private int padding;
    private int frameSize;
    private float hudScaling;
    private Font font;

    private Inventory inventory;
    private ItemManager itemManager;
    private BufferedImage slotFrame;

    private String message = null;
    private long startShowingMessage, nowTime, messageDuration;

    private Player player;
    private BufferedImage heartIcon;


    public HUDOverlay() {};
    public void loadLevel(Inventory inventory, ItemManager itemManager, Player player) {
        this.inventory = inventory;
        this.itemManager = itemManager;
        this.player = player;

        hudScaling = ConstantValues.GameParameters.SCALING*0.8f;

        slotFrame = LoadData.GetSpriteImage("/gui/hud/invenslot.png");
        font = LoadData.GetFont("/font/EightBitDragon.ttf").deriveFont(6*hudScaling);
        heartIcon = LoadData.GetSpriteImage("/gui/hud/hearticon.png");

        padding = (int)(8*hudScaling);
        frameSize = (int)(slotFrame.getHeight()*hudScaling);
    }

    public void showMessage(String message) {
        this.message = message;
        startMessageTimer(3000);
    }

    private void startMessageTimer(long miliseconds) {
        startShowingMessage = System.currentTimeMillis();
        messageDuration = miliseconds;
    }

    public void update() {
        nowTime = System.currentTimeMillis();
        //update for message showing
        if (nowTime - startShowingMessage > messageDuration) {
            message = null;
        }
    }

    public void draw(Graphics g) {
        int shownSlots = 0;
        g.setFont(font);
        g.setColor(Color.DARK_GRAY);
        for (int i=0; i<inventory.getSlots().length; i++) {
            if (inventory.getSlot(i) > 0) {
                g.drawImage(slotFrame, padding, padding+(frameSize*shownSlots), frameSize, frameSize, null);
                g.drawImage(itemManager.getItemSprite(i), padding+(padding/2), padding+(frameSize*shownSlots)+(padding/2), frameSize-(frameSize/3), frameSize-(frameSize/3), null);
                if (inventory.getSlot(i) > 1) {
                    g.drawString(inventory.getSlot(i)+"x", padding+4, padding+(frameSize*shownSlots)+frameSize-4);
                }
                shownSlots++;
            }
        }

        int heart = player.getHeartCount()-1;
        while (heart >= 0) {
            g.drawImage(heartIcon, ConstantValues.GameParameters.GAME_WIDTH-padding-frameSize, padding+(frameSize*heart), frameSize, frameSize, null);
            heart--;
        }

        if (message != null) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect((2*padding)+frameSize+(padding/4), padding+(padding/4), ConstantValues.GameParameters.GAME_WIDTH-(((2*padding)+frameSize)*2)-(padding/3), (int)(2.5f*padding));
            g.setColor(Color.WHITE);
            g.fillRect((2*padding)+frameSize, padding, ConstantValues.GameParameters.GAME_WIDTH-(((2*padding)+frameSize)*2)-(padding/3), (int)(2.5f*padding));
            g.setColor(Color.DARK_GRAY);
            g.drawString(message, (3*padding)+frameSize, (int)(2.6f*padding));
        }
    }
}
