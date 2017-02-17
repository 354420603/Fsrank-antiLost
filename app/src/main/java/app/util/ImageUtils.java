package app.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yejinbing on 16/1/4.
 */
public class ImageUtils {

    public static Bitmap clearBackground(Bitmap src) {
        int destColor = Color.TRANSPARENT;
        int srcColor = Color.WHITE;//getBackgroundColor(src);
        List<Point> points = new ArrayList<>();
        points.add(new Point(10, 10));
        return changeColor(src, srcColor, destColor, 2, points);
    }

    public static Bitmap clearBackground(Bitmap src, List<Point> points, int diff) {
        int destColor = Color.TRANSPARENT;
        int srcColor = Color.WHITE;//getBackgroundColor(src);
        return changeColor(src, srcColor, destColor, diff, points);
    }

    public static Bitmap changeColor(Bitmap src, int sourceColor, int destColor, int diff, List<Point> points) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        List<Integer> indexs = new ArrayList<>();
        for (Point point : points) {
            indexs.add(width * point.y + point.x);
        }

        mIndexs = new boolean[pixels.length];

        while (true) {
            indexs = clear(indexs, pixels, width, height, destColor, diff);
            if (indexs.size() == 0)
                break;
        }

        for (int i = 0; i < mIndexs.length; i++) {
            if (mIndexs[i])
                pixels[i] = destColor;
        }

        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    private static void clear(Point point, int[] pixels, int width, int height, int sourceColor, int destColor, int diff) {
        int index = point.y * width + point.x;
        int pixel = pixels[index];

        if (pixel == destColor)
            return;

        int A, R, G, B;
        R = Color.red(sourceColor);
        G = Color.green(sourceColor);
        B = Color.blue(sourceColor);

        int diffR = Math.abs(Color.red(pixel) - R);
        int diffG = Math.abs(Color.green(pixel) - G);
        int diffB = Math.abs(Color.blue(pixel) - B);

        if (diffR < diff && diffG < diff && diffB < diff) {
            pixels[index] = destColor;
            if (point.x < width - 1)
                clear(new Point(point.x + 1, point.y), pixels, width, height, sourceColor, destColor, diff);
            if (point.y < height - 1)
                clear(new Point(point.x, point.y + 1), pixels, width, height, sourceColor, destColor, diff);
            if (point.x > 0)
                clear(new Point(point.x - 1, point.y), pixels, width, height, sourceColor, destColor, diff);
            if (point.y > 0)
                clear(new Point(point.x, point.y - 1), pixels, width, height, sourceColor, destColor, diff);
        }
    }

    private static boolean[] mIndexs;

    private static List<Integer> clear(List<Integer> indexs, int[] pixels, int width, int height, int destColor, int diff) {
        List<Integer> newIndexs = new ArrayList<>();
        for (Integer index : indexs) {
            int pixel = pixels[index];

            if (pixel == destColor)
                continue;

            int y = index / width;
            int x = index % width;

            if (x > 0) {
                int nextIndex = index - 1;
                if (!mIndexs[nextIndex]) {
                    if (isSimilar(pixels[nextIndex], pixel, diff)) {
                        newIndexs.add(nextIndex);
                        mIndexs[nextIndex] = true;
                    }
                }
            }
            if (y > 0) {
                int nextIndex = index - width;
                if (!mIndexs[nextIndex]) {
                    if (isSimilar(pixels[nextIndex], pixel, diff)) {
                        newIndexs.add(nextIndex);
                        mIndexs[nextIndex] = true;
                    }
                }
            }
            if (x < width - 1) {
                int nextIndex = index + 1;
                if (!mIndexs[nextIndex]) {
                    if (isSimilar(pixels[nextIndex], pixel, diff)) {
                        newIndexs.add(nextIndex);
                        mIndexs[nextIndex] = true;
                    }
                }
            }
            if (y < height - 1) {
                int nextIndex = index + width;
                if (!mIndexs[nextIndex]) {
                    if (isSimilar(pixels[nextIndex], pixel, diff)) {
                        newIndexs.add(nextIndex);
                        mIndexs[nextIndex] = true;
                    }
                }
            }

            /*if (x > 0) {
                int nextIndex = y * width + x - 1;
                if (!mIndexs[nextIndex]) {
                    if (x < width - 1) {
                        if (isSimilar(pixels[y * width + x + 1], pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    } else {
                        if (isSimilar(pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    }
                }
            }
            if (y > 0) {
                int nextIndex = (y - 1) * width + x;
                if (!mIndexs[nextIndex]) {
                    if (y < height - 1) {
                        if (isSimilar(pixels[(y + 1) * width + x], pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    } else {
                        if (isSimilar(pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    }
                }
            }
            if (x < width - 1) {
                int nextIndex = y * width + x + 1;
                if (!mIndexs[nextIndex]) {
                    if (x > 0) {
                        if (isSimilar(pixels[y * width + x - 1], pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    } else {
                        if (isSimilar(pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    }
                }
            }
            if (y < height - 1) {
                int nextIndex = (y + 1) * width + x;
                if (!mIndexs[nextIndex]) {
                    if (y > 0) {
                        if (isSimilar(pixels[(y - 1) * width + x], pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    } else {
                        if (isSimilar(pixels[nextIndex], pixel, diff))
                            newIndexs.add(nextIndex);
                    }
                }
            }*/
        }

        return newIndexs;
    }

    private static boolean isSimilar(int src, int dst, int diff) {
        int diffR = Math.abs(Color.red(src) - Color.red(dst));
        int diffG = Math.abs(Color.green(src) - Color.green(dst));
        int diffB = Math.abs(Color.blue(src) - Color.blue(dst));

//        return diffR < diff && diffG < diff && diffB < diff;
        return (diffR + diffG + diffB) / 3 < diff;
    }

    private static boolean isSimilar(int prev, int dst, int src, int diff) {
        float diff0 = colorDiff(prev, src);
        float diff1 = colorDiff(src, dst);
//        System.out.println("diff:" + diff1 + " " + diff0);
        return diff1 <= 1.2 * diff0;
    }

    private static float colorDiff(int c0, int c1) {
        int diffR = Math.abs(Color.red(c0) - Color.red(c1));
        int diffG = Math.abs(Color.green(c0) - Color.green(c1));
        int diffB = Math.abs(Color.blue(c0) - Color.blue(c1));

        return (float) (diffR + diffG + diffB) / 3;
    }

}
