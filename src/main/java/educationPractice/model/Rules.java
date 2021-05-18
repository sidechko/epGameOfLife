package educationPractice.model;

/**
 Правила для игры в жизнь
 */
public class Rules {
    /**
     * Количество соседий для зарождения.
     */
    int forBirth;
    /**
     * Количество соседий для сохранения.
     */
    int forSafe;

    public Rules(int forSafe, int forBirth) {
        this.forBirth = forBirth;
        this.forSafe = forSafe;
    }

    @Override
    public String toString() {
        return String.format("Rules{%d,%d}",this.forSafe,this.forBirth);
    }
}
