package lsddevgame.main.gamestates;

import lsddevgame.main.Game;
import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.objects.entities.Player;
import lsddevgame.main.ui.playingoverlay.DeathOverlay;
import lsddevgame.main.ui.playingoverlay.FinishOverlay;
import lsddevgame.main.ui.playingoverlay.HUDOverlay;
import lsddevgame.main.ui.playingoverlay.PauseOverlay;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.LoadData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static lsddevgame.main.utils.ConstantValues.Movement.*;
import static lsddevgame.main.utils.ConstantValues.Movement.RIGHT;

public class Playing extends State {
    private Player player;
    private LevelManager levelManager;
    private boolean pause = false, death = false, finish = false;
    private HUDOverlay hud;
    private PauseOverlay pauseOverlay;
    private DeathOverlay deathOverlay;
    private FinishOverlay finishOverlay;
    private int levelID;
    private int xLevelOffset, maxXLevelOffset, maxWidthTileOffset, leftBorder, rightBorder;
    private int yLevelOffset, maxYLevelOffset, maxHeightTileOffset, ceilBorder, floorBorder;

    public Playing(Game game, int levelID) {
        super(game);
        this.levelID = levelID;
        gameInitialize();
    }

    private void gameInitialize() {
        pauseOverlay = new PauseOverlay(this.game);
        deathOverlay = new DeathOverlay(this.game);
        finishOverlay = new FinishOverlay(this.game);
        levelManager = new LevelManager(this);
        player = new Player(LoadData.GetSpriteImage(LoadData.PLAYER_ATLAS), 0, 0, levelManager);
        levelManager.setActivePlayer(player);
        hud = new HUDOverlay();
        loadLevel(levelID);
//        player = new Player(LoadData.PLAYER_ATLAS, levelManager.getStartXIndex()*ConstantValues.GameParameters.TILES_SIZE, levelManager.getStartYIndex()*ConstantValues.GameParameters.TILES_SIZE, levelManager);
    }

    public void loadLevel(int levelID) {
        levelManager.loadLevel(levelID);
        player.loadLevel();
        hud.loadLevel(levelManager.getInventory(), levelManager.getItemManager(), player);
        initializeOffset();
    }

    private void initializeOffset() {
        xLevelOffset = 0;
        leftBorder = (int)(0.4f*ConstantValues.GameParameters.GAME_WIDTH);
        rightBorder = (int)(0.6f*ConstantValues.GameParameters.GAME_WIDTH);
        maxWidthTileOffset = levelManager.getMapWidth() - ConstantValues.GameParameters.TILES_IN_WIDTH;
        maxXLevelOffset = maxWidthTileOffset * ConstantValues.GameParameters.TILES_SIZE;

        yLevelOffset = 0;
        ceilBorder = (int)(0.4f*ConstantValues.GameParameters.GAME_HEIGHT);
        floorBorder = (int)(0.6f*ConstantValues.GameParameters.GAME_HEIGHT);
        maxHeightTileOffset = levelManager.getMapHeight() - ConstantValues.GameParameters.TILES_IN_HEIGHT;
        maxYLevelOffset = maxHeightTileOffset * ConstantValues.GameParameters.TILES_SIZE;
    }

    private void adjustOffset() {
        int playerX = (int)player.getHitbox().x;
        int different = playerX - xLevelOffset;
        if (different > rightBorder) {
            if (xLevelOffset < maxXLevelOffset) {
                xLevelOffset += different - rightBorder;
                if (xLevelOffset > maxXLevelOffset) xLevelOffset = maxXLevelOffset;
            }
        } else if (different < leftBorder) {
            if (xLevelOffset > 0) {
                xLevelOffset -= leftBorder - different;
                if (xLevelOffset < 0) xLevelOffset = 0;
            }
        }

        int playerY = (int)player.getHitbox().y;
        different = playerY - yLevelOffset;
        if (different < ceilBorder) {
            if (yLevelOffset > 0) {
                yLevelOffset -= ceilBorder - different;
                if (yLevelOffset < 0) yLevelOffset = 0;
            }
        } else if (different > floorBorder) {
            if (yLevelOffset < maxYLevelOffset) {
                yLevelOffset += different - floorBorder;
                if (yLevelOffset > maxYLevelOffset) yLevelOffset = maxYLevelOffset;
            }
        }
    }

    @Override
    public void update() {
        if (!pause && !death && !finish) {
            player.update();
            levelManager.update();
            adjustOffset();
            hud.update();
            return;
        }
        if (pause) pauseOverlay.update(); else
        if (death) deathOverlay.update(); else
        if (finish) finishOverlay.update();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g, xLevelOffset, yLevelOffset);
        player.draw(g, xLevelOffset, yLevelOffset);
        hud.draw(g);
        if (pause) pauseOverlay.draw(g); else
        if (death) deathOverlay.draw(g); else
        if (finish) finishOverlay.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (death) deathOverlay.mousePressed(e); else
        if (pause) pauseOverlay.mousePressed(e); else
        if (finish) finishOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (death) deathOverlay.mouseReleased(e); else
        if (pause) pauseOverlay.mouseReleased(e); else
        if (finish) finishOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (death) deathOverlay.mouseMoved(e); else
        if (pause) pauseOverlay.mouseMoved(e); else
        if (finish) finishOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pause && !death) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setMovement(UP, true);
                    break;
                case KeyEvent.VK_A:
                    player.setMovement(LEFT, true);
                    break;
                case KeyEvent.VK_S:
                    player.setMovement(DOWN, true);
                    break;
                case KeyEvent.VK_D:
                    player.setMovement(RIGHT, true);
                    break;
//            case KeyEvent.VK_E :
//                player.setState(FIGHT_ATTACK);
//                break;
//            case KeyEvent.VK_C:
//                player.setState(FIGHT_DEALT_DAMAGE);
//                break;
//            case KeyEvent.VK_V:
//                player.setState(FIGHT_RECEIVE_DAMAGE);
//                break;
                case KeyEvent.VK_SPACE:
                    player.setMovement(SPACING, true);
                    break;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !death) {
            this.pause = !this.pause;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!pause && !death) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setMovement(UP, false);
                    break;
                case KeyEvent.VK_A:
                    player.setMovement(LEFT, false);
                    break;
                case KeyEvent.VK_S:
                    player.setMovement(DOWN, false);
                    break;
                case KeyEvent.VK_D:
                    player.setMovement(RIGHT, false);
                    break;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public PauseOverlay getPauseOverlay() {
        return pauseOverlay;
    }

    public HUDOverlay getHud() {return hud;}

    public boolean isPause() {
        return pause;
    }
    public void unPause() {
        pause = false;
    }
    public void playerIsDead() {
        death = true;
        game.getAudioPlayer().stopBgMusic();
        game.getAudioPlayer().playSFX(AudioPlayer.GAME_OVER);
    }
    public void gameFinished() {
        finish = true;
        game.getAudioPlayer().stopBgMusic();
        game.getAudioPlayer().playSFX(AudioPlayer.DEMO_FINISHED);
    }
}
