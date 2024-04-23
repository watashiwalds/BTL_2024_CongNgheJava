package lsddevgame.main.managers;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.gamestates.Playing;
import lsddevgame.main.objects.mechanics.Dialogue;
import lsddevgame.main.objects.mechanics.Inventory;
import lsddevgame.main.objects.mechanics.RollbackZone;
import lsddevgame.main.objects.entities.BlockEntity;
import lsddevgame.main.objects.entities.NPC;
import lsddevgame.main.objects.entities.Player;
import lsddevgame.main.objects.entities.ItemEntity;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;

import static lsddevgame.main.utils.ConstantValues.GameParameters.*;

public class LevelManager {
    private Playing gsPlaying;
    private int levelID;
    private String blockAtlas, itemAtlas;
    private BufferedImage background;
    private int blockTypes, itemTypes;
    private int startXIndex, startYIndex;
    private int mapW, mapH, graphicMap[][], layerMap[][];
    private Player player;
    private BlockManager blockManager = null;
    private ItemManager itemManager = null;
    private Inventory inventory;
    private NPCManager npcManager = null;
    private RollbackManager rollbackManager = null;

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
        /*
        crucial required data in data.json:
            playerStartIndex[x,y]
            mapSize[w,h]
            blockAtlas, blockTypes
            graphicMap, layerMap
         else is optional
         in its most basic form, a map appear to be a blank map with blocks and player in its, no endpoint for mechanism
         */

        JSONObject jsobj = LoadData.GetJSONFile("res/levels/lvl"+levelID+"/data.json");

        //read playerStart
        JSONArray jsarr = (JSONArray) jsobj.get("playerStartIndex");
        startXIndex = (int)(long)jsarr.get(0);
        startYIndex = (int)(long)jsarr.get(1);

        //read mapSize
        jsarr = (JSONArray) jsobj.get("mapSize");
        mapW = (int)(long)jsarr.get(0);
        mapH = (int)(long)jsarr.get(1);

        //optional background
        if (jsobj.containsKey("background")) {
            background = LoadData.GetSpriteImage((String)jsobj.get("background"));
        } else {
            background = null;
        }

        //optional existence of items
        if (jsobj.containsKey("itemAtlas")) {
            itemAtlas = (String)jsobj.get("itemAtlas");
            itemTypes = (int)(long)jsobj.get("itemTypes");
            itemManager = new ItemManager(this, itemAtlas, itemTypes);
            inventory = new Inventory(itemTypes);
            //optional if item spawn on map
            if (jsobj.containsKey("itemSpawns")) {
                jsarr = (JSONArray) jsobj.get("itemSpawns");
                for (int i=0; i<jsarr.size(); i++) {
                    JSONObject obj = (JSONObject) jsarr.get(i);
                    int id = (int)(long)obj.get("id");
                    int xTile = (int)(long)((JSONArray)obj.get("cord")).get(0);
                    int yTile = (int)(long)((JSONArray)obj.get("cord")).get(1);
                    itemManager.addItemEntity(new ItemEntity(id, xTile, yTile, this, itemManager));
                }
            }
        }

        //read blocks data
        blockAtlas = (String)jsobj.get("blockAtlas");
        blockTypes = (int)(long)jsobj.get("blockTypes");
        blockManager = new BlockManager(this, blockAtlas, blockTypes);

        //required data to match with mapSize
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

