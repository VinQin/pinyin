package edu.stu.pinyin;

import java.io.FileNotFoundException;
import java.util.Map;

public class AddDictionaryHelper {
    private AddDictionaryHelper() {
    }

    /**
     * Add a new Chinese dictionary from the .dict file by it's file path
     *
     * @throws FileNotFoundException if the specified filepath is invalid
     */
    public synchronized static void addChineseDict(String filepath) throws FileNotFoundException {
        Map<String, String> newSimplifiedDict = PinyinResource.getResource(PinyinResource.newFileReader(filepath));
        ChineseHelper.CHINESE_SIMPLIFIED_MAP.putAll(newSimplifiedDict);
        Map<String, String> newTraditionalDict = ChineseHelper.getChineseTraditionalMap(newSimplifiedDict);
        ChineseHelper.CHINESE_TRADITIONAL_MAP.putAll(newTraditionalDict);
    }
}
