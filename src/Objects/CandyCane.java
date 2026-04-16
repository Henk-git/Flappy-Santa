package Objects;

import config.GameConfig;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Rectangle;

public class CandyCane {

    private float x;
    private float gapY;
    private int width;
    private int gapSize;
    private boolean scoored;

    public CandyCane(float startX) {
        this.x = startX;
        this.width = GameConfig.OBSTACLE_WIDTH;
        this.gapSize = GameConfig.OBSTACLE_GAP;
        randomizeGap();
    }
        //movement von rechts nach links
    public void update(int speed) {
        x -= speed;

        if (x < -width) {
            reset();
        }
    }

    private void reset() {
        x = GameConfig.OBSTACLE_SPAWN_X;
        scoored = false; //punkte zurücksetzten
        randomizeGap();
    }

    //Mehtode für score pukte ob Santa durchgeflogen ist
    public boolean hasPassed(float santaX){
        if (!scoored && santaX > x + width) {
            scoored = true;
            return true; // Punkt bekommen!
        }
        return false;
    }


    private void randomizeGap() {
        int minY = 50;
        int maxY = GameConfig.WINDOW_HEIGHT - gapSize - 50;
        gapY = (float) (minY + Math.random() * (maxY - minY));
    }

    //Rechteck Collum erstellen mit Gameconfig größen
    public void draw() {
        Rectangle topRect = new Rectangle(x, 0, width, (int) gapY, Colors.RED);
        topRect.draw();

        float bottomY = gapY + gapSize;
        float bottomHeight = GameConfig.WINDOW_HEIGHT - bottomY;

        Rectangle bottomRect = new Rectangle(x, (int) bottomY, width, (int) bottomHeight, Colors.RED);
        bottomRect.draw();
    }

    public float getX() {
        return x;
    }

    public float getGapY() {
        return gapY;
    }

    public int getWidth() {
        return width;
    }

    public int getGapSize() {
        return gapSize;
    }
}