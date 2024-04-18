package lsddevgame.main.utils;

public class ConstantValues {
    public static class UIParameters {
        public static class Buttons {
            public static final int SIZE_NANO = 0;
            public static final int SIZE_MILI = 1;
            public static final int SIZE_SMALLWIDTH = 2;
            public static final int SIZE_MEDWIDTH = 3;
            public static final int SIZE_LARGEWIDTH = 4;
            public static final int HEIGHT_DEFAULT_NANO = 16;
            public static final int HEIGHT_DEFAULT = 32;
            public static final int WIDTH_DEFAULT_NANO = 16;
            public static final int WIDTH_DEFAULT_MILI = 32;
            public static final int WIDTH_DEFAULT_SMALL = 64;
            public static final int WIDTH_DEFAULT_MED = 128;
            public static final int WIDTH_DEFAULT_LARGE = 256;
            public static final int HEIGHT_NANO = HEIGHT_DEFAULT_NANO * (int)GameParameters.SCALING/2;
            public static final int HEIGHT = HEIGHT_DEFAULT * (int)GameParameters.SCALING/2;
            public static final int WIDTH_NANO = WIDTH_DEFAULT_NANO * (int)GameParameters.SCALING/2;
            public static final int WIDTH_MILI = WIDTH_DEFAULT_MILI * (int)GameParameters.SCALING/2;
            public static final int WIDTH_SMALL = WIDTH_DEFAULT_SMALL * (int)GameParameters.SCALING/2;
            public static final int WIDTH_MED = WIDTH_DEFAULT_MED * (int)GameParameters.SCALING/2;
            public static final int WIDTH_LARGE = WIDTH_DEFAULT_LARGE * (int)GameParameters.SCALING/2;
            public static int GetWidthDefault(int sizeNumber) {
                switch (sizeNumber) {
                    case 0 : return WIDTH_DEFAULT_NANO;
                    case 1 : return WIDTH_DEFAULT_MILI;
                    case 2 : return WIDTH_DEFAULT_SMALL;
                    case 3 : return WIDTH_DEFAULT_MED;
                    case 4 : return WIDTH_DEFAULT_LARGE;
                    default: return 0;
                }
            }
            public static int GetWidthAfterScaling(int sizeNumber) {
                switch (sizeNumber) {
                    case 0 : return WIDTH_NANO;
                    case 1 : return WIDTH_MILI;
                    case 2 : return WIDTH_SMALL;
                    case 3 : return WIDTH_MED;
                    case 4 : return WIDTH_LARGE;
                    default: return 0;
                }
            }
            public static int GetHeightDefault(int sizeNumber) {
                switch (sizeNumber) {
                    case 0 : return HEIGHT_DEFAULT_NANO;
                    default: return HEIGHT_DEFAULT;
                }
            }
            public static int GetHeightAfterScaling(int sizeNumber) {
                switch (sizeNumber) {
                    case 0 : return HEIGHT_NANO;
                    default: return HEIGHT;
                }
            }
        }
    }

    public static class GameParameters {
        public static final boolean HITBOX_DEBUG = false;

        public static final float SCALING = 4f;
        public static final int
                TILES_DEFAULT_SIZE = 16,
                TILES_SIZE = (int) (TILES_DEFAULT_SIZE*SCALING),
                TILES_IN_WIDTH = 26,
                TILES_IN_HEIGHT = 14,
                GAME_WIDTH = TILES_SIZE*TILES_IN_WIDTH,
                GAME_HEIGHT = TILES_SIZE*TILES_IN_HEIGHT;
    }

    public static class Movement {
        public static final int NONE = -1;
        public static final int UP = 0;
        public static final int RIGHT = 1;
        public static final int DOWN = 2;
        public static final int LEFT = 3;
        public static final int SPACING = 4;
    }

    public static class PlayerConstants {
        public static final float MOVE_SPEED = 1f * GameParameters.SCALING;
        public static final float DIAGONAL_MOVE_SPEED = MOVE_SPEED * 0.66f;

        //by image placing - rows
        public static final int ANIMATION_VARIATION = 6;
        public static final int MOVING = 0;
        public static final int IDLE = 1;
        public static final int FIGHT_ATTACK = 2;
        public static final int FIGHT_DEALT_DAMAGE = 3;
        public static final int FIGHT_RECEIVE_DAMAGE = 4;
        public static final int CLIMBING = 5;

        public static int GetSpriteAmount(int playerAction) {
            return switch (playerAction) {
                case MOVING, IDLE, CLIMBING -> 6;
                case FIGHT_DEALT_DAMAGE -> 4;
                default -> 1;
            };
        }
    }

    public static class BlockIDs {
        public static final int AIR = 0;
    }

    public static class LayerLevel {
        public static final int BACKGROUND = 0; //behind player, able to go through
        public static final int MIDDLE = 1; //same with player, non-throughable
        public static final int TOP = 2; //in front of player, throughable
    }
}
