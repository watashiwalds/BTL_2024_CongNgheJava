package lsddevgame.main.managers;

import lsddevgame.main.objects.entities.NPC;

import java.awt.*;
import java.util.ArrayList;

public class NPCManager {
    private LevelManager levelManager;
    private ArrayList<NPC> npcs = new ArrayList<>();

    public NPCManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public void update() {
        for (int i=0; i<npcs.size(); i++) npcs.get(i).update(levelManager.getPlayer().getHitbox());
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        for (NPC npc : npcs) npc.draw(g, xLevelOffset, yLevelOffset);
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }
    public NPC getInteractedNPC() {
        for (NPC npc : npcs) if (npc.getPlayerTouched()) return npc;
        return null;
    }
}
