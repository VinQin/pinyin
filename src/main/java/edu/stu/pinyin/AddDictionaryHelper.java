package edu.stu.pinyin;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

/**
 * Add new dictionary by external .dict file
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
public class AddDictionaryHelper {
    private AddDictionaryHelper() {
    }

    /**
     * Add a new Chinese dictionary from the .dict file by specified file path
     *
     * @param filepath the path of new Chinese dictionary file(.dict)
     * @throws FileNotFoundException if the specified filepath is invalid
     */
    public synchronized static void addChineseDict(String filepath) throws FileNotFoundException {
        Map<String, String> newSimplifiedDict = PinyinResource.getResource(PinyinResource.newFileReader(filepath));
        ChineseHelper.CHINESE_SIMPLIFIED_MAP.putAll(newSimplifiedDict);
        Map<String, String> newTraditionalDict = ChineseHelper.getChineseTraditionalMap(newSimplifiedDict);
        ChineseHelper.CHINESE_TRADITIONAL_MAP.putAll(newTraditionalDict);
    }

    /**
     * Add a new pinyin dictionary from the .dict file by specified file path
     *
     * @param filepath the path of new pinyin dictionary file(.dict)
     * @throws FileNotFoundException if the specified filepath is invalid
     */
    public synchronized static void addPinyinDict(String filepath) throws FileNotFoundException {
        PinyinHelper.PINYIN_TABLE.putAll(PinyinResource.getResource(PinyinResource.newFileReader(filepath)));
    }

    /**
     * Add a new multi-pinyin dictionary from the .dict file by specified file path.
     * This method will also synchronize adding new data to {@linkplain edu.stu.pinyin.TreeMapTrie TREE_MAP_TRIE}
     *
     * @param filepath the path od new multi-pinyin dictionary file(.dict)
     * @throws FileNotFoundException if the specified filepath is invalid
     */
    public synchronized static void addMultiPinyinDict(String filepath) throws FileNotFoundException {
        final Map<String, String> NEW_MULTI_PINYIN_TABLE = PinyinResource.getResource(
                PinyinResource.newFileReader(filepath));
        PinyinHelper.MULTI_PINYIN_TABLE.putAll(NEW_MULTI_PINYIN_TABLE);

        Set<String> set = NEW_MULTI_PINYIN_TABLE.keySet();
        String[] newWords = set.toArray(new String[0]);
        PinyinHelper.TREE_MAP_TRIE.buildChineseWordsTrie(newWords);

    }
}
