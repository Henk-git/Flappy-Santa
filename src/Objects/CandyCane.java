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
        drawCane(x, 0, width, (int) gapY, true);

        float bottomY = gapY + gapSize;
        float bottomHeight = GameConfig.WINDOW_HEIGHT - bottomY;
        drawCane(x, (int) bottomY, width, (int) bottomHeight, false);
    }

    private void drawCane(float x, float y, int width, int height, boolean isTop) {
        // 1. Hintergrund / Grundfarbe
        Rectangle base = new Rectangle(x, y, width, height, Colors.RED);
        base.draw();

        // 2. Weisse Streifen zeichnen
        int stripeHeight = 25;
        for (int currY = 0; currY < height; currY += stripeHeight * 2) {
            int h1 = Math.min(stripeHeight, height - currY);
            if (h1 > 0) {
                Rectangle stripe = new Rectangle(x, y + currY, width, h1, Colors.WHITE);
                stripe.draw();
            }
        }

        // 3. Grünes Band / Cap (Forest Green: 34, 139, 34) am Ausgang
        int capHeight = 16;
        float capY = isTop ? (y + height - capHeight) : y;
        Rectangle cap = new Rectangle(x - 3, capY, width + 6, capHeight, new de.ur.mi.oop.colors.Color(34, 139, 34));
        cap.setBorder(Colors.BLACK, 2);
        cap.draw();

        // 4. Goldenes Band auf dem grünen Band (Gold: 218, 165, 32)
        float goldY = isTop ? (capY + 6) : (capY + 6);
        Rectangle goldBand = new Rectangle(x - 3, goldY, width + 6, 4, new de.ur.mi.oop.colors.Color(218, 165, 32));
        goldBand.draw();

        // 5. Schwarze Umrandung um das ganze Hindernis für Schärfe
        Rectangle outline = new Rectangle(x, y, width, height, Colors.TRANSPARENT);
        outline.setBorder(Colors.BLACK, 3);
        outline.draw();
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