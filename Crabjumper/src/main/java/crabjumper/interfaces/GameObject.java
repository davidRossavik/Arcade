package crabjumper.interfaces;

import javafx.scene.canvas.GraphicsContext;

public interface GameObject {
    void update(double deltaTime);
    void render(GraphicsContext gc);
    double getX();
    double getY();
    double getWidth();
    double getHeight();
}
