package app.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yejinbing on 15/9/10.
 */
public class UrlUtils {

    public static Map<String, String> parseArguments(String argument) {
        Map<String, String> argMap = new HashMap<>();
        String[] arguments = argument.split("&");
        for (String c : arguments) {
            String[] v = c.split("=");
            if (v.length == 2) {
                argMap.put(v[0], v[1]);
            }
        }
        return argMap;
    }
}
