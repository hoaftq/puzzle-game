package hoaftq.puzzle.info;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class GameInfoView {
    private int elapsedTime;
    private int step;

    private final Timer timer;
    private final Numbers numbers;

    private Consumer<Integer> tickListener;

    public GameInfoView() throws IOException {
        numbers = new Numbers("numbers.gif");
        timer = new Timer(1000, e -> {
            elapsedTime++;
            if (tickListener != null) {
                tickListener.accept(elapsedTime);
            }
        });
    }

    public void registerTickListener(Consumer<Integer> tickListener) {
        this.tickListener = tickListener;
    }

    public void increaseStep() {
        step++;
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void reset() {
        step = 0;
        elapsedTime = 0;
    }

    public void paint(Graphics g, int width, int y1, int y2) {

        // Draw elapsed time
        numbers.drawNumber(g, 10, y1, y2, elapsedTime, 4);

        // Draw played step
        numbers.drawNumberRightAlign(g, width - 12, y1, y2, step);
    }
}
