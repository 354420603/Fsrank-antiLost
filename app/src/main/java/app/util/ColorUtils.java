package app.util;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Created by jinbing on 2015/5/6 0006.
 */
public class ColorUtils {

    public static int rateColor(int firstColor, int secondColor, float rate) {
        int red = (int) (Color.red(secondColor) * rate + Color.red(firstColor) * (1 - rate));
        int green = (int) (Color.green(secondColor) * rate + Color.green(firstColor) * (1 - rate));
        int blue = (int) (Color.blue(secondColor) * rate + Color.blue(firstColor) * (1 - rate));
        int alpha = (int) (Color.alpha(secondColor) * rate + Color.alpha(firstColor) * (1 - rate));
        return Color.argb(alpha, red, green, blue);
    }

    public static Drawable getFilterDrawable(Drawable drawable, int color) {
        if (drawable == null)
            return null;

        float[] matrix  = new float[]{
                0, 0, 0, 0, Color.red(color),
                0, 0, 0, 0, Color.green(color),
                0, 0, 0, 0, Color.blue(color),
                0, 0, 0, 1, 0};
        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        drawable.mutate().setColorFilter(colorFilter);
        return drawable;
    }

}
