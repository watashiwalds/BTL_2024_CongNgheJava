package lsddevgame.main.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadData {
    public static final String PLAYER_ATLAS = "/player/meow.png";
    public static final String MAINMENU_BUTTONS = "/gui/mainmenu/buttons.png";
    public static final String MAINMENU_BACKGROUND = "/gui/mainmenu/background.png";
    public static final String FULLLOGO_ATLAS = "/gui/mainmenu/logo.png";
    public static final String PAUSEOVERLAY_BACKGROUND = "/gui/pauseoverlay/background.png";
    public static final String PAUSEOVERLAY_STATEBUTTONATLAS = "/gui/pauseoverlay/statebuttons.png";
    public static final String VOLUMEMASTER_ATLAS = "/gui/volume/control.png";
    public static final String VOLUMEMASTER_DISPLAYBAR = "/gui/volume/displaybar.png";
    public static final String DEATHOVERLAY_BACKGROUND = "/gui/gameover/gameover.png";
    public static final String FINISHOVERLAY_BACKGROUND = "/gui/demofin/demofin.png";
    public static final String KEYCAP_PATH = "/gui/keycap/";

    public static BufferedImage GetSpriteImage(String imgSrc) {
        BufferedImage toreturn = null;
        InputStream is = LoadData.class.getResourceAsStream(imgSrc);
        try {
            toreturn = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return toreturn;
    }

    public static BufferedImage GetSpriteImage(String imgSrc, int width, int height, int index) {
        BufferedImage toreturn = null;
        InputStream is = LoadData.class.getResourceAsStream(imgSrc);
        try {
            BufferedImage processtemp = ImageIO.read(is);
            int xIndex = index%(processtemp.getWidth()/width);
            int yIndex = index/(processtemp.getHeight()/height);
            toreturn = processtemp.getSubimage(xIndex*width, yIndex*height, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return toreturn;
    }

    public static Font GetFont(String fontSrc) {
        Font font = null;
        InputStream is = LoadData.class.getResourceAsStream(fontSrc);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return font;
    }
}
