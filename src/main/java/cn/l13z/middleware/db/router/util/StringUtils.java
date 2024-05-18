package cn.l13z.middleware.db.router.util;

/**
 * ClassName: StringUtils.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 07:56 <br> Description: 字符串工具类 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 字符串工具类 <br>
 */
public class StringUtils {
    public static String middleScoreToCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == '-') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        return result.toString();
    }
}
