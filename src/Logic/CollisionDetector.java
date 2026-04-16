package Logic;

import Objects.CandyCane;
import Objects.Santa;
import config.GameConfig;
import de.ur.mi.oop.graphics.Rectangle;

public class CollisionDetector {
    public static boolean checkCollision(Santa santa, CandyCane obstacle) {
        // Santa's Grenzen
        float santaLeft = santa.getX();
        float santaRight = santa.getX() + santa.getWidth();
        float santaTop = santa.getY();
        float santaBottom = santa.getY() + santa.getHeight();

        // Obstacle's Grenzen
        float obstacleLeft = obstacle.getX();
        float obstacleRight = obstacle.getX() + obstacle.getWidth();

        // Ist Santa horizontal im Bereich des Hindernisses?
        boolean horizontalOverlap = santaRight > obstacleLeft && santaLeft < obstacleRight;

        if (!horizontalOverlap) {
            return false; // Keine Kollision möglich
        }

        // Trifft Santa das obere Hindernis?
        boolean hitsTopObstacle = santaTop < obstacle.getGapY();

        // Trifft Santa das untere Hindernis?
        float bottomObstacleStart = obstacle.getGapY() + obstacle.getGapSize();
        boolean hitsBottomObstacle = santaBottom > bottomObstacleStart;

        return hitsTopObstacle || hitsBottomObstacle;
    }

    // Prüft ob Santa den Boden oder die Decke berührt
    public static boolean checkBounds(Santa santa) {
        float groundLevel = GameConfig.WINDOW_HEIGHT - santa.getHeight() - 50;
        return santa.getY() >= groundLevel || santa.getY() <= 0;
    }
}
