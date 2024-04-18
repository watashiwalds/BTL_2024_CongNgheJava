package lsddevgame.main;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.gamestates.*;
import lsddevgame.main.gamestates.Menu;
import lsddevgame.main.ui.volume.VolumeMaster;
import lsddevgame.main.utils.ConstantValues;

import java.awt.*;

//implement Runnable to make Game runs on multi-thread, reduce thread traffic jam
    //separate gamePanel's painting and control listeners thread
public class Game implements Runnable {

    //game's critical system entities
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameLoopThread; //gamePanel loop thread
    private Menu gsMenu;
    private Playing gsPlaying;
    private Setting gsSetting;
    private AudioPlayer audioPlayer;
    private VolumeMaster volumeMaster;

    //FPS stands for frame-per-second, determine how quick the game paint it's next panel to update its visual for player (like 60Hz n 144Hz)
    private final int FPS_SET = 60;
    //faster FPS doesn't mean faster gameplay, gameplay need to be persistent across all device, that's what UPS in use - update-per-second
    private final int UPS_SET = 60;

    public Game() {
        //prepare all game's components to rum as a game
        //ontop cuz gamePanel need player firsthand
        gameInitialize();

        //prepare all game's component to run as a program
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);

        gamePanel.requestFocus(); //all eyes on gamePanel, it's got all the inputs
        startGameLoop();
    }

    private void gameInitialize() {
        audioPlayer = new AudioPlayer();
        volumeMaster = new VolumeMaster(audioPlayer);
        gsMenu = new Menu(this);
        gsSetting = new Setting(this);
        playingInitialize();
    }

    public void playingInitialize() {
        gsPlaying = new Playing(this, 1);
    }

    //push gamePanel frame painting to a new Thread (?) looks more like push this game to a separate thread from others handler
    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    //do update every UPS time
    public void update() {
        switch (GameState.state) {
            case MENU:
                gsMenu.update();
                break;
            case PLAYING:
                gsPlaying.update();
                break;
            case SETTINGS:
                gsSetting.update();
                break;
            case QUIT:
                System.exit(0);
        }
    }
    //do rendering (paint panel) every FPS time
    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU:
                gsMenu.draw(g);
                break;
            case PLAYING:
                gsPlaying.draw(g);
                break;
            case SETTINGS:
                gsSetting.draw(g);
                break;
        }
    }

    @Override
    public void run() {
        //assign parameters for framerate control
        double frameTime = 1000000000.0 / FPS_SET;
        double updateTime = 1000000000.0 / UPS_SET;
        double  deltaFPS = 0,
                deltaUPS = 0;
        int     fpsCounter = 0,
                upsCounter = 0;
        double  nowTime,
                prevTime = System.nanoTime();
        double  lastShowStatics = System.currentTimeMillis();

        while (true) {
            //to prevent FPS/UPS loss, use double to count and fix time loss
            nowTime = System.nanoTime();
            deltaFPS += (nowTime - prevTime) / frameTime;
            deltaUPS += (nowTime - prevTime) / updateTime;
            prevTime = nowTime;

            if (deltaFPS >= 1) {
                gamePanel.repaint();
                deltaFPS--;
                fpsCounter++;
            }
            if (deltaUPS >= 1) {
                this.update();
                deltaUPS--;
                upsCounter++;
            }

            //every 1000ms = 1s, print out game's statics 'bout FPS n UPS
            if (System.currentTimeMillis()-lastShowStatics >= 1000) {
                lastShowStatics = System.currentTimeMillis();
                System.out.println("FPS: " + fpsCounter + " | UPS: " + upsCounter);
                fpsCounter = upsCounter = 0;
            }
        }
    }

    public Menu getGsMenu() {
        return gsMenu;
    }

    public Playing getGsPlaying() {
        return gsPlaying;
    }
    public Setting getGsSetting() {return gsSetting;}

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public VolumeMaster getVolumeMaster() {
        return volumeMaster;
    }

    public void windowLostFocus() {
        if (GameState.state == GameState.PLAYING) gamePanel.getGame().getGsPlaying().getPlayer().setState(ConstantValues.PlayerConstants.IDLE);
    }
}
