package Logic;

public class ScoreManager {
    private int currentScore;
    private int highScore;

    public ScoreManager() {
        this.currentScore = 0;
        this.highScore = 0;
    }

    public void addPoint() {
        currentScore++;
        if (currentScore > highScore) {
            highScore = currentScore;
        }
    }

    public void reset() {
        currentScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }
}