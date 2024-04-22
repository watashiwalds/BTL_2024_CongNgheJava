package lsddevgame.main.mechanics;

import lsddevgame.main.objects.entities.BlockEntity;

import java.util.ArrayList;

public class RollbackZone {
    private String id;
    private int x, y, w, h;
    private int[][] graphicMap, layerMap;
    private ArrayList<BlockEntity> bae = new ArrayList<>();

    public RollbackZone(String id, int x, int y, int w, int h, int[][] graphicMap, int[][]layerMap, ArrayList<BlockEntity> blockEntities) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        loadMapZone(graphicMap, layerMap);
        loadBAEInZone(blockEntities);
    }

    public void loadMapZone(int[][] graphicMap, int[][] layerMap) {
        this.graphicMap = new int[h][w];
        this.layerMap = new int[h][w];
        int k = 0, l = 0;
        for (int i=y; i<y+h; i++) {
            for (int j=x; j<x+w; j++) {
                this.graphicMap[k][l] = graphicMap[i][j];
                this.layerMap[k][l] = layerMap[i][j];
                l++;
            }
            k++;
            l=0;
        }
    }

    public void loadBAEInZone(ArrayList<BlockEntity> bae) {
        for (BlockEntity b : bae) {
            if (b.getXTile() >= x && b.getXTile() < x+w && b.getYTile() >= y && b.getYTile() < y+h) {
                this.bae.add(b);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int[][] getGraphicMap() {
        return graphicMap;
    }

    public int[][] getLayerMap() {
        return layerMap;
    }

    public ArrayList<BlockEntity> getBlockEntities() {
        return bae;
    }

    public String getId() {
        return id;
    }
}
