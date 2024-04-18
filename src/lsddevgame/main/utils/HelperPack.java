package lsddevgame.main.utils;

import lsddevgame.main.managers.LevelManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;

import static lsddevgame.main.utils.ConstantValues.*;

public class HelperPack {

    //airspeed serve only for player, 0 for all else
    public static boolean canCollisionWithCheck(float x, float y, float w, float h, float airSpeed, LevelManager levelManager) {
        if (!isSolidCheck(x, y, airSpeed, levelManager))
            if (!isSolidCheck(x+w, y+h, airSpeed, levelManager))
                if (!isSolidCheck(x+w, y, airSpeed, levelManager))
                    if (!isSolidCheck(x, y+h, airSpeed, levelManager))
                        return true;
        return false;
    }
    private static boolean isSolidCheck(float x, float y, float airSpeed, LevelManager levelManager) {
        //window boundary
        if (x < 0 || x >= (levelManager.getMapWidth() * GameParameters.TILES_SIZE)) return true;
        if (y < 0 || y >= (levelManager.getMapHeight() * GameParameters.TILES_SIZE)) return true;

        float mapX = x / GameParameters.TILES_SIZE;
        float mapY = y / GameParameters.TILES_SIZE;

        int oncheckLayer = levelManager.getLayerLevel((int) mapX, (int) mapY);
        return (oncheckLayer == LayerLevel.MIDDLE || onewaySolidCheck((int)mapX, (int)mapY, airSpeed, levelManager));
    }
    private static boolean onewaySolidCheck(int mapX, int mapY, float airSpeed, LevelManager levelManager) {
        return (levelManager.getLayerLevel(mapX, mapY) == 2 && airSpeed >= 0);
    }

    public static float GetWallNextToEntity(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int)(hitbox.x+(hitbox.width/2))/GameParameters.TILES_SIZE;
        if (xSpeed > 0) {
            //goto right
            return (currentTile * GameParameters.TILES_SIZE) + (GameParameters.TILES_SIZE-hitbox.width) -1;
        } else {
            //goto left
            return (currentTile * GameParameters.TILES_SIZE);
        }
    }
    public static float GetRoofOrFloorNextToEntity(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int)(hitbox.y+(hitbox.height/2))/GameParameters.TILES_SIZE;
        if (airSpeed < 0) {
            //air and up
            return (currentTile * GameParameters.TILES_SIZE) + 1;
        } else {
            //air and down
            return (currentTile * GameParameters.TILES_SIZE) - 1;
        }
    }
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, float airSpeed, LevelManager levelManager) {
        if (!isSolidCheck(hitbox.x+hitbox.width+1, hitbox.y+hitbox.height+1, airSpeed, levelManager))
            if (!isSolidCheck(hitbox.x, hitbox.y+hitbox.height+1, airSpeed, levelManager))
                return false;
        return true;
    }
    public static boolean IsEntityOnOnewayPlaform(float entityXMid, float entityYMid_includeHeight, LevelManager levelManager) {
        int mapX = (int)(entityXMid/GameParameters.TILES_SIZE);
        int mapY = (int)((entityYMid_includeHeight-1)/GameParameters.TILES_SIZE);
        return (levelManager.getLayerLevel(mapX, mapY+1) == 2);
    }
    public static boolean IsMouseOnSensableArea(Rectangle area, MouseEvent theMouse) {
        return area.contains(theMouse.getX(), theMouse.getY());
    }
}
