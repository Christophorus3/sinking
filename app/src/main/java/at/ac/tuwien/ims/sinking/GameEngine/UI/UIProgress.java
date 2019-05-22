package at.ac.tuwien.ims.sinking.GameEngine.UI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

/**
 * A progress bar that shows the remaining time
 *
 * @author Christoph Wottawa
 */
public class UIProgress extends UIElement {

    private float progress;

    private Paint secondaryPaint;

    public UIProgress() {
        super();

        secondaryPaint = new Paint();
        progress = 0.0f;
    }

    /**
     * draw two colored bar using set percentage.
     * @param canvas
     */
    public void draw(Canvas canvas)
    {
        canvas.drawRect(x, y, x + width, y + height, paint);
        canvas.drawRect(x, y, x + (width * progress), y + height, secondaryPaint);
    }


    public void setProgress(float progress) {
        this.progress = progress;
    }

    /**
     * set secondary color, for the progress indication.
     *
     * @param color
     */
    public void setSecondaryColor(@ColorInt int color) {
        secondaryPaint.setColor(color);
    }
}
