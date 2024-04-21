package lsddevgame.main.ui.playingoverlay;

import lsddevgame.main.gamestates.Playing;
import lsddevgame.main.mechanics.Dialogue;
import lsddevgame.main.objects.entities.NPC;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class DialogueOverlay {
    private Playing gsPlaying;
    private Font font;
    private int padding, x, y, w, h;

    private NPC npc;
    private Dialogue dialogue;
    private int nowLine;
    private BufferedImage keyHint;

    private String nowEntity;
    private String dlgName;
    private BufferedImage dlgSprite;

    public DialogueOverlay(Playing gsPlaying) {
        this.gsPlaying = gsPlaying;

        float dialogueScaling = ConstantValues.GameParameters.SCALING*0.8f;
        font = LoadData.GetFont("/font/EightBitDragon.ttf").deriveFont(6*dialogueScaling);

        keyHint = LoadData.GetSpriteImage(LoadData.KEYCAP_PATH + "key_f.png");

        padding = (int)(8*ConstantValues.GameParameters.SCALING);
        x = padding*4;
        w = ConstantValues.GameParameters.GAME_WIDTH - 2*x;
        h = padding*6;
        y = ConstantValues.GameParameters.GAME_HEIGHT - h - padding;
    };

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x+(int)(4*ConstantValues.GameParameters.SCALING), y+(int)(4*ConstantValues.GameParameters.SCALING), w, h);
        g.setColor(Color.WHITE);
        g.fillRect(x, y, w, h);

        if (!dialogue.getEntityID(nowLine).equalsIgnoreCase(nowEntity)) {
            if (dialogue.getEntityID(nowLine).equalsIgnoreCase(npc.getId())) {
                dlgName = npc.getName();
                dlgSprite = npc.getSprite();
            } else if (dialogue.getEntityID(nowLine).equalsIgnoreCase("player")) {
                dlgName = "Player";
                dlgSprite = gsPlaying.getPlayer().getDialogueSprite();
            }
        }

        g.drawImage(dlgSprite, x+padding/2, y+padding/2, padding*5, padding*5, null);

        g.setFont(font);
        g.setColor(Color.DARK_GRAY);
        g.drawString(dlgName, x+padding*7, y+(int)(padding*1.5f));

        g.fillRect(x+padding*7, y+(int)(padding*1.9f), w-padding*8, (int)(ConstantValues.GameParameters.SCALING));

        int linedraw = 0;
        for (String s : dialogue.getLine(nowLine).split("\n")) {
            g.drawString(s, x + padding * 7, y + padding * 3 + padding * linedraw);
            linedraw++;
        }

        g.drawImage(keyHint, w+2*padding, ConstantValues.GameParameters.GAME_HEIGHT-3*padding, ConstantValues.GameParameters.TILES_SIZE, ConstantValues.GameParameters.TILES_SIZE, null);
    }

    public void playDialogue(NPC npc) {
        this.npc = npc;
        this.dialogue = npc.getDialogue();
        nowLine = 0;
    }

    private void nextLine() {
        nowLine++;
        if (nowLine >= dialogue.lineCount()) endDialogue();
    }

    private void endDialogue() {
        npc.doActionAfterInteraction();
        resetOverlayParameters();
        gsPlaying.dialogueFinished();
    }

    private void resetOverlayParameters() {
        npc = null;
        dialogue = null;
        nowLine = -1;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            nextLine();
        }
    }
}