        //optional mechanism block entities
        //includes next-point and endpoint, seldom blank
        if (jsobj.containsKey("interactiveBlocks")) {
            jsarr = (JSONArray) jsobj.get("interactiveBlocks");
            for (int i=0; i<jsarr.size(); i++) {
                JSONObject obj = (JSONObject) jsarr.get(i);
                JSONArray arr = (JSONArray) obj.get("cord");
                int x = (int)(long)arr.get(0);
                int y = (int)(long)arr.get(1);
                String action = (String)obj.get("action");
                BlockEntity placeholder = new BlockEntity(graphicMap[y][x], x, y, action, this, blockManager);

                if (obj.containsKey("message")) placeholder.setMessage((String)obj.get("message"));
                if (obj.containsKey("removeAfterAction")) placeholder.setRemoveAfterAction((boolean)obj.get("removeAfterAction"));
                if (obj.containsKey("id")) placeholder.setId((int)(long)obj.get("id"), blockManager);

                if (placeholder.getAction() == ConstantValues.BlockEntityAction.DISAPPEAR) {
                    placeholder.setItemRequiredID((int)(long)obj.get("itemRequired"));
                    placeholder.setBlockIDFollowed((int)(long)obj.get("blockIDFollowed"));
                } else
                if (placeholder.getAction() == ConstantValues.BlockEntityAction.APPEAR) {
                    placeholder.setItemRequiredID((int)(long)obj.get("itemRequired"));
                    placeholder.setBlockIDFollowed((int)(long)obj.get("blockIDToAppear"));
                    JSONArray arr2 = (JSONArray) obj.get("appearCord");
                    placeholder.setActionXMap((int)(long)arr2.get(0));
                    placeholder.setActionYMap((int)(long)arr2.get(1));
                } else
                if (placeholder.getAction() == ConstantValues.BlockEntityAction.DIALOGUE) {
                    placeholder.setMessageDuration((long)obj.get("duration"));
                } else
                if (placeholder.getAction() == ConstantValues.BlockEntityAction.GIVE_ITEM_CONDITION_MET) {
                    placeholder.setItemRequiredID((int)(long)obj.get("itemRequired"));
                    placeholder.setItemIDToGive((int)(long)obj.get("giveItemID"));
                } else
                if (placeholder.getAction() == ConstantValues.BlockEntityAction.WEIGHT_SENSING) {
                    placeholder.setPairID((String)obj.get("pairID"));
                    if (obj.containsKey("havePair")) placeholder.setHavePair(true);
                    if (obj.containsKey("pairMain")) {
                        placeholder.setPairMain(true);
                        placeholder.setAfterActivated((String)obj.get("afterActivated"));
                        placeholder.setNpcIDAffected((String)obj.get("npcIDAffected"));
                        placeholder.setBlockIDFollowed((int)(long)obj.get("blockIDFollowed"));
                        arr = (JSONArray) obj.get("affectedCords");
                        for (Object o : arr) {
                            placeholder.addAffectedCord((int)(long)((JSONArray)o).get(0), (int)(long)((JSONArray)o).get(1));
                        }
                    }
                    if (obj.containsKey("blockEntitiesClear")) {
                        placeholder.setBlockEntitiesClear(true);
                        arr = (JSONArray) obj.get("clearZone");
                        placeholder.setClearZone((int)(long)arr.get(0), (int)(long)arr.get(1), (int)(long)arr.get(2), (int)(long)arr.get(3));
                    }
                }

                blockManager.addBlockEntity(placeholder);
            }
        }

        //optional NPC spawning (require Dialogue)
        npcManager = new NPCManager(this);
        if (jsobj.containsKey("npcs")) {
            jsarr = (JSONArray) jsobj.get("npcs");
            for (int i=0; i<jsarr.size(); i++) {
                JSONObject obj = (JSONObject) jsarr.get(i);
                JSONArray arr = (JSONArray) obj.get("cord");
                String action = (String)obj.get("action");
                NPC placeholder = new NPC(LoadData.GetSpriteImage((String) jsobj.get("npcAtlas"), 16, 16, (int) (long) obj.get("spriteID")), (int) (long) arr.get(0), (int) (long) arr.get(1), (String) obj.get("npcID"), (String) obj.get("name"), new Dialogue(levelID, (String) obj.get("dialogueID")), action, this);

                if (obj.containsKey("removeAfterAction")) placeholder.setRemoveAfterAction((boolean)obj.get("removeAfterAction"));

                if (placeholder.getAction() == ConstantValues.NPCAction.PLAYER_AFFECT) {
                    placeholder.setAffectType((String)obj.get("affectType"));
                } else if (placeholder.getAction() == ConstantValues.NPCAction.GIVE_ITEM) {
                    placeholder.setTogiveItemID((int)(long)obj.get("itemID"));
                } else if (placeholder.getAction() == ConstantValues.NPCAction.WORLD_AFFECT) {
                    placeholder.setAffectType((String)obj.get("affectType"));
                    if (placeholder.getAffectType().equalsIgnoreCase("rollback")) {
                        placeholder.setRollbackID((String)obj.get("rollbackID"));
                    }
                }

                npcManager.addNPC(placeholder);
            }
        }

