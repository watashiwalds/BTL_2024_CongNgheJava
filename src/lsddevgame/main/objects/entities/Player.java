package lsddevgame.main.objects.entities;

import lsddevgame.main.audio.AudioPlayer;
import lsddevgame.main.managers.LevelManager;
import lsddevgame.main.utils.ConstantValues;
import lsddevgame.main.utils.ConstantValues.*;
import lsddevgame.main.utils.HelperPack;

import java.awt.*;
import java.awt.image.BufferedImage;

import static lsddevgame.main.utils.ConstantValues.GameParameters.*;

public class Player extends Entity {
    private long nowTime;

    //jump and gravity
    private boolean jump = false, doublejump = false;
    private float airSpeed = 0f;
    private float gravity = 0.2f * SCALING;
    private float jumpSpeed = -3.5f * SCALING;
    private float doubleJumpSpeed = -2.5f * SCALING;
    private float fallSpeedIfCollision = 0.5f * SCALING;
    private boolean inAir = true;

    //player direction to move player according to pressed key on keyboard
    private boolean upDir = false, rightDir = false, downDir = false, leftDir = false;

    private float xSpeed = 0f;
    private float climbSpeed = 2f;
    private float xTile, yTile;

    private int heartCount = 1;

    private int
            playerState = PlayerConstants.IDLE,
            playerTempState = playerState; //when in cooldown, save next action in pTempState

    private boolean actionCooldown = false; //prevent quick-swap action on special action (attack, receive damage,...)
    private int cooldownDuration;
    private long cooldownTimeStart;
    private boolean dmgReceiveOnCooldown = false;
    private long dmgRcvCdDuration = 2000, dmgRcvCdStart;

    private BufferedImage[][] playerAni;
    private int
            aniTickPerFrame, //fit 1 cycle of animation in FPS mean (according to 1 second)
            aniType = 1,
            aniIndex = 0, //no of sub-images in ani sequence
            aniTickCount = 0; //game time tick counter
    private int aniDir = 0; //0 for right-faced, 1 for left-faced
    public Player(BufferedImage imgSrc, float xCord, float yCord, LevelManager levelManager) {
        super(imgSrc, xCord, yCord, levelManager);
        super.hitboxInitialize(2*SCALING, 4*SCALING, TILES_SIZE-(4*SCALING), TILES_SIZE-(4*SCALING));
        loadAnimation();
    }

    public void loadLevel() {
        xCord = levelManager.getStartXIndex()*ConstantValues.GameParameters.TILES_SIZE;
        yCord = levelManager.getStartYIndex()*ConstantValues.GameParameters.TILES_SIZE;
    }

    //updating player
    //being called *UPS* time per second
    public void update() {
        nowTime = System.currentTimeMillis(); //class-local timing stuff to use in multiple functions
        updateHitbox();
        updateAnimation();
        updatePosition();
        updateActionCooldown();
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(playerAni[aniType+(aniDir*PlayerConstants.ANIMATION_VARIATION)][aniIndex], (int)(xCord-xLevelOffset), (int)(yCord-yLevelOffset), TILES_SIZE, TILES_SIZE, null);
        if (ConstantValues.GameParameters.HITBOX_DEBUG) this.drawHitbox(g, xLevelOffset, yLevelOffset);
    }

    //handling player movement
    private void updatePosition() {
        xTile = (hitbox.x+(hitbox.width/2))/TILES_SIZE;
        yTile = (hitbox.y+(hitbox.height/2))/TILES_SIZE;

        xSpeed = 0f;

        //check on need to activate jump motion
        if (jump) {
            startJump();
        }

        //left n right moving
        if (leftDir && !rightDir) {
            setState(PlayerConstants.MOVING);
            xSpeed = -PlayerConstants.MOVE_SPEED;
        }
        else if (rightDir && !leftDir) {
            setState(PlayerConstants.MOVING);
            xSpeed = PlayerConstants.MOVE_SPEED;
        }
        else
            setState(PlayerConstants.IDLE);

        //get hurt (affect xSpeed so allowed to be in udPos) (hehe)
        if (levelManager.getLayerLevel((int)xTile, (int)yTile) == 4) {
            audioPlayer.playSFX(AudioPlayer.INWATER);
            xSpeed /= 2f;
            if (!dmgReceiveOnCooldown) {
                heartCount--;
                beingDamaged();
            }
            updateDamaging();
        }

        //climb stair (affect yCord so allowed to be in udPos)
        if (upDir && levelManager.getLayerLevel((int)xTile, (int)yTile) == 3) {
            if (HelperPack.canCollisionWithCheck(hitbox.x, hitbox.y-climbSpeed, hitbox.width, hitbox.height, airSpeed, levelManager) && !HelperPack.isInOnewayPlatform(hitbox.x, hitbox.y-climbSpeed, hitbox.width, levelManager)) {
                yCord -= climbSpeed;
                audioPlayer.playSFX(AudioPlayer.CLIMBING);
            } else {
                yCord = HelperPack.GetRoofNextToEntity(hitbox) - offsetY;
            }
            setState(PlayerConstants.CLIMBING);
            updateXPos(xSpeed);
            resetInAir();
            return;
        }

        //handle in air motion
        if (!inAir) {
            if (!HelperPack.IsEntityOnFloor(hitbox, airSpeed, levelManager)) inAir = true;
            updateXPos(xSpeed);
        } else
        if (inAir) {
            if (HelperPack.canCollisionWithCheck(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, airSpeed, levelManager)) {
                yCord += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                if (levelManager.getLayerLevel((int)xTile, (int)yTile) != 2) {
                    yCord = HelperPack.GetRoofOrFloorNextToEntity(hitbox, airSpeed);
                    if (airSpeed >= 0) {
                        resetInAir(); //hit floor? no more in air action
                    } else {
                        yCord -= offsetY; //hitbox offset with model so model need to render above where hitbox is
                        airSpeed = fallSpeedIfCollision;
                    }
                }
                else {
                    yCord += airSpeed;
                    airSpeed += gravity;
                    updateXPos(xSpeed);
                }
            }
        }
    }

