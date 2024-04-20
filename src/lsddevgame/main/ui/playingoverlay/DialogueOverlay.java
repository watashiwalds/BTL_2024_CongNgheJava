package lsddevgame.main.ui.playingoverlay;

import lsddevgame.main.mechanics.Dialogue;
import lsddevgame.main.objects.entities.NPC;

public class DialogueOverlay {
    private NPC npc;
    private Dialogue dialogue;

    public DialogueOverlay() {};

    public void loadDialogue(NPC npc, Dialogue dialogue) {
        this.npc = npc;
        this.dialogue = dialogue;

    }
}
