package logparser.utils;

public class StringUtils {
    public static int calcOccurences(final String inputString, final String strFind) {
        int count = 0, fromIndex = 0;

        while ((fromIndex = inputString.indexOf(strFind, fromIndex)) != -1 ){
            count++;
            fromIndex++;
        }
        return count;
    }
}
