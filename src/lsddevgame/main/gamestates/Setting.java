package lsddevgame.main.gamestates;

import lsddevgame.main.Game;
import lsddevgame.main.ui.StateChangingButton;
import lsddevgame.main.ui.volume.VolumeMaster;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.HelperPack;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Setting extends State {
    private Game game;
    private VolumeMaster volumeMaster;
    private StateChangingButton back;
    private BufferedImage bg, optionbox;
    private int opbXAlign, opbYAlign;

    public Setting(Game game) {
        super(game);
        this.volumeMaster = game.getVolumeMaster();
        back = new StateChangingButton(this, (int)(16*ConstantValues.GameParameters.SCALING), (int)(8*ConstantValues.GameParameters.SCALING), ConstantValues.UIParameters.Buttons.SIZE_NANO, LoadData.GetSpriteImage("/gui/setting/back.png"), 0, GameState.MENU, game.getAudioPlayer());
        back.setFixScaling(2);
        bg = LoadData.GetSpriteImage(LoadData.MAINMENU_BACKGROUND);
        optionbox = LoadData.GetSpriteImage("/gui/setting/box.png");
        opbXAlign = (int)(optionbox.getWidth()/2*ConstantValues.GameParameters.SCALING);
        opbYAlign = (int)(optionbox.getHeight()/2*ConstantValues.GameParameters.SCALING) + (int)(8*ConstantValues.GameParameters.SCALING);
    }

    @Override
    public void update() {
        back.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bg, 0, 0, ConstantValues.GameParameters.GAME_WIDTH, ConstantValues.GameParameters.GAME_HEIGHT, null);
        g.drawImage(optionbox, (ConstantValues.GameParameters.GAME_WIDTH/2)-opbXAlign, (ConstantValues.GameParameters.GAME_HEIGHT/2)-opbYAlign, (int)(optionbox.getWidth()* ConstantValues.GameParameters.SCALING), (int)(optionbox.getHeight()*ConstantValues.GameParameters.SCALING), null);
        back.draw(g);
        volumeMaster.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        back.setMousePressed(back.isMouseOver());

        volumeMaster.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        back.setMousePressed(HelperPack.IsMouseOnSensableArea(back.getTouchbox(), e));
        if (back.isMousePressed()) back.applyAction();

        volumeMaster.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        back.setMouseOver(HelperPack.IsMouseOnSensableArea(back.getTouchbox(), e));

        volumeMaster.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
