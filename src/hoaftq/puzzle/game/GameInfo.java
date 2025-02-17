package hoaftq.puzzle.game;

import hoaftq.puzzle.info.Numbers;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class GameInfo {
    /**
     * Time played
     */
    private int elapsedTime;

    /**
     * Step moved
     */
    private int step;

    /**
     * Timer count time played
     */
    private Timer timer;

    /**
     * Number object used to display time played and step
     */
    private Numbers numbers;

    private Consumer<Integer> tickListener;


    public GameInfo() throws IOException {
        numbers = new Numbers("numbers.gif");
        timer = new Timer(1000, e -> {
            elapsedTime++;
            if (tickListener != null) {
                tickListener.accept(elapsedTime);
            }
//                repaint();
        });
    }

    public void setTickListener(Consumer<Integer> tickListener) {
        this.tickListener = tickListener;
    }

    public void increaseStep() {
        step++;
    }

    public void reset() {
        step = 0;
        elapsedTime = 0;
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void paintInformation(Graphics g, int width, int y1, int y2) {
        // Draw time played
        numbers.drawNumber(g, 10, y1, y2, elapsedTime, 4);

        // Draw played step
        numbers.drawNumberRightAlign(g, width - 12, y1, y2, step);
    }


}
