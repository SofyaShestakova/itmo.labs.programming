package ru.shestakova.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import ru.shestakova.gui.client.BookComponent;

public class ColorAnimator {

  private final long EFFECT_PERIOD = 100L;
  private final long EFFECT_TIME = 5000L;
  private final int EFFECT_ITERATIONS = (int) (EFFECT_TIME / EFFECT_PERIOD);

  private final long EPS_DELAY = EFFECT_PERIOD;

  private Map<BookComponent, Color> sourceColors;
  private Map<BookComponent, Color> targetColors;
  private boolean running = false;
  private boolean workedOut = false;

  private Timer forwardTimer = new Timer();
  private Timer backwardTimer = new Timer();

  public void startAnimation(List<BookComponent> components) {
    if (workedOut) {
      forwardTimer = new Timer();
      backwardTimer = new Timer();
      workedOut = false;
    }

    if (running) {
      throw new IllegalStateException("ColorAnimator is running!");
    }

    running = true;
    sourceColors = new HashMap<>(components.size());
    targetColors = new HashMap<>(components.size());

    components.forEach(component -> {
      sourceColors.put(component, component.getBook().getColor());
      targetColors.put(component, Color.GREEN);
    });

    startEffect(forwardTimer, components,
        sourceColors, targetColors,
        EFFECT_ITERATIONS, EFFECT_PERIOD);

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        startEffect(backwardTimer, components,
            targetColors, sourceColors,
            EFFECT_ITERATIONS, EFFECT_PERIOD);
      }
    }, EFFECT_TIME);

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        workedOut = true;
        running = false;
      }
    }, 2 * EFFECT_TIME + EPS_DELAY);
  }

  private static void startEffect(
      final Timer timer, final List<BookComponent> books,
      final Map<BookComponent, Color> sourceColors, final Map<BookComponent, Color> targetColors,
      final int N, final long period) {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        for (BookComponent component : books) {
          Color source = sourceColors.get(component);
          Color target = targetColors.get(component);
          double[] difference = getIncrease(source, target, N);
          Color currentColor = component.getColor();

          if (Math.abs(target.getRed() - currentColor.getRed()) < Math.abs(difference[0])) {
            component.setColor(target);
            try {
              timer.cancel();
            } catch (IllegalStateException ex) { }
          } else {
            long newRed = Math.round(currentColor.getRed() + difference[0]);
            long newGreen = Math.round(currentColor.getGreen() + difference[1]);
            long newBlue = Math.round(currentColor.getBlue() + difference[2]);

            newRed = (newRed < 0 ? 0 : (newRed > 255 ? 255 : newRed));
            newGreen = (newGreen < 0 ? 0 : (newGreen > 255 ? 255 : newGreen));
            newBlue = (newBlue < 0 ? 0 : (newBlue > 255 ? 255 : newBlue));

            Color newColor = null;
            try {
              newColor = new Color((int) newRed, (int) newGreen, (int) newBlue);
            } catch (IllegalArgumentException ex) {
              System.out.println("Oh hello there");
            }
            component.setColor(newColor);
          }
        }
      }
    }, 0L, period);
  }

  public void stopAnimation() {
    if(!workedOut) {
      try {
        forwardTimer.cancel();
        backwardTimer.cancel();
        workedOut = true;
      } catch (IllegalStateException ex) {

      }
    }

    cancelEffect();
    running = false;
  }

  private void cancelEffect() {
    sourceColors.forEach(BookComponent::setColor);
  }

  public boolean isRunning() {
    return running;
  }

  private static double[] getIncrease(final Color source, final Color target, final int N) {
    double red = ((double) (target.getRed() - source.getRed())) / N;
    double green = ((double) (target.getGreen() - source.getGreen())) / N;
    double blue = ((double) (target.getBlue() - source.getBlue())) / N;

    return new double[]{red, green, blue};
  }
}
