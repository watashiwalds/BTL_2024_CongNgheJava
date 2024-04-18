package lsddevgame.main.mechanics;

public class Inventory {
    private int slots[];
    public Inventory(int itemTypes) {
        slots = new int[itemTypes];
    }

    public void useItem(int itemID) {
        if (slots[itemID] > 0) {
            slots[itemID]--;
        }
    }

    public void putItem(int itemID) {
        slots[itemID]++;
    }

    public int[] getSlots() {
        return slots;
    }

    public int getSlot(int itemID) {
        return slots[itemID];
    }
}
