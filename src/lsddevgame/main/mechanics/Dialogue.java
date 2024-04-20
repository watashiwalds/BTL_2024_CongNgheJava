package lsddevgame.main.mechanics;

import lsddevgame.main.utils.LoadData;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class Dialogue {
    private ArrayList<String> lines = new ArrayList<>();

    public Dialogue(int levelID, String dialogueID) {
        loadDialogue(levelID, dialogueID);
    }

    private void loadDialogue(int levelID, String dialogueID) {
        JSONArray jsarr = (JSONArray) LoadData.GetJSONFile("res/levels/lvl" + levelID + "/dialogues.json").get(dialogueID);
        for (Object o : jsarr) lines.add((String) o);
    }

    public ArrayList<String> getLines() {
        return lines;
    }
    public String getLine(int index) {
        return lines.get(index);
    }
}
