package org.dxworks.dxplatform.plugins.insider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    public static String removeComments(String fileContent) {
        String ret = fileContent;

        Pattern p = Pattern.compile("((\\/\\*([\\S\\s]+?)\\*\\/))");
        Matcher m = p.matcher(ret);

        char[] charArray = ret.toCharArray();
        while (m.find()) {
            int startIndex = m.start();
            int stopIndex = m.end();

            for (int i = startIndex + 2; i < stopIndex - 2; i++) {
                if (!Character.isSpaceChar(charArray[i]) && !Character.isWhitespace(charArray[i]))
                    charArray[i] = '#';
            }
        }

        ret = String.valueOf(charArray);

        p = Pattern.compile("(?:\\/\\/.*)");
        m = p.matcher(ret);

        charArray = ret.toCharArray();
        while (m.find()) {
            int startIndex = m.start();
            int stopIndex = m.end();

            for (int i = startIndex + 2; i < stopIndex; i++) {
                if (!Character.isSpaceChar(charArray[i]) && !Character.isWhitespace(charArray[i]))
                    charArray[i] = '#';
            }
        }

        ret = String.valueOf(charArray);

        return ret;
    }
}
