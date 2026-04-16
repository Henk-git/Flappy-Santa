import Logic.CollisionDetector;
import Logic.GameState;
import Logic.ScoreManager;
import Objects.CandyCane;
import Objects.Santa;
import config.Assets;
import config.GameConfig;
import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Image;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;
import de.ur.mi.oop.events.KeyPressedEvent;

public class ChristmasChallenge extends GraphicsApp {
    private Santa santa;
    private CandyCane obstacle;
    private GameState currentState;
    private ScoreManager scoreManager;
    private int currentSpeed; // RICHTIGE SCHREIBWEISE!
    //score oben links
    private Image scoreImage;
    private Label scoreLabel;

    // Variable für das Hintergrundbild
    private Image backgroundImage;

    public static void main(String[] args) { // public nicht vergessen!
        GraphicsAppLauncher.launch();
    }

    @Override
    public void initialize() {
        setCanvasSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        scoreManager = new ScoreManager();

       //scoreboard
        scoreImage = new Image(10,0, Assets.SCOREBOARD_IMG);
        //text nich les bar deshlab bei export hochskaliert und jz wieder runter
        scoreImage.setWidth(150);  // Breite im Spiel
        scoreImage.setHeight(50);  // Höhe im Spiel

        scoreLabel = new Label(35, 35, "Score: 0", Colors.WHITE);
        scoreLabel.setFontSize(12);

        //background
        backgroundImage = new Image(0, 0, Assets.CANVAS_IMG);
        //richtige größe einstellen
        backgroundImage.setWidth(GameConfig.WINDOW_WIDTH);
        backgroundImage.setHeight(GameConfig.WINDOW_HEIGHT);

        //gamestart
        startGame();
    }

    private void startGame() {
        santa = new Santa(GameConfig.SANTA_START_X, GameConfig.SANTA_START_Y);
        obstacle = new CandyCane(GameConfig.OBSTACLE_SPAWN_X);
        scoreManager.reset();
        currentSpeed = GameConfig.OBSTACLE_SPEED; // WICHTIG: Initialisieren!
        currentState = GameState.PLAYING;
    }

    @Override
    public void draw() {
        backgroundImage.draw();

        if (currentState == GameState.PLAYING) {
            // UPDATE
            santa.update();
            obstacle.update(currentSpeed); // Jetzt mit Wert!

            // SCORE CHECK
            if (obstacle.hasPassed(santa.getX())) {
                scoreManager.addPoint();
                System.out.println("Score: " + scoreManager.getCurrentScore());
                increaseDifficulty();
            }

            // COLLISION CHECK
            if (CollisionDetector.checkCollision(santa, obstacle)) {
                currentState = GameState.GAME_OVER;
                System.out.println("GAME OVER! Final Score: " + scoreManager.getCurrentScore());
                System.out.println("High Score: " + scoreManager.getHighScore());
            }
        }

        // ZEICHNEN immer
        santa.draw();
        obstacle.draw();

       //scoreoboard aktualisieren und zeichnen
        scoreLabel.setText("Score: "+scoreManager.getCurrentScore());
        scoreImage.draw();
        scoreLabel.draw();

        // Game Over Screen
        if (currentState == GameState.GAME_OVER) {
            // TODO: Deathscreen
        }
    }

    public void increaseDifficulty() {
        int score = scoreManager.getCurrentScore();

        if (score % 5 == 0 && score > 0) {
            currentSpeed = currentSpeed + 1;
            if (currentSpeed > GameConfig.OBSTACLE_MAX_SPEED) {
                currentSpeed = GameConfig.OBSTACLE_MAX_SPEED;
            }
            System.out.println("Speed increased to: " + currentSpeed);
        }
    }

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        if (currentState == GameState.PLAYING) {
            if (event.getKeyCode() == KeyPressedEvent.VK_SPACE) {
                santa.jump();
            }
        } else if (currentState == GameState.GAME_OVER) {
            if (event.getKeyCode() == KeyPressedEvent.VK_SPACE) {
                startGame();
            }
        }
    }
}