package app.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by jinbing on 2015/5/28 0028.
 */
public class ArraysUtils {

    /**
     * 判断数组是否为空
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> T[] union(T[] front, T[] tail) {
        front = Arrays.copyOf(front, front.length + tail.length);
        int start = front.length - tail.length;
        for (int i = 0; i < tail.length; i++) {
            front[start + i] = tail[i];
        }
        return front;
    }

    public static String[] parseArray(String array) {
        return array.split(" ");
    }

    public static String converArray(String[] arrays) {
        String array = "";
        for (int i = 0; i < arrays.length; i++) {
            array += arrays[i];
            if (i < arrays.length - 1) array += " ";
        }
        return array;
    }

    public static <T> boolean container(T[] array, T src) {
        for (T item : array) {
            if (item.equals(src)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean container(Collection<T> array, T src) {
        for (T item : array) {
            if (item.equals(src)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将数组转换为逗号分割的String
     * @param array
     * @return [one, two, three] --> one,two,three
     */
    public static String toStringWithComma(String[] array) {
        StringBuilder sb = new StringBuilder();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                sb.append(array[i]);
                if (i < array.length - 1) sb.append(",");
            }
        }
        return sb.toString();
    }

}
