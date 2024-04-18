package lsddevgame.main.managers;

import lsddevgame.main.objects.entities.ItemEntity;
import lsddevgame.main.objects.items.Item;
import lsddevgame.main.utils.LoadData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ItemManager {
    private BufferedImage atlas;
    private int itemTypes;
    private ArrayList<Item> itemlist = new ArrayList<>();
    private ArrayList<ItemEntity> itemEntities = new ArrayList<>();

    public ItemManager(String imgSrc, int itemTypes) {
        atlas = LoadData.GetSpriteImage(imgSrc);
        this.itemTypes = itemTypes;
        loadCategory();
    }

    private void loadCategory() {
        int atlasRows = atlas.getWidth()/16, atlasCols = atlas.getHeight()/16;
        for (int i=0; i<atlasCols; i++) {
            for (int j=0; j<atlasRows; j++) {
                itemlist.add(new Item(this.getSprite(j, i)));
                if (itemlist.size() == itemTypes) return;
            }
        }
    }

    private BufferedImage getSprite(int atlasX, int atlasY) {
        return atlas.getSubimage(atlasX*16, atlasY*16, 16, 16);
    }

    public BufferedImage getItemSprite(int itemID) {
        return itemlist.get(itemID).getSprite();
    }

    public void addItemEntity(ItemEntity iae) {
        itemEntities.add(iae);
    }

    public ArrayList<ItemEntity> getItemEntities() {
        return itemEntities;
    }
}
