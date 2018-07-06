package edu.stu.pinyin;

import java.util.*;

/**
 * @author vinqin
 * @version create time -- Mon Jul 3 CST 2018
 */
public class PinyinHelper {
    static final Map<String, String> PINYIN_TABLE = PinyinResource.getPinyinResource();
    static final Map<String, String> MULTI_PINYIN_TABLE = PinyinResource.getMultiPinyinResource();
    private static final String[] CHINESE_WORDS = new String[MULTI_PINYIN_TABLE.size()];

    static {
        int count = 0;
        for (String word : MULTI_PINYIN_TABLE.keySet()) {
            CHINESE_WORDS[count++] = word;
        }
    }

    static final TreeMapTrie TREE_MAP_TRIE = TreeMapTrie.getInstance(CHINESE_WORDS);
    private static final String PINYIN_SEPARATOR = ","; // 拼音分隔符
    private static final char CHINESE_LING = '〇';
    private static final String ALL_UNMARKED_VOWEL = "aeiouv";
    private static final String ALL_MARKED_VOWEL = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ"; // 所有带声调的拼音字母

    private PinyinHelper() {
    }

    /**
     * Convert pinyin to the format which contains tone with number
     * <p>
     * eg: <br/>
     * "mā,ma,duì,nǐ,liǎo,rú,zhǐ,zhǎng" convert to
     * [ma1, ma5, dui4, ni3, liao3, ru2, zhi3, zhang3]
     * </p>
     *
     * @param pinyinString the pinyin string which contains tone with mark originally
     * @return pinyin contains tone with number
     */
    static String[] convertWithToneNumber(String pinyinString) {
        String[] pinyinArray = pinyinString.split(PINYIN_SEPARATOR);
        for (int i = pinyinArray.length - 1; i > -1; i--) { // 从后往前替换音标
            boolean hasMarkedChar = false; // 拼音pinyinArray[i]是否有音标
            String originalPinyin = pinyinArray[i].replace("ü", "v"); // 将拼音中的ü替换为v

            for (int j = originalPinyin.length() - 1; j > -1; j--) {
                char oldChar = originalPinyin.charAt(j);

                // 搜索带声调的拼音字母，如果存在则替换为对应不带声调的英文字母
                if (oldChar < 'a' || oldChar > 'z') {
                    int indexOfFirstMarked = ALL_MARKED_VOWEL.indexOf(oldChar);
                    int toneNumber = indexOfFirstMarked % 4 + 1; //声调数
                    char newChar = ALL_UNMARKED_VOWEL.charAt(indexOfFirstMarked / 4);
                    pinyinArray[i] = originalPinyin.replace(oldChar, newChar) + toneNumber;
                    hasMarkedChar = true;
                    break;
                }
            }

            if (!hasMarkedChar) {
                // 找不到带声调的拼音字母说明是轻声，用数字5表示
                pinyinArray[i] = originalPinyin + 5;
            }

        }

        return pinyinArray;
    }

    /**
     * Convert pinyin to the format which contains tone without mark
     * <p>
     * eg: <br/>
     * "mā,ma,duì,nǐ,liǎo,rú,zhǐ,zhǎng" convert to
     * [ma, ma, dui, ni, liao, ru, zhi, zhang]
     * </p>
     *
     * @param pinyinString the pinyin string which contains tone with mark originally
     * @return pinyin contains tone without mark
     */
    static String[] convertWithoutTone(String pinyinString) {
        String[] pinyinArray;

        for (int i = ALL_MARKED_VOWEL.length() - 1; i > -1; i--) {
            char oldChar = ALL_MARKED_VOWEL.charAt(i);
            char newChar = ALL_UNMARKED_VOWEL.charAt(i / 4);
            pinyinString = pinyinString.replace(oldChar, newChar);
        }

        pinyinArray = pinyinString.replace("ü", "v").split(PINYIN_SEPARATOR);
        return pinyinArray;
    }

    static String convertWithToneMark(String pinyinWithToneNumberString) {
        int tone = Integer.parseInt(pinyinWithToneNumberString.substring(pinyinWithToneNumberString.length() - 1));
        if (5 == tone) {
            return pinyinWithToneNumberString.substring(0, pinyinWithToneNumberString.length() - 1); // 轻声直接去掉原拼音后面的数字
        }
        char[] vowel = ALL_UNMARKED_VOWEL.toCharArray();
        char[] pinyinWithToneMark = pinyinWithToneNumberString.substring(0, pinyinWithToneNumberString.length() - 1)
                .toCharArray();
        tone -= 5;
        for (int i = 0, len = vowel.length; i < len; i++) {
            String v = String.valueOf(vowel[i]);
            if (pinyinWithToneNumberString.contains(v)) {
                int indexOfV = pinyinWithToneNumberString.indexOf(v);
                int j = (i + 1) * 4 + tone;
                pinyinWithToneMark[indexOfV] = ALL_MARKED_VOWEL.charAt(j);
                break;
            }
        }
        return new String(pinyinWithToneMark);
    }

    public static String convertWithToneMark(String[] pinyinWithToneNumberArray, String separator) {
        StringBuilder sb = new StringBuilder();
        final String REGEX = "^[a-z]+[1-5]$";
        for (int i = 0, len = pinyinWithToneNumberArray.length; i < len; i++) {
            String pinyinWithToneNumberString = pinyinWithToneNumberArray[i];
            if (!pinyinWithToneNumberString.matches(REGEX)) {
                if (i != 0) {
                    sb.append(separator);
                }
                sb.append(pinyinWithToneNumberString);
                continue;
            }
            String pinyinWithToneMark = convertWithToneMark(pinyinWithToneNumberString);
            if (i != 0) {
                sb.append(separator);
            }
            sb.append(pinyinWithToneMark);
        }
        return sb.toString();
    }

    public static String convertWithToneMark(String[] pinyinWithToneNumberArray, String separator, final boolean
            strict) throws
            PinyinException {
        if (!strict) {
            return convertWithToneMark(pinyinWithToneNumberArray, separator);
        }
        StringBuilder sb = new StringBuilder(2 * pinyinWithToneNumberArray.length - 1);
        final String REGEX = "^[a-z]+[1-5]$";
        for (int i = 0, len = pinyinWithToneNumberArray.length; i < len; i++) {
            String pinyinWithToneNumberString = pinyinWithToneNumberArray[i];
            if (!pinyinWithToneNumberString.matches(REGEX)) {
                throw new PinyinException("Can't convert to pinyin with tone mark: <" + pinyinWithToneNumberString +
                        ">");
            }
            String pinyinWithToneMark = convertWithToneMark(pinyinWithToneNumberString);
            if (i != 0) {
                sb.append(separator);
            }
            sb.append(pinyinWithToneMark);
        }
        return sb.toString();
    }

    /**
     * Convert the pinyin which contains tone with mark originally to specified
     * {@linkplain edu.stu.pinyin.PinyinFormat pinyin format}.
     *
     * @param pinyinString the pinyin string which contains tone with mark
     * @param pinyinFormat pinyin format
     * @return specified format of pinyin
     */
    static String[] formatPinyin(String pinyinString, PinyinFormat pinyinFormat) {
        switch (pinyinFormat) {
            case WITH_TONE_MARK:
                return pinyinString.split(PINYIN_SEPARATOR);
            case WITH_TONE_NUMBER:
                return convertWithToneNumber(pinyinString);
            case WITHOUT_TONE:
                return convertWithoutTone(pinyinString);
            default:
                return new String[0];
        }
    }

    /**
     * Convert one single Chinese character to specified {@linkplain edu.stu.pinyin.PinyinFormat pinyin format}
     *
     * @param c            Chinese character
     * @param pinyinFormat pinyin format
     * @return specified format of pinyin
     */
    public static String[] convertToPinyinArray(char c, PinyinFormat pinyinFormat) {
        String pinyinString = PINYIN_TABLE.get(String.valueOf(c));
        if ((null != pinyinString) && (!pinyinString.equals("null"))) {
            return removeDuplication(formatPinyin(pinyinString, pinyinFormat));
        }
        return new String[0];
    }

    /**
     * Convert one single Chinese character to pinyin with tone mark
     *
     * @param c Chinese character
     * @return pinyin with tone mark
     */
    public static String[] convertToPinyinArray(char c) {
        return convertToPinyinArray(c, PinyinFormat.WITH_TONE_MARK);
    }

    /**
     * Convert string containing Chinese characters to specified {@linkplain edu.stu.pinyin.PinyinFormat pinyin format}
     *
     * @param statement    the string which contains Chinese characters
     * @param separator    the sign to separate each pinyin
     * @param pinyinFormat the format of pinyin
     * @return the string of pinyin with specified format
     */
    public static String convertToPinyinString(String statement, String separator, PinyinFormat pinyinFormat) {
        Map<String, Boolean> resultMap = TREE_MAP_TRIE.splitByMultiPinyin(statement);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
            String chineseStr = entry.getKey();
            boolean multiPinyinFlag = entry.getValue();

            if (multiPinyinFlag) {// 多音字
                String multiPinyin = MULTI_PINYIN_TABLE.get(chineseStr);
                String[] multiPinyinArray;
                if ((null != multiPinyin) && (!multiPinyin.equals("null"))) {
                    multiPinyinArray = formatPinyin(multiPinyin, pinyinFormat);
                    if (multiPinyinArray.length == 0) {
                        multiPinyinArray = new String[1];
                        multiPinyinArray[0] = chineseStr;
                    }
                } else {
                    multiPinyinArray = new String[1];
                    multiPinyinArray[0] = chineseStr; // 多音字map中找不到对应拼音，则原样返回汉字字符串
                }
                result.append(combineElementOfStringArrayBySeparatorSign(multiPinyinArray, separator));
            } else {
                String[] pinyinArray = new String[chineseStr.length()];
                for (int i = 0, k = 0, len = chineseStr.length(); i < len; i++, k++) {
                    char c = chineseStr.charAt(i);
                    if (!ChineseHelper.isChineseCharacter(c)) {
                        pinyinArray[k] = String.valueOf(c); // if this character is not a Chinese character
                        continue;
                    }
                    String[] tmp = convertToPinyinArray(c, pinyinFormat);
                    if (0 == tmp.length) {
                        pinyinArray[k] = String.valueOf(c);
                    } else {
                        pinyinArray[k] = tmp[0]; // use the first pinyin if the pinyin is multi-pinyin 如果为多音字则取该字的第一个拼音
                    }
                }
                result.append(combineElementOfStringArrayBySeparatorSign(pinyinArray, separator));
            }
        }

        String res = result.toString();
        int firstIndexOfSeparator = res.indexOf(separator);
        int lengthOfSeparator = firstIndexOfSeparator == -1 ? 1 : separator.length();
        return res.substring(firstIndexOfSeparator + lengthOfSeparator); // subtract the first separator
    }

    /**
     * Convert Chinese string to specified {@linkplain edu.stu.pinyin.PinyinFormat pinyin format}. This method will
     * throw a PinyinException when strict is true and the specified string contains non Chinese character
     *
     * @param statement    the string which contains Chinese characters
     * @param separator    the sign to separate each pinyin
     * @param pinyinFormat the format of pinyin
     * @param strict       whether be strict to convert Chinese character
     * @return the string of pinyin with specified format
     * @throws PinyinException when statement contains character that is not a Chinese character
     * @see PinyinHelper#convertToPinyinString(String, String, PinyinFormat)
     */
    public static String convertToPinyinString(String statement, String separator, PinyinFormat pinyinFormat, final
    boolean strict) throws PinyinException {
        if (!strict) {
            return convertToPinyinString(statement, separator, pinyinFormat);
        }
        Map<String, Boolean> resultMap = TREE_MAP_TRIE.splitByMultiPinyin(statement);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
            String chineseStr = entry.getKey();
            boolean multiPinyinFlag = entry.getValue();

            if (multiPinyinFlag) { // 多音字
                String multiPinyin = MULTI_PINYIN_TABLE.get(chineseStr);
                String[] multiPinyinArray;
                if ((null != multiPinyin) && (!multiPinyin.equals("null"))) {
                    multiPinyinArray = formatPinyin(multiPinyin, pinyinFormat);
                    if (0 == multiPinyinArray.length) {
                        throw new PinyinException("Can't convert to pinyin: <" + chineseStr + ">");
                    }
                } else {
                    throw new PinyinException("Can't convert to pinyin: <" + chineseStr + ">");
                }
                result.append(combineElementOfStringArrayBySeparatorSign(multiPinyinArray, separator));
            } else {
                String[] pinyinArray = new String[chineseStr.length()];
                for (int i = 0, k = 0, len = chineseStr.length(); i < len; i++, k++) {
                    char c = chineseStr.charAt(i);
                    if (!ChineseHelper.isChineseCharacter(c)) {
                        throw new PinyinException("Can't convert to pinyin: <" + c + ">");
                    }
                    String[] tmp = convertToPinyinArray(c, pinyinFormat);
                    if (0 == tmp.length) {
                        throw new PinyinException("Can't convert to pinyin: <" + c + ">");
                    }
                    pinyinArray[k] = tmp[0];
                }
                result.append(combineElementOfStringArrayBySeparatorSign(pinyinArray, separator));
            }

        }
        String res = result.toString();
        int firstIndexOfSeparator = res.indexOf(separator);
        int lengthOfSeparator = firstIndexOfSeparator == -1 ? 1 : separator.length();
        return res.substring(firstIndexOfSeparator + lengthOfSeparator); // subtract the first separator
    }

    /**
     * Convert string containing Chinese characters to pinyin with tone mark
     *
     * @param statement the string which contains Chinese characters
     * @param separator the sign to separate each pinyin
     * @return the string of pinyin with tone mark
     * @see PinyinHelper#convertToPinyinString(String, String, PinyinFormat)
     */
    public static String convertToPinyinString(String statement, String separator) {
        return convertToPinyinString(statement, separator, PinyinFormat.WITH_TONE_MARK);
    }

    /**
     * Convert string containing Chinese characters to pinyin with tone mark. This method will
     * throw a PinyinException when strict is true and the specified string contains non Chinese character
     *
     * @param statement the string which contains Chinese characters
     * @param separator the sign to separate each pinyin
     * @param strict    whether be strict to convert Chinese character
     * @return the string of pinyin with tone mark
     * @throws PinyinException when statement contains character that is not a Chinese character
     * @see PinyinHelper#convertToPinyinString(String, String, PinyinFormat, boolean)
     */
    public static String convertToPinyinString(String statement, String separator, final boolean strict) throws
            PinyinException {
        return convertToPinyinString(statement, separator, PinyinFormat.WITH_TONE_MARK, strict);
    }

    @Deprecated
    private static String combineElementOfStringArray(String[] pinyinArray, String separator) {
        StringBuilder sb = new StringBuilder();
        sb.append(pinyinArray[0]);
        for (int i = 1; i < pinyinArray.length; i++) {
            sb.append(separator);
            sb.append(pinyinArray[i]);
        }
        return sb.toString();
    }

    private static String combineElementOfStringArrayBySeparatorSign(String[] pinyinArray, String separator) {
        StringBuilder sb = new StringBuilder(pinyinArray.length * 2);
        for (String pinyin : pinyinArray) {
            sb.append(separator);
            sb.append(pinyin);
        }
        return sb.toString();
    }

    /**
     * Whether a character has multiple pinyin or not.
     *
     * @param c the character need to validate
     * @return true if c has multiple pinyin, or false
     */
    public static boolean isMultiPinyin(char c) {
        String[] pinyinArray = convertToPinyinArray(c);
        return pinyinArray.length > 1;
    }

    /**
     * Convert string containing Chinese characters to pinyin string where each pinyin only keeps it's first
     * character. eg: yīng --&gt; y
     *
     * @param statement the string which contains Chinese characters
     * @return pinyin string where each pinyin only keeps it's first character
     */
    public static String convertToShortPinyin(String statement) {
        String separator = "#"; // use # as the separator sign
        String pinyins = convertToPinyinString(statement, separator, PinyinFormat.WITHOUT_TONE);
        String[] pinyinArray = pinyins.split(separator);
        char[] shortPinyin = new char[pinyinArray.length];
        int k = 0;
        for (String pinyin : pinyinArray) {
            shortPinyin[k++] = pinyin.charAt(0);
        }
        return String.valueOf(shortPinyin);
    }

    private static <T> String[] getMultiPinyin(String statement, PinyinFormat pinyinFormat, T type) {
        final int SIZE = statement.length();
        List<T>[] pinyinArray = new List[SIZE];

        for (int i = 0; i < SIZE; i++) {
            pinyinArray[i] = new ArrayList<>();
            char c = statement.charAt(i);
            String[] pinyins = convertToPinyinArray(c, pinyinFormat);
            if (pinyins.length == 0) { // c is not a Chinese character
                if (type instanceof Character) {
                    T cc = (T) Character.valueOf(c);
                    pinyinArray[i].add(cc);
                } else if (type instanceof String) {
                    T cc = (T) String.valueOf(c);
                    pinyinArray[i].add(cc);
                } else {
                    //throw new PinyinException("Can't convert to pinyin: " + c);
                }
            } else {
                for (String pinyin : pinyins) {
                    if (type instanceof Character) {
                        Character p = pinyin.charAt(0);
                        pinyinArray[i].add((T) p);
                    }
                    if (type instanceof String) {
                        pinyinArray[i].add((T) pinyin);
                    }

                }
            }
        }

        List<?> resultList = new CartesianProduct().product(pinyinArray);
        int lenOfList = resultList.size();
        String[] result = new String[lenOfList];
        for (int i = 0; i < lenOfList; i++) {
            Object obj = resultList.get(i);
            if (obj instanceof List) {
                result[i] = getMultiPinyin((List<?>) obj);
            } else {
                result[i] = obj.toString();
            }
        }

        return removeDuplication(result);
    }

    private static String getMultiPinyin(List<?> lists) {
        StringBuilder sb = new StringBuilder();
        for (Object list : lists) {
            if (list instanceof List) {
                sb.append(getMultiPinyin((List<?>) list));
            } else {
                sb.append(list.toString());
            }
        }
        return sb.toString();
    }

    private static String[] removeDuplication(String[] origin) {
        Set<String> set = new LinkedHashSet<>();
        set.addAll(Arrays.asList(origin));
        return set.toArray(new String[set.size()]);
        // be careful set.toArray(origin); is wrong. Because of origin.length >= set.size()
    }

    private static <T> String[] getMultiPinyin(String statement, PinyinFormat pinyinFormat, T type, final boolean
            strict) throws PinyinException {
        if (!strict) {
            return getMultiPinyin(statement, pinyinFormat, type);
        }
        final int SIZE = statement.length();
        List<T>[] pinyinArray = new List[SIZE];

        for (int i = 0; i < SIZE; i++) {
            pinyinArray[i] = new ArrayList<>();
            char c = statement.charAt(i);
            String[] pinyins = convertToPinyinArray(c, pinyinFormat);
            if (0 == pinyins.length || !ChineseHelper.isChineseCharacter(c)) { // c is not a Chinese character
                throw new PinyinException("Can't convert to pinyin: <" + c + ">");
            }
            for (String pinyin : pinyins) {
                if (type instanceof Character) {
                    Character p = pinyin.charAt(0);
                    pinyinArray[i].add((T) p);
                }
                if (type instanceof String) {
                    pinyinArray[i].add((T) pinyin);
                }
            }
        }

        List<?> resultList = new CartesianProduct().product(pinyinArray);
        int lenOfList = resultList.size();
        String[] result = new String[lenOfList];
        for (int i = 0; i < lenOfList; i++) {
            Object obj = resultList.get(i);
            if (obj instanceof List) {
                result[i] = getMultiPinyin((List<?>) obj);
            } else {
                result[i] = obj.toString();
            }
        }

        return removeDuplication(result);

    }

    /**
     * Combine all multi-pinyin of Chinese character in statement and make each pinyin abbreviated(keep the first
     * character of each pinyin). <br>
     * eg: 单小强 --&gt; {"dxq", "sxj", "cxq", "dxj", "sxq", "cxj"}
     * <p>
     * <b>Note:</b> It is suggested to limit the length of statement to an acceptable range in case of OOM Exception.
     * </p>
     *
     * @param statement the string which contains Chinese characters
     * @return product of multi-pinyin
     */
    public static String[] getShortOfMultiPinyin(String statement) {
        return getMultiPinyin(statement, PinyinFormat.WITHOUT_TONE, new Character('0'));
    }

    /**
     * Combine all multi-pinyin of Chinese character in statement and make each pinyin abbreviate(keep the first
     * character of each pinyin). This method will throw a PinyinException when strict is true and the specified
     * string contains non Chinese character. <br>
     * eg: 单小强 --&gt; {"dxq", "sxj", "cxq", "dxj", "sxq", "cxj"}
     * <p><b>Note:</b>
     * It is suggested to limit the length of statement to an acceptable range in case of OOM Exception.</p>
     *
     * @param statement the string which contains Chinese characters
     * @param strict    whether be strict to combine only Chinese character that may has multi-pinyin
     * @return product of multi-pinyin
     * @throws PinyinException when statement contains character that is not a Chinese character
     * @see PinyinHelper#getShortOfMultiPinyin(String)
     */
    public static String[] getShortOfMultiPinyin(String statement, final boolean strict) throws PinyinException {
        return getMultiPinyin(statement, PinyinFormat.WITHOUT_TONE, new Character('0'), strict);
    }

    /**
     * Combine all multi-pinyin of Chinese character in statement by specified
     * {@linkplain edu.stu.pinyin.PinyinFormat pinyin format}. <br>
     * eg: 单车 --&gt; {"dānchē", "dānjū", "shànchē", "shànjū","chánchē", "chánjū"} if specified pinyinFormat is
     * PinyinFormat.WITH_TONE_MARK
     * <p>
     * <b>Note:</b> It is suggested to limit the length of statement to an acceptable range in case of OOM Exception.
     * </p>
     *
     * @param statement    the string which contains Chinese characters
     * @param pinyinFormat the multi-pinyin format
     * @return product of multi-pinyin
     */
    public static String[] getFullOfMultiPinyin(String statement, PinyinFormat pinyinFormat) {
        return getMultiPinyin(statement, pinyinFormat, new String());
    }

    /**
     * Combine all multi-pinyin of Chinese character in statement by specified
     * {@linkplain edu.stu.pinyin.PinyinFormat pinyin format}. This method will throw a PinyinException when strict
     * is true and the specified string contains non Chinese character. <br>
     * eg: 单车 --&gt; {"dānchē", "dānjū", "shànchē", "shànjū","chánchē", "chánjū"} if specified pinyinFormat is
     * PinyinFormat.WITH_TONE_MARK
     * <p>
     * <b>Note:</b> It is suggested to limit the length of statement to an acceptable range in case of OOM Exception.
     * </p>
     *
     * @param statement    the string which contains Chinese characters
     * @param pinyinFormat the multi-pinyin format
     * @param strict       whether be strict to combine only Chinese character that may has multi-pinyin
     * @return product of multi-pinyin
     * @throws PinyinException when statement contains character that is not a Chinese character
     * @see PinyinHelper#getFullOfMultiPinyin(String, PinyinFormat)
     */
    public static String[] getFullOfMultiPinyin(String statement, PinyinFormat pinyinFormat, final boolean strict)
            throws PinyinException {
        return getMultiPinyin(statement, pinyinFormat, new String(), strict);
    }

    /**
     * Combine all multi-pinyin of Chinese character in statement by PinyinFormat.WITH_TONE_MARK. <br>
     * eg: 单车 --&gt; {"dānchē", "dānjū", "shànchē", "shànjū","chánchē", "chánjū"}
     * <p>
     * <b>Note:</b> It is suggested to limit the length of statement to an acceptable range in case of OOM Exception.
     * </p>
     *
     * @param statement the string which contains Chinese characters
     * @return product of multi-pinyin
     * @see PinyinHelper#getFullOfMultiPinyin(String, PinyinFormat)
     * @see PinyinHelper#getFullOfMultiPinyin(String, PinyinFormat, boolean)
     */
    public static String[] getFullOfMultiPinyin(String statement) {
        return getFullOfMultiPinyin(statement, PinyinFormat.WITH_TONE_MARK);
    }

    /**
     * Combine all multi-pinyin of Chinese character in statement by PinyinFormat.WITH_TONE_MARK. This method will
     * throw a PinyinException when strict is true and the specified string contains non Chinese character. <br>
     * eg: 单车 --&gt; {"dānchē", "dānjū", "shànchē", "shànjū","chánchē", "chánjū"}
     * <p>
     * <b>Note:</b> It is suggested to limit the length of statement to an acceptable range in case of OOM Exception.
     * </p>
     *
     * @param statement the string which contains Chinese characters
     * @param strict    whether be strict to combine only Chinese character that may has multi-pinyin
     * @return product of multi-pinyin
     * @throws PinyinException when statement contains character that is not a Chinese character
     * @see PinyinHelper#getFullOfMultiPinyin(String, PinyinFormat)
     * @see PinyinHelper#getFullOfMultiPinyin(String, PinyinFormat, boolean)
     */
    public static String[] getFullOfMultiPinyin(String statement, final boolean strict) throws PinyinException {
        return getFullOfMultiPinyin(statement, PinyinFormat.WITH_TONE_MARK, strict);
    }

}
