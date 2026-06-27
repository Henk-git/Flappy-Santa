import Logic.CollisionDetector;
import Logic.GameState;
import Logic.ScoreManager;
import Objects.CandyCane;
import Objects.Santa;
import config.Assets;
import config.GameConfig;
import de.ur.mi.oop.app.GraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
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

    // Schneeflockenfeld für festliche Hintergrundanimation
    private Snowflake[] snowflakes;

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

        // Schneeflocken initialisieren
        snowflakes = new Snowflake[65];
        for (int i = 0; i < snowflakes.length; i++) {
            float x = (float) (Math.random() * GameConfig.WINDOW_WIDTH);
            float y = (float) (Math.random() * GameConfig.WINDOW_HEIGHT);
            float speed = (float) (0.8 + Math.random() * 1.8);
            float size = (float) (1.0 + Math.random() * 2.5);
            snowflakes[i] = new Snowflake(x, y, speed, size);
        }

        // Setup MENU state initially
        santa = new Santa(GameConfig.SANTA_START_X, GameConfig.SANTA_START_Y);
        obstacle = new CandyCane(GameConfig.OBSTACLE_SPAWN_X);
        currentState = GameState.MENU;
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

        // Schneeflocken updaten und zeichnen in allen Zuständen
        for (Snowflake flake : snowflakes) {
            flake.update();
            flake.draw();
        }

        if (currentState == GameState.MENU) {
            // Santa schwebt sanft auf und ab im Start-Menü
            float hoverY = (float) (GameConfig.WINDOW_HEIGHT / 2.0 + Math.sin(System.currentTimeMillis() / 200.0) * 18.0);
            santa.setY(hoverY);
            santa.draw();

            drawMenuOverlay();
        } else if (currentState == GameState.PLAYING) {
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

            // ZEICHNEN
            santa.draw();
            obstacle.draw();

            // scoreboard aktualisieren und zeichnen
            scoreLabel.setText("Score: " + scoreManager.getCurrentScore());
            scoreImage.draw();
            scoreLabel.draw();
        } else if (currentState == GameState.GAME_OVER) {
            // Objekte im Hintergrund eingefroren zeichnen
            santa.draw();
            obstacle.draw();

            drawGameOverOverlay();
        }
    }

    private void drawMenuOverlay() {
        float boxWidth = 520;
        float boxHeight = 360;
        float boxX = (GameConfig.WINDOW_WIDTH - boxWidth) / 2;
        float boxY = (GameConfig.WINDOW_HEIGHT - boxHeight) / 2;

        // Container mit weihnachtlichem Dunkelgrün & edlem Goldrand
        Rectangle menuBox = new Rectangle(boxX, boxY, boxWidth, boxHeight, new de.ur.mi.oop.colors.Color(15, 60, 25, 215));
        menuBox.setBorder(new de.ur.mi.oop.colors.Color(218, 165, 32), 4);
        menuBox.draw();

        // Goldener Titel
        Label titleLabel = new Label(boxX + 95, boxY + 80, "FLAPPY SANTA", new de.ur.mi.oop.colors.Color(255, 215, 0));
        titleLabel.setFontSize(38);
        titleLabel.setFont("Arial");
        titleLabel.draw();

        // Untertitel
        Label subTitleLabel = new Label(boxX + 170, boxY + 120, "Christmas Edition", Colors.WHITE);
        subTitleLabel.setFontSize(16);
        subTitleLabel.draw();

        // Highscore-Anzeige
        String highScoreTxt = "Santas Best Score: " + scoreManager.getHighScore();
        Label highScoreLabel = new Label(boxX + 145, boxY + 185, highScoreTxt, new de.ur.mi.oop.colors.Color(242, 229, 153));
        highScoreLabel.setFontSize(16);
        highScoreLabel.draw();

        // Pulsierende Start-Meldung
        if ((System.currentTimeMillis() / 600) % 2 == 0) {
            Label startLabel = new Label(boxX + 115, boxY + 255, "PRESS SPACE TO START GAME", Colors.WHITE);
            startLabel.setFontSize(18);
            startLabel.draw();
        }

        Label controlsLabel = new Label(boxX + 115, boxY + 300, "[SPACE] to make Santa climb, dodge obstacles!", new de.ur.mi.oop.colors.Color(200, 200, 200));
        controlsLabel.setFontSize(13);
        controlsLabel.draw();
    }

    private void drawGameOverOverlay() {
        // Dunkle, transparente Überlagerung des gesamten Bildschirms
        Rectangle fullOverlay = new Rectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, new de.ur.mi.oop.colors.Color(20, 0, 0, 165));
        fullOverlay.draw();

        float boxWidth = 480;
        float boxHeight = 330;
        float boxX = (GameConfig.WINDOW_WIDTH - boxWidth) / 2;
        float boxY = (GameConfig.WINDOW_HEIGHT - boxHeight) / 2;

        // Festliche rote Box mit Goldrand
        Rectangle endBox = new Rectangle(boxX, boxY, boxWidth, boxHeight, new de.ur.mi.oop.colors.Color(85, 10, 15, 230));
        endBox.setBorder(new de.ur.mi.oop.colors.Color(218, 165, 32), 4);
        endBox.draw();

        // Game Over-Schriftzug
        Label gameOverLabel = new Label(boxX + 115, boxY + 70, "GAME OVER", new de.ur.mi.oop.colors.Color(235, 92, 95));
        gameOverLabel.setFontSize(36);
        gameOverLabel.draw();

        // Ergebnis
        Label finalScoreLabel = new Label(boxX + 160, boxY + 130, "Your Score:  " + scoreManager.getCurrentScore(), Colors.WHITE);
        finalScoreLabel.setFontSize(18);
        finalScoreLabel.draw();

        // Rekord
        Label highScoreLabel = new Label(boxX + 160, boxY + 170, "High Score:  " + scoreManager.getHighScore(), new de.ur.mi.oop.colors.Color(242, 229, 153));
        highScoreLabel.setFontSize(18);
        highScoreLabel.draw();

        // Neuer Rekord Badge!
        if (scoreManager.getCurrentScore() >= scoreManager.getHighScore() && scoreManager.getCurrentScore() > 0) {
            Label newBestLabel = new Label(boxX + 140, boxY + 215, "NEW SANTA BEST! 🏆", new de.ur.mi.oop.colors.Color(236, 217, 105));
            newBestLabel.setFontSize(16);
            newBestLabel.draw();
        }

        // Pulsierende Replay-Meldung
        if ((System.currentTimeMillis() / 600) % 2 == 0) {
            Label restartLabel = new Label(boxX + 110, boxY + 270, "PRESS SPACE FOR REPLAY", Colors.WHITE);
            restartLabel.setFontSize(18);
            restartLabel.draw();
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
        if (currentState == GameState.MENU) {
            if (event.getKeyCode() == KeyPressedEvent.VK_SPACE) {
                startGame();
            }
        } else if (currentState == GameState.PLAYING) {
            if (event.getKeyCode() == KeyPressedEvent.VK_SPACE) {
                santa.jump();
            }
        } else if (currentState == GameState.GAME_OVER) {
            if (event.getKeyCode() == KeyPressedEvent.VK_SPACE) {
                startGame();
            }
        }
    }

    private class Snowflake {
        float x;
        float y;
        float speed;
        float size;

        public Snowflake(float x, float y, float speed, float size) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.size = size;
        }

        public void update() {
            y += speed;
            if (y > GameConfig.WINDOW_HEIGHT) {
                y = -10;
                x = (float) (Math.random() * GameConfig.WINDOW_WIDTH);
            }
        }

        public void draw() {
            Circle flake = new Circle(x, y, size, Colors.WHITE);
            flake.draw();
        }
    }
}