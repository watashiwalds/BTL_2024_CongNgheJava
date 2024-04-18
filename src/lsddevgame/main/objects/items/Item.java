package lsddevgame.main.objects.items;

import java.awt.image.BufferedImage;

public class Item {
    private BufferedImage srcImg;

    public Item(BufferedImage sprite) {
        this.srcImg = sprite;
    }

    public BufferedImage getSprite() {
        return srcImg;
    }
}

