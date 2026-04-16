package config;

import de.ur.mi.oop.colors.Color;

public abstract class GameConfig {

    /**
     * Some Christmassy colors. Source: https://www.schemecolor.com/christmas-carol.php
     */
    public static final Color FIRE_OPAL = new Color(235, 92, 95);
    public static final Color AMERICAN_PINK = new Color(250, 149, 148);
    public static final Color LINEN = new Color(251, 242, 234);
    public static final Color FLACESCENT = new Color(242, 229, 153);
    public static final Color ARYLIDE_YELLOW = new Color(236, 217, 105);
    public static final Color DARK_SEA_GREEN = new Color(153, 211, 136);

        // --- FENSTER EINSTELLUNGEN ---
        public static final int WINDOW_WIDTH = 800;
        public static final int WINDOW_HEIGHT = 600;
        public static final String TITLE = "Flappy Santa";


        // --- SANTA (SPIELER) ---
        // Nicht zu riesig machen, sonst stößt man überall an
        public static final int SANTA_WIDTH = 90;
        public static final int SANTA_HEIGHT = 100;

        // Startposition
        public static final int SANTA_START_X = 100;
        public static final int SANTA_START_Y = WINDOW_HEIGHT / 2;

        // --- PHYSIK
        public static final double GRAVITY = 0.8;

        //  Muss NEGATIV sein
        public static final double JUMP_STRENGTH = -12.0;

        // --- ZUCKERSTANGEN (HINDERNISSE) ---
        public static final int OBSTACLE_WIDTH = 80;
        public static final int OBSTACLE_SPEED = 6;
        public static final int OBSTACLE_MAX_SPEED = 15;
        public static final int OBSTACLE_GAP = 240;

        // Wo erscheinen sie? (Rechts außerhalb des Bildschirms)
        public static final int OBSTACLE_SPAWN_X = WINDOW_WIDTH + 50;
    }

