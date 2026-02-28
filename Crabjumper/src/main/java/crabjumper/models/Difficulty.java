package crabjumper.models;

public enum Difficulty {
    EASY(100, -500),
    MEDIUM(200, -600),
    HARD(300, -702);

    public final double platformGap;
    public final double jumpStrength;

    Difficulty(double platformGap, double jumpStrength) {
        this.platformGap = platformGap;
        this.jumpStrength = jumpStrength;
    }
}