        //optional rollback zone, primarily for puzzle that can go unsolvable
        rollbackManager = new RollbackManager(this);
        if (jsobj.containsKey("rollbackZones")) {
            jsarr = (JSONArray) jsobj.get("rollbackZones");
            for (int i=0; i<jsarr.size(); i++) {
                JSONObject obj = (JSONObject) jsarr.get(i);
                JSONArray arr1 = (JSONArray) obj.get("cord");
                JSONArray arr2 = (JSONArray) obj.get("size");
                RollbackZone placeholder = new RollbackZone((String)obj.get("id"), (int)(long)arr1.get(0), (int)(long)arr1.get(1), (int)(long)arr2.get(0), (int)(long)arr2.get(1), graphicMap, layerMap, blockManager.getBlockEntities());
                rollbackManager.addRollback(placeholder);
            }
        }
    }

    public void update() {
        if (!player.isAlive()) gsPlaying.playerIsDead();
        if (!blockManager.isBlockEntitiesEmpty()) blockManager.update();
        if (!itemManager.isItemEntitiesEmpty()) itemManager.update();
        if (!npcManager.isNPCsEmpty()) npcManager.update();
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        for (int i=0; i<mapH; i++) {
            for (int j=0; j<mapW; j++) {
                g.drawImage(blockManager.getBlockSprite(graphicMap[i][j]), (j*TILES_SIZE)-xLevelOffset, (i*TILES_SIZE)-yLevelOffset, TILES_SIZE, TILES_SIZE, null);
            }
        }
        if (!blockManager.isBlockEntitiesEmpty()) blockManager.draw(g, xLevelOffset, yLevelOffset);
        if (!itemManager.isItemEntitiesEmpty()) itemManager.draw(g, xLevelOffset, yLevelOffset);
        if (!npcManager.isNPCsEmpty()) npcManager.draw(g, xLevelOffset, yLevelOffset);
    }

    public void interactionOnBlockEntity(BlockEntity bae) {
        int action = bae.getAction();

        //demo finished, do endgame
        if (action == ConstantValues.BlockEntityAction.GAME_FINISHED) {
            gsPlaying.gameFinished();
            return;
        }

        //current level finished, load next level if possible
        if (action == ConstantValues.BlockEntityAction.NEXT_LEVEL) {
            gsPlaying.loadLevel(levelID+1);
            return;
        }

        //item-required block entities
        if (bae.getItemRequiredID() != -1 && inventory.getSlot(bae.getItemRequiredID()) > 0) {
            int blockIDFollowed = bae.getBlockIDFollowed();

            //locked door open, straight edit to gradientMap and layerMap, got reset upon replay
            if (action == ConstantValues.BlockEntityAction.DISAPPEAR) {
                inventory.useItem(bae.getItemRequiredID());
                gsPlaying.getGame().getAudioPlayer().playSFX(AudioPlayer.DOOROPEN);

                int xTile = bae.getXTile();
                int yTile = bae.getYTile();
                graphicMap[yTile][xTile] = 0;
                layerMap[yTile][xTile] = 0;

                //only check vertical, may have horizontal if necessary
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

            //block appear when using item
            if (action == ConstantValues.BlockEntityAction.APPEAR) {
                inventory.useItem(bae.getItemRequiredID());

                //take appear action when interact with said block entities
                int actionXMap = bae.getActionXMap();
                int actionYMap = bae.getActionYMap();
                //followed block that has same graphic with affected previous block gets the same treatment, only vertical, expand if necessary
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

                //attempting to remove block entity from arraylist if stated
                if (bae.needRemoveAfterAction()) blockManager.getBlockEntities().remove(bae);
            }

            //give item on comdition met
            if (action == ConstantValues.BlockEntityAction.GIVE_ITEM_CONDITION_MET) {
                inventory.putItem(bae.getItemIDToGive());
                if (bae.needRemoveAfterAction()) blockManager.getBlockEntities().remove(bae);
            }
        }

        if (action == ConstantValues.BlockEntityAction.WEIGHT_SENSING) {
            String pairID = bae.getPairID();
            boolean trulyActivated = true;
            for (BlockEntity b : blockManager.getBlockEntities()) {
                if (b.isHavePair() && b.getPairID().equalsIgnoreCase(pairID)) {
                    trulyActivated = (trulyActivated && b.isBeingPressed());
                }
            }
            if (trulyActivated) {
                if (bae.getAfterActivated().equalsIgnoreCase("disappear")) {
                    int blockIDFollowed = bae.getBlockIDFollowed();
                    for (int i=0; i<bae.affectedCordCount(); i++) {
                        int xTile = bae.getAffectedCordX(i);
                        int yTile = bae.getAffectedCordY(i);
                        graphicMap[yTile][xTile] = 0;
                        layerMap[yTile][xTile] = 0;
                        int k = 1;
                        while (graphicMap[yTile+k][xTile] == blockIDFollowed) {
                            graphicMap[yTile+k][xTile] = 0;
                            layerMap[yTile+k][xTile] = 0;
                            k++;
                        }
                        k = 1;
                        while (graphicMap[yTile-k][xTile] == blockIDFollowed) {
                            graphicMap[yTile-k][xTile] = 0;
                            layerMap[yTile-k][xTile] = 0;
                            k++;
                        }
                    }
                    String npcIDAffected = bae.getNpcIDAffected();
                    for (int i=0; i<npcManager.getNpcs().size(); i++) {
                        if (npcManager.getNpcs().get(i).getId().equalsIgnoreCase(npcIDAffected)) {
                            npcManager.getNpcs().remove(npcManager.getNpcs().get(i));
                            i--;
                        }
                    }
                    if (bae.isBlockEntitiesClear()) {
                        for (int i=0; i<blockManager.getBlockEntities().size(); i++) {
                            BlockEntity b = blockManager.getBlockEntities().get(i);
                            if (b.getXTile() >= bae.getcZ_x() && b.getXTile() <= bae.getcZ_x()+bae.getcZ_w() && b.getYTile() >= bae.getcZ_y() && b.getYTile() <= bae.getcZ_y()+bae.getcZ_h()) {
                                if (graphicMap[b.getYTile()][b.getXTile()] != b.getId()) graphicMap[b.getYTile()][b.getXTile()] = b.getId();
                                blockManager.getBlockEntities().remove(b);
                                i--;
                            }
                        }
                    }
                }
            }
            return;
        }

        gsPlaying.getHud().showMessage(bae.getMessage());
    }
    public void actionOnBlockEntity(BlockEntity bae) {
        int action = bae.getAction();

        //only pushable required E to active, so for now it is in its own actionOnBlock function
        if (action == ConstantValues.BlockEntityAction.PUSHABLE) {
            if (bae.getPlayerTouchSide() == ConstantValues.Movement.LEFT && layerMap[bae.getyTile()][bae.getxTile()+1] != 1) {
                layerMap[bae.getYTile()][bae.getXTile()] = 0;
                bae.setxTile(bae.getXTile()+1);
            } else
            if (bae.getPlayerTouchSide() == ConstantValues.Movement.RIGHT && layerMap[bae.getyTile()][bae.getxTile()-1] != 1) {
                layerMap[bae.getYTile()][bae.getXTile()] = 0;
                bae.setxTile(bae.getXTile()-1);
            }
            int y = bae.getYTile();
            while (layerMap[y+1][bae.getXTile()] != 1) {
                y++;
                if (y+1 >= mapH) break;
            }
            bae.setyTile(y);
            layerMap[bae.getYTile()][bae.getXTile()] = 1;
            bae.updateLocation();
        }
    }
    public void itemPickup(ItemEntity iae) {
        gsPlaying.getGame().getAudioPlayer().playSFX(AudioPlayer.ITEM_PICKUP);
        //id:0 item is heart, affeted player heart count
        if (iae.getId() == 0)
            gsPlaying.getPlayer().addHeartCount();
        else
            inventory.putItem(iae.getId());
        itemManager.getItemEntities().remove(iae);
    }
    public void getNowPlayerInteractedWith() {
        NPC checkingNPC;
        if ((checkingNPC = npcManager.getInteractedNPC()) != null) {
            checkingNPC.doInteraction();
            gsPlaying.dialogueStart();
            return;
        }
        BlockEntity checkingBAE;
        if ((checkingBAE = blockManager.getInteractedBlockEntity()) != null) {
            if (checkingBAE.getAction() == ConstantValues.BlockEntityAction.PUSHABLE) actionOnBlockEntity(checkingBAE);
        }
    }
    public void finishNPCInteraction(NPC npc) {
        if (npc.getAction() == ConstantValues.NPCAction.PLAYER_AFFECT) {
            if (npc.getAffectType().equalsIgnoreCase("heart_increase")) {
                gsPlaying.getPlayer().addHeartCount();
            }
        } else
        if (npc.getAction() == ConstantValues.NPCAction.GIVE_ITEM) {
            inventory.putItem(npc.getTogiveItemID());
        } else
        if (npc.getAction() == ConstantValues.NPCAction.WORLD_AFFECT) {
            if (npc.getAffectType().equalsIgnoreCase("rollback")) {
                RollbackZone rz = rollbackManager.findRolllback(npc.getRollbackID());
                int k = 0, l = 0;
                for (int i=rz.getY(); i<rz.getY()+rz.getH(); i++) {
                    for (int j=rz.getX(); j<rz.getX()+rz.getW(); j++) {
                        graphicMap[i][j] = rz.getGraphicMap()[k][l];
                        layerMap[i][j] = rz.getLayerMap()[k][l];
                        l++;
                    }
                    k++;
                    l=0;
                }
                for (int i=0; i<blockManager.getBlockEntities().size(); i++) {
                    if (blockManager.getBlockEntities().get(i).getXTile() >= rz.getX() && blockManager.getBlockEntities().get(i).getXTile() < rz.getX()+rz.getW() && blockManager.getBlockEntities().get(i).getYTile() >= rz.getY() && blockManager.getBlockEntities().get(i).getYTile() < rz.getY()+rz.getH()) {
                        blockManager.getBlockEntities().remove(blockManager.getBlockEntities().get(i));
                        i--;
                    }
                }
                for (BlockEntity b : rz.getBlockEntities()) blockManager.addBlockEntity(b.clone());
            }
        }
        if (npc.needRemoveAfterAction()) {
            npcManager.getNpcs().remove(npc);
        }
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
    public Player getPlayer() {
        return player;
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