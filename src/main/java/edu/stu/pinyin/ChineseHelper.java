package edu.stu.pinyin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Convert Chinese characters between simplified Chinese and traditional Chinese. It's mean convert simplified
 * Chinese characters to traditional Chinese characters or in reverse.
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
public final class ChineseHelper {

    private ChineseHelper() {
    }

    private static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";
    // key is traditional Chinese character
    static final Map<String, String> CHINESE_SIMPLIFIED_MAP = PinyinResource.getChineseResource();
    // key is simplified Chinese character
    static final Map<String, String> CHINESE_TRADITIONAL_MAP = getChineseTraditionalMap(CHINESE_SIMPLIFIED_MAP);

    /**
     * Get the CHINESE_TRADITIONAL_MAP by reversing the key and value of CHINESE_SIMPLIFIED_MAP
     *
     * @return CHINESE_TRADITIONAL_MAP
     */
    static Map<String, String> getChineseTraditionalMap(final Map<String, String> simple) {
        final Map<String, String> tradition = new ConcurrentHashMap<>();
        for (Map.Entry<String, String> entry : simple.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            tradition.put(value, key);
        }

        return tradition;
    }

    /**
     * Convert one simplified Chinese character to traditional Chinese character
     *
     * @param c the simplified Chinese character
     * @return the traditional Chinese character
     */
    public static char convertToTraditionalChinese(char c) {
        String traditionalChinese = CHINESE_TRADITIONAL_MAP.get(String.valueOf(c));
        if (null != traditionalChinese) {
            return traditionalChinese.charAt(0);
        }

        return c;
    }

    /**
     * Convert one traditional Chinese character to simplified Chinese character
     *
     * @param c the traditional Chinese character
     * @return the simplified Chinese character
     */
    public static char convertToSimplifiedChinese(char c) {
        String simplifiedChinese = CHINESE_SIMPLIFIED_MAP.get(String.valueOf(c));
        if (null != simplifiedChinese) {
            return simplifiedChinese.charAt(0);
        }

        return c;
    }

    /**
     * Convert the simplified Chinese string to traditional Chinese string
     *
     * @param simplifiedChinese the simplified Chinese string
     * @return the traditional Chinese string
     */
    public static String convertToTraditionalChinese(String simplifiedChinese) {
        StringBuilder traditionalChinese = new StringBuilder();
        for (int i = 0, len = simplifiedChinese.length(); i < len; i++) {
            char c = simplifiedChinese.charAt(i);
            traditionalChinese.append(convertToTraditionalChinese(c));
        }

        return traditionalChinese.toString();
    }

    /**
     * Convert the traditional Chinese string to simplified Chinese string
     *
     * @param traditionalChinese the traditional Chinese string
     * @return the simplified Chinese string
     */
    public static String convertToSimplifiedChinese(String traditionalChinese) {
        StringBuilder simplifiedChinese = new StringBuilder();
        for (int i = 0, len = traditionalChinese.length(); i < len; i++) {
            char c = traditionalChinese.charAt(i);
            simplifiedChinese.append(convertToSimplifiedChinese(c));
        }

        return simplifiedChinese.toString();
    }

    /**
     * Returns {@code true} if the specified character is traditional Chinese.
     *
     * @param c c is to be tested
     * @return {@code true} if the specified character is traditional Chinese
     */
    public static boolean isTraditionalChineseCharacter(char c) {
        return CHINESE_SIMPLIFIED_MAP.containsKey(String.valueOf(c));
    }

    /**
     * Returns {@code true} if the specified character is Chinese.
     *
     * @param c c is to be tested
     * @return {@code true} if the specified character is Chinese.
     */
    public static boolean isChineseCharacter(char c) {
        return '〇' == c || String.valueOf(c).matches(CHINESE_REGEX);
    }

    /**
     * Returns {@code true} if the specified string contains Chinese character.
     *
     * @param str str is to be tested
     * @return {@code true} if the specified string contains Chinese character.
     */
    public static boolean containsChinese(String str) {
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (isChineseCharacter(c)) {
                return true;
            }
        }

        return false;
    }

}
