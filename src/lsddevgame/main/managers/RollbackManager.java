package lsddevgame.main.managers;

import lsddevgame.main.mechanics.RollbackZone;

import java.util.ArrayList;

public class RollbackManager {
    private LevelManager levelManager;
    private ArrayList<RollbackZone> rollbacks = new ArrayList<>();

    public RollbackManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void addRollback(RollbackZone rollback) {
        rollbacks.add(rollback);
    }

    public RollbackZone findRolllback(String rollbackID) {
        for (RollbackZone rz : rollbacks) {
            if (rz.getId().equalsIgnoreCase(rollbackID)) return rz;
        }
        return null;
    }
}
