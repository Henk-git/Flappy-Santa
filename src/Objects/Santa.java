package Objects;

import config.Assets;
import config.GameConfig;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Image;
import de.ur.mi.oop.graphics.Rectangle;

public class Santa {
    private final float x;
    private float y;
    private float velocity;
    private final int width;
    private final int height;
    private boolean isOnGround;
    private Image santaImage;
    public Santa(int santaStartX, int santaStartY) {
        this.x = GameConfig.SANTA_START_X;
        this.y = GameConfig.SANTA_START_Y;
        this.width = GameConfig.SANTA_WIDTH;
        this.height = GameConfig.SANTA_HEIGHT;
        this.velocity = 0;
        this.isOnGround = false;
    }

    public void draw() {
        santaImage = new Image(x, y, Assets.SANTA_IMG);
        santaImage.setHeight(this.height);
        santaImage.setWidth(this.width);
        santaImage.draw();
    }

    public void update() {
        velocity += (float) GameConfig.GRAVITY;
        y += velocity;

        // Bodenkollision (damit Santa nicht durchfällt)
        float groundLevel = GameConfig.WINDOW_HEIGHT - height; // 50 Pixel vom Boden
        if (y >= groundLevel) {
            y = groundLevel;
            velocity = 0;
            isOnGround = true;
        } else {
            isOnGround = false;
        }

        // Deckenkollision (damit Santa nicht oben rausfliegt)
        if (y <= 0) {
            y = 0;
            velocity = 0;
        }
    }



    public void jump() {
        //System.out.println("JUMP CALLED! Current velocity: " + velocity); //debugging
        velocity = (float) GameConfig.JUMP_STRENGTH;
        //System.out.println("New velocity: " + velocity);  //debugging
    }


    //Getter
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

