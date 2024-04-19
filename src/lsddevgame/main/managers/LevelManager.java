package lsddevgame.main.managers;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.gamestates.Playing;
import lsddevgame.main.mechanics.Inventory;
import lsddevgame.main.objects.entities.BlockEntity;
import lsddevgame.main.objects.entities.Player;
import lsddevgame.main.objects.entities.ItemEntity;
import lsddevgame.main.utils.LoadData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;

import static lsddevgame.main.utils.ConstantValues.GameParameters.*;

public class LevelManager {
    private Playing gsPlaying;
    private int levelID;
    private String blockAtlas, itemAtlas, bgSrc;
    private BufferedImage background;
    private int blockTypes, itemTypes;
    private int startXIndex, startYIndex;
    private int mapW, mapH, graphicMap[][], layerMap[][];
    private Player player;
    private BlockManager blockManager;
    private ItemManager itemManager;
    private Inventory inventory;


    public LevelManager(Playing gsPlaying) {
        this.gsPlaying = gsPlaying;
    }

    public void loadLevel(int levelID) {
        this.levelID = levelID;
        loadLevelData();
    }

    public void setActivePlayer(Player player) {
        this.player = player;
    }
    private void loadLevelData() {
        String dataJSONPath = "res/levels/lvl"+levelID+"/data.json";
        JSONParser jspr = new JSONParser();
        try {
            JSONObject jsobj = (JSONObject) jspr.parse(new FileReader(dataJSONPath));

            JSONArray jsarr = (JSONArray) jsobj.get("playerStartIndex");
            startXIndex = (int)(long)jsarr.get(0);
            startYIndex = (int)(long)jsarr.get(1);

            jsarr = (JSONArray) jsobj.get("mapSize");
            mapW = (int)(long)jsarr.get(0);
            mapH = (int)(long)jsarr.get(1);

            bgSrc = (String)jsobj.get("background");
            background = LoadData.GetSpriteImage(bgSrc);

            itemAtlas = (String)jsobj.get("itemAtlas");
            itemTypes = (int)(long)jsobj.get("itemTypes");
            itemManager = new ItemManager(itemAtlas, itemTypes);
            inventory = new Inventory(itemTypes);
            jsarr = (JSONArray) jsobj.get("itemSpawns");
            for (int i=0; i<jsarr.size(); i++) {
                JSONObject obj = (JSONObject) jsarr.get(i);
                int id = (int)(long)obj.get("id");
                int xTile = (int)(long)((JSONArray)obj.get("cord")).get(0);
                int yTile = (int)(long)((JSONArray)obj.get("cord")).get(1);
                itemManager.addItemEntity(new ItemEntity(id, xTile, yTile, this, itemManager));
            }

            blockAtlas = (String)jsobj.get("blockAtlas");
            blockTypes = (int)(long)jsobj.get("blockTypes");
            blockManager = new BlockManager(blockAtlas, blockTypes);

            graphicMap = new int[mapH][mapW];
            jsarr = (JSONArray) jsobj.get("graphicMap");
            JSONArray jsrow;
            for (int i=0; i<mapH; i++) {
                jsrow = (JSONArray) jsarr.get(i);
                for (int j=0; j<mapW; j++) {
                    graphicMap[i][j] = (int)(long)jsrow.get(j);
                }
            }
            layerMap = new int[mapH][mapW];
            jsarr = (JSONArray) jsobj.get("layerMap");
            for (int i=0; i<mapH; i++) {
                jsrow = (JSONArray) jsarr.get(i);
                for (int j=0; j<mapW; j++) {
                    layerMap[i][j] = (int)(long)jsrow.get(j);
                }
            }

            jsarr = (JSONArray) jsobj.get("interactiveBlocks");
            for (int i=0; i<jsarr.size(); i++) {
                JSONObject obj = (JSONObject) jsarr.get(i);
                JSONArray arr = (JSONArray) obj.get("cord");
                int x = (int)(long)arr.get(0);
                int y = (int)(long)arr.get(1);
                String action = (String)obj.get("action");
                String message = (String)obj.get("message");
                if (action.equalsIgnoreCase("appear")) {
                    JSONArray arr2 = (JSONArray) obj.get("appearCord");
                    blockManager.addBlockEntity(new BlockEntity(graphicMap[y][x], x, y, (int)(long)obj.get("itemRequired"), (String)obj.get("action"), (int)(long)obj.get("blockIDFollowed"), message, (int)(long)arr2.get(0), (int)(long)arr2.get(1), this, blockManager));
                } else if (action.equalsIgnoreCase("dialogue")) {
                    blockManager.addBlockEntity(new BlockEntity(graphicMap[y][x], x, y, (int)(long)obj.get("itemRequired"), (String)obj.get("action"), (int)(long)obj.get("blockIDFollowed"), message, (long)obj.get("duration"), (boolean)obj.get("removeAfterAction"), this, blockManager));
                } else if (action.equalsIgnoreCase("giveItem")) {
                    blockManager.addBlockEntity(new BlockEntity(graphicMap[y][x], x, y, (int)(long)obj.get("itemRequired"), (String)obj.get("action"), (int)(long)obj.get("blockIDFollowed"), message, (int)(long)obj.get("giveItemID"), this, blockManager));
                } else {
                    blockManager.addBlockEntity(new BlockEntity(graphicMap[y][x], x, y, (int)(long)obj.get("itemRequired"), (String)obj.get("action"), (int)(long)obj.get("blockIDFollowed"), message, this, blockManager));
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!player.checkIfAlive()) gsPlaying.playerIsDead();
        for (int i=0; i<itemManager.getItemEntities().size(); i++) itemManager.getItemEntities().get(i).update(player.getHitbox());
        for (int i=0; i<blockManager.getBlockEntities().size(); i++) blockManager.getBlockEntities().get(i).update(player.getHitbox());
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        for (int i=0; i<mapH; i++) {
            for (int j=0; j<mapW; j++) {
                g.drawImage(blockManager.getBlockSprite(graphicMap[i][j]), (j*TILES_SIZE)-xLevelOffset, (i*TILES_SIZE)-yLevelOffset, TILES_SIZE, TILES_SIZE, null);
            }
        }
        for (ItemEntity iae : itemManager.getItemEntities()) iae.draw(g, xLevelOffset, yLevelOffset);
        for (BlockEntity bae : blockManager.getBlockEntities()) bae.draw(g, xLevelOffset, yLevelOffset);
    }

    public void interactionOnBlockEntity(BlockEntity bae) {
        int action = bae.getAction();

        if (action == 4) {
            //demo finished, do endgame
            gsPlaying.gameFinished();
            return;
        }

        if (action == 3) {
            //current level finished, load next level if possible
            gsPlaying.loadLevel(levelID+1);
            return;
        }

        if (bae.getItemRequiredID() != -1 && inventory.getSlot(bae.getItemRequiredID()) > 0) {
            int blockIDFollowed = bae.getBlockIDFollowed();

            if (action == 0) {
                inventory.useItem(bae.getItemRequiredID());
                gsPlaying.getGame().getAudioPlayer().playSFX(AudioPlayer.DOOROPEN);

                //locked door open, straight edit to gradientMap and layerMap, got reset upon replay
                int xTile = bae.getXTile();
                int yTile = bae.getYTile();
                graphicMap[yTile][xTile] = 0;
                layerMap[yTile][xTile] = 0;
                int i = 1;
                while (graphicMap[yTile+i][xTile] == blockIDFollowed) {
                    graphicMap[yTile+i][xTile] = 0;
                    layerMap[yTile+i][xTile] = 0;
                    i++;
                }
                i = 1;
                while (graphicMap[yTile-i][xTile] == blockIDFollowed) {
                    graphicMap[yTile-i][xTile] = 0;
                    layerMap[yTile-i][xTile] = 0;
                    i++;
                }

                if (bae.needRemoveAfterAction()) blockManager.getBlockEntities().remove(bae);
            } else

            //mechanic-related that active when using item
            if (action == 1) {
                inventory.useItem(bae.getItemRequiredID());

                int actionXMap = bae.getActionXMap();
                int actionYMap = bae.getActionYMap();
                if (graphicMap[actionYMap][actionXMap] != 0) {
                    int toremove = graphicMap[actionYMap][actionXMap];
                    int i = 1;
                    while (graphicMap[actionYMap+i][actionXMap] == toremove) {
                        graphicMap[actionYMap+i][actionXMap] = 0;
                        layerMap[actionYMap+i][actionXMap] = 0;
                        i++;
                    }
                    i = 1;
                    while (graphicMap[actionYMap-i][actionXMap] == toremove) {
                        graphicMap[actionYMap-i][actionXMap] = 0;
                        layerMap[actionYMap-i][actionXMap] = 0;
                        i++;
                    }
                }
                graphicMap[actionYMap][actionXMap] = blockIDFollowed;
                layerMap[actionYMap][actionXMap] = 1;

                if (bae.needRemoveAfterAction()) blockManager.getBlockEntities().remove(bae);
            }

            //give item on comdition met
            if (action == 5) {
                inventory.putItem(bae.getItemIDToGive());

                if (bae.needRemoveAfterAction()) blockManager.getBlockEntities().remove(bae);
            }
        } else {
            gsPlaying.getHud().showMessage(bae.getMessage());
        }
    }
    public void itemPickup(ItemEntity iae) {
        gsPlaying.getGame().getAudioPlayer().playSFX(AudioPlayer.ITEM_PICKUP);
        //id:0 item is heart, affeted player heart count
        if (iae.getId() == 0) gsPlaying.getPlayer().addHeartCount(); else inventory.putItem(iae.getId());
        itemManager.getItemEntities().remove(iae);
    }

    public int getMapWidth() {
        return mapW;
    }
    public int getMapHeight() {
        return mapH;
    }
    public int getStartXIndex() {
        return startXIndex;
    }
    public int getStartYIndex() {
        return startYIndex;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public ItemManager getItemManager() {
        return itemManager;
    }

    public Playing getGsPlaying() {
        return gsPlaying;
    }

    public int getLayerLevel(int blockX, int blockY) {
        return layerMap[blockY][blockX];
    }

    private void outputTest() {
        System.out.println("blockAtlas: " + blockAtlas);
        System.out.println("blockTypes: " + blockTypes);
        System.out.println("playerStartIndex: " + startXIndex + " " + startYIndex);
        System.out.println("mapSize: " + mapW + " " + mapH);
        for (int i=0; i<mapH; i++) {
            for (int j=0; j<mapW; j++) {
                System.out.print(graphicMap[i][j] + " ");
            }
            System.out.println();
        }
    }
}