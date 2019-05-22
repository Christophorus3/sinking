package at.ac.tuwien.ims.sinking.GameEngine.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Base class for an UI element</br>
 * @author Matthias Huerbe
 */
public class UIElement
{
    public int x;
    public int y;
    public int width;
    public int height;
    Paint paint;

    public UIElement()
    {
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    public void setColor(@ColorInt int color)
    {
        paint.setColor(color);
    }


    // Handles input
    public InputHandle setInput(MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                int eventX = (int)event.getX();
                int eventY = (int)event.getY();
                if(isPointInBox(eventX, eventY))
                {
                    onClicked();
                }

                break;
            case MotionEvent.ACTION_UP:
                onReleased();

                break;
        }

        return InputHandle.Unhandled;
    }

    private boolean isPointInBox(int x, int y)
    {
        if(x >= this.x && x <= (this.x + width))
            if(y >= this.y && y <= (this.y + height))
            return true;

        return false;
    }

    public void onClicked()
    {
        //paint.setColor(Color.RED);
    }

    public void onReleased()
    {
        //paint.setColor(Color.WHITE);
    }
}
