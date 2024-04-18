package lsddevgame.main.managers;

import lsddevgame.main.objects.blocks.Block;
import lsddevgame.main.objects.entities.BlockEntity;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BlockManager {
    private BufferedImage atlas;
    private int blockTypes;
    private ArrayList<Block> blocklist = new ArrayList<>();
    private ArrayList<BlockEntity> blockEntities = new ArrayList<>();

    public BlockManager(String imgScr, int blockTypes) {
        atlas = LoadData.GetSpriteImage(imgScr);
        this.blockTypes = blockTypes;
        loadBlocks();
    }

    private void loadBlocks() {
        blocklist.add(new Block(null));  // Air|Blank block for ID:0
        int atlasRows = atlas.getWidth()/16, atlasCols = atlas.getHeight()/16;
        for (int i=0; i<atlasCols; i++) {
            for (int j=0; j<atlasRows; j++) {
                blocklist.add(new Block(this.getSprite(j, i)));
                if (blocklist.size() == blockTypes+1) return;
            }
        }
    }

    private BufferedImage getSprite(int atlasX, int atlasY) {
        return atlas.getSubimage(atlasX*16, atlasY*16, 16, 16);
    }

    public BufferedImage getBlockSprite(int id) {
        if (id == ConstantValues.BlockIDs.AIR) return null;
        return blocklist.get(id).getSprite();
    }

    public void addBlockEntity(BlockEntity bae) {blockEntities.add(bae);}

    public ArrayList<BlockEntity> getBlockEntities() {return blockEntities;}
}
