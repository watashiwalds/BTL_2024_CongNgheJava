package lsddevgame.main.objects.blocks;

import lsddevgame.main.utils.ConstantValues.LayerLevel.*;

import java.awt.image.BufferedImage;

import static lsddevgame.main.utils.ConstantValues.LayerLevel.*;

public class Block {
    private BufferedImage srcImg;

    public Block(BufferedImage sprite) {
        this.srcImg = sprite;
    }

    public BufferedImage getSprite() {
        return srcImg;
    }
}
