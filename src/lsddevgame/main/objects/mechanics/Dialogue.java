package lsddevgame.main.objects.mechanics;

import lsddevgame.main.utils.LoadData;
import org.json.simple.JSONArray;

import java.util.ArrayList;

public class Dialogue {
    private ArrayList<String> entityID = new ArrayList<>();
    private ArrayList<String> lines = new ArrayList<>();

    public Dialogue(int levelID, String dialogueID) {
        loadDialogue(levelID, dialogueID);
    }

    private void loadDialogue(int levelID, String dialogueID) {
        JSONArray jsarr = (JSONArray) LoadData.GetJSONFile("res/levels/lvl" + levelID + "/dialogues.json").get(dialogueID);
        for (Object o : jsarr) {
            JSONArray arr = (JSONArray) o;
            entityID.add((String)arr.get(0));
            lines.add((String)arr.get(1));
        };
    }

    public ArrayList<String> getLines() {
        return lines;
    }
    public String getLine(int index) {
        return lines.get(index);
    }
    public int lineCount() {
        return lines.size();
    }
    public String getEntityID(int index) {
        return entityID.get(index);
    }
}