    private void updateXPos(float xSpeed) {
        if (HelperPack.canCollisionWithCheck(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, airSpeed, levelManager)) {
            xCord += xSpeed;
        } else {
            xCord = HelperPack.GetWallNextToEntity(hitbox, xSpeed) - offsetX;
        }
    }

    private void startJump() {
        setState(PlayerConstants.MOVING);
        if (inAir && doublejump) return;
        if (inAir && !doublejump) {
            doublejump = true;
            airSpeed = doubleJumpSpeed;
            audioPlayer.playSFX(AudioPlayer.JUMP);
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
        audioPlayer.playSFX(AudioPlayer.JUMP);
    }
    private void getOffOnewayPlaform() {
        if (HelperPack.IsEntityOnOnewayPlaform(xCord+hitbox.width/2, yCord+hitbox.height, levelManager)) {
            yCord += hitbox.height/1.5f;
            inAir = true;
            airSpeed = 0.5f;
        }
    }
    private void resetInAir() {
        inAir = false;
        doublejump = false;
        airSpeed = 0f;
    }

    //changing player state and direction
    //intertwine with animation control, just separate ani-prefix with others
    public void setState(int state) {
        if (!actionCooldown || (state>=3 && state<=4)) {
            this.playerState = state;
            aniType = state;
            if (state>=2 && state<=4) {
                aniTickCount = 0;
                aniIndex = 0;
                resetDirection();
                setCooldown(500); //special state receive cooldown
            }
        } else {
            playerTempState = state;
        }
    }
    public void setMovement(int dir, boolean status) {
        //prevent moving when in actionCooldown
        if (actionCooldown) return;

        //set moving direction
        switch (dir) {
            case Movement.LEFT:
                leftDir = status;
                this.setState(PlayerConstants.MOVING);
                break;
            case Movement.RIGHT:
                rightDir = status;
                this.setState(PlayerConstants.MOVING);
                break;
            case Movement.UP:
                upDir = status;
                break;
            case Movement.DOWN:
                downDir = status;
                break;
            case Movement.SPACING:
                if (!downDir) startJump(); else getOffOnewayPlaform();
        }

        //define moving or idle state
        if (!rightDir && !leftDir) this.setState(PlayerConstants.IDLE);
    }
    public void resetDirection() {
        upDir = downDir = leftDir = rightDir = false;
    }

    //cooldown time for special action
    //make actionCooldown goes off in 1sec, utilize parameters from animation control
    private void updateActionCooldown() {
        if (actionCooldown && nowTime-cooldownTimeStart >= cooldownDuration) {
            actionCooldown = false;
            setState(playerTempState);
            playerTempState = PlayerConstants.IDLE;
        }
    }
    private void setCooldown(int milliseconds) {
        actionCooldown = true;
        this.cooldownDuration = milliseconds;
        cooldownTimeStart = System.currentTimeMillis();
    }

    //animating character (this time it's meowPlayer)
    private void loadAnimation() {
        //assign image's sub-images array for animation looping
        int aniLength = srcImg.getWidth()/16;
        playerAni = new BufferedImage[2*PlayerConstants.ANIMATION_VARIATION][aniLength]; //colls - rows

        //cut srcImg to individual sub-images and save in playerAni[][]
        //8 cols = y - 6 rows = x
        //2 kind of animation (left/right) for 6 playerState
        for (int k=0; k<2; k++) {
            for (int i=0; i<PlayerConstants.ANIMATION_VARIATION; i++) {
                for (int j=0; j<PlayerConstants.GetSpriteAmount(i); j++) {
                    playerAni[(k*PlayerConstants.ANIMATION_VARIATION)+i][j] = srcImg.getSubimage(j*16, ((k*PlayerConstants.ANIMATION_VARIATION)+i)*16, 16, 16);
                }
            }
        }
    }

    //animation image loop
    public void updateAnimation() {
        //assign value to animation loop duration
        //'cause player has many state -> duration has to be changed
        aniTickPerFrame = 60/PlayerConstants.GetSpriteAmount(aniType); //1 loop per sec
        if (leftDir && !rightDir) aniDir = 1; else if (rightDir && !leftDir) aniDir = 0;

        aniTickCount++;
        if (aniTickCount >= aniTickPerFrame) {
            aniType = playerState;
            aniTickCount = 0;
            aniIndex++;
            if (aniIndex >= PlayerConstants.GetSpriteAmount(aniType))
                aniIndex = 0;
        }
    }

    //handle player's heart
    public int getHeartCount() {return heartCount;}
    public void addHeartCount() {heartCount++;}
    private void beingDamaged() {
        setState(PlayerConstants.FIGHT_RECEIVE_DAMAGE);
        audioPlayer.playSFX(AudioPlayer.HURT);
        dmgReceiveOnCooldown = true;
        dmgRcvCdStart = System.currentTimeMillis();
    }
    private void updateDamaging() {
        if (dmgReceiveOnCooldown && nowTime - dmgRcvCdStart > dmgRcvCdDuration) {
            dmgReceiveOnCooldown = false;
        }
    }

    //a state where player <=0 heart, force gamer to replay the chapter
    public boolean isAlive() {
        return (heartCount>0);
    }

    public BufferedImage getDialogueSprite() {
        return playerAni[0][0];
    }
}
