package edu.stu.pinyin;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

/**
 * Add new dictionary using external .dict file by class path or file path.
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
public class AddDictionaryHelper {
    private AddDictionaryHelper() {
    }

    private static final int FILE_PATH = 1;
    private static final int CLASS_PATH = 2;

    //    /**
//     * Add a new Chinese dictionary from the .dict file by specified file path
//     *
//     * @param filepath The path of new Chinese dictionary file(.dict)
//     * @throws FileNotFoundException If the specified filepath is invalid
//     * @see PinyinResource#newFileReader(String)
//     * @see PinyinResource#getResource(Reader)
//     */
    private synchronized static void addChineseDict(String path, final int PATH) throws
            FileNotFoundException {
        Map<String, String> newSimplifiedDict;
        if (PATH == FILE_PATH) {
            newSimplifiedDict = PinyinResource.getResource(PinyinResource.newFileReader(path));
        } else {
            newSimplifiedDict = PinyinResource.getResource(PinyinResource.newClassPathReader(path));
        }
        ChineseHelper.CHINESE_SIMPLIFIED_MAP.putAll(newSimplifiedDict);
        Map<String, String> newTraditionalDict = ChineseHelper.getChineseTraditionalMap(newSimplifiedDict);
        ChineseHelper.CHINESE_TRADITIONAL_MAP.putAll(newTraditionalDict);
    }

    private synchronized static void addChineseDict(String path, final int PATH, String charsetName) throws
            FileNotFoundException, UnsupportedEncodingException {
        Map<String, String> newSimplifiedDict;
        if (PATH == FILE_PATH) {
            newSimplifiedDict = PinyinResource.getResource(PinyinResource.newFileReader(path, charsetName));
        } else {
            newSimplifiedDict = PinyinResource.getResource(PinyinResource.newClassPathReader(path, charsetName));
        }
        ChineseHelper.CHINESE_SIMPLIFIED_MAP.putAll(newSimplifiedDict);
        Map<String, String> newTraditionalDict = ChineseHelper.getChineseTraditionalMap(newSimplifiedDict);
        ChineseHelper.CHINESE_TRADITIONAL_MAP.putAll(newTraditionalDict);
    }

    public static void addChineseDictByFilePath(String filepath) throws FileNotFoundException {
        addChineseDict(filepath, FILE_PATH);
    }

    public static void addChineseDictByFilePath(String filepath, String charsetName) throws FileNotFoundException,
            UnsupportedEncodingException {
        addChineseDict(filepath, FILE_PATH, charsetName);
    }

    public static void addChineseDictByClassPath(String classpath) throws FileNotFoundException {
        addChineseDict(classpath, CLASS_PATH);
    }

    public static void addChineseDictByClassPath(String classpath, String charsetName) throws FileNotFoundException,
            UnsupportedEncodingException {
        addChineseDict(classpath, CLASS_PATH, charsetName);
    }

    //    /**
//     * Add a new pinyin dictionary from the .dict file by specified file path
//     *
//     * @param filepath the path of new pinyin dictionary file(.dict)
//     * @throws FileNotFoundException if the specified filepath is invalid
//     */
    private synchronized static void addPinyinDict(String path, final int PATH) throws FileNotFoundException {
        Reader reader;
        if (PATH == FILE_PATH) {
            reader = PinyinResource.newFileReader(path);
        } else {
            reader = PinyinResource.newClassPathReader(path);
        }
        PinyinHelper.PINYIN_TABLE.putAll(PinyinResource.getResource(reader));
    }

    private synchronized static void addPinyinDict(String path, final int PATH, String charsetName) throws
            FileNotFoundException, UnsupportedEncodingException {
        Reader reader;
        if (PATH == FILE_PATH) {
            reader = PinyinResource.newFileReader(path, charsetName);
        } else {
            reader = PinyinResource.newClassPathReader(path, charsetName);
        }
        PinyinHelper.PINYIN_TABLE.putAll(PinyinResource.getResource(reader));
    }

    public static void addPinyinDictByFilePath(String filepath) throws FileNotFoundException {
        addPinyinDict(filepath, FILE_PATH);
    }

    public static void addPinyinDictByFilePath(String filepath, String charsetName) throws FileNotFoundException,
            UnsupportedEncodingException {
        addPinyinDict(filepath, FILE_PATH, charsetName);
    }

    public static void addPinyinDictByClassPath(String classpath) throws FileNotFoundException {
        addPinyinDict(classpath, CLASS_PATH);
    }

    public static void addPinyinDictByClassPath(String classpath, String charsetName) throws FileNotFoundException,
            UnsupportedEncodingException {
        addPinyinDict(classpath, CLASS_PATH, charsetName);
    }

    //    /**
//     * Add a new multi-pinyin dictionary from the .dict file by specified file path.
//     * This method will also synchronize adding new data to {@linkplain edu.stu.pinyin.TreeMapTrie TREE_MAP_TRIE}
//     *
//     * @param filepath the path od new multi-pinyin dictionary file(.dict)
//     * @throws FileNotFoundException if the specified filepath is invalid
//     */
    private synchronized static void addMultiPinyinDict(String path, final int PATH) throws FileNotFoundException {
        final Map<String, String> NEW_MULTI_PINYIN_TABLE;
        if (PATH == FILE_PATH) {
            NEW_MULTI_PINYIN_TABLE = PinyinResource.getResource(PinyinResource.newFileReader(path));
        } else {
            NEW_MULTI_PINYIN_TABLE = PinyinResource.getResource(PinyinResource.newClassPathReader(path));
        }
        PinyinHelper.MULTI_PINYIN_TABLE.putAll(NEW_MULTI_PINYIN_TABLE);
        Set<String> set = NEW_MULTI_PINYIN_TABLE.keySet();
        String[] newWords = set.toArray(new String[0]);
        PinyinHelper.TREE_MAP_TRIE.buildChineseWordsTrie(newWords);
    }

    private synchronized static void addMultiPinyinDict(String path, final int PATH, String charsetName) throws
            FileNotFoundException, UnsupportedEncodingException {
        final Map<String, String> NEW_MULTI_PINYIN_TABLE;
        if (PATH == FILE_PATH) {
            NEW_MULTI_PINYIN_TABLE = PinyinResource.getResource(PinyinResource.newFileReader(path, charsetName));
        } else {
            NEW_MULTI_PINYIN_TABLE = PinyinResource.getResource(PinyinResource.newClassPathReader(path, charsetName));
        }
        PinyinHelper.MULTI_PINYIN_TABLE.putAll(NEW_MULTI_PINYIN_TABLE);
        Set<String> set = NEW_MULTI_PINYIN_TABLE.keySet();
        String[] newWords = set.toArray(new String[0]);
        PinyinHelper.TREE_MAP_TRIE.buildChineseWordsTrie(newWords);
    }

    public static void addMultiPinyinDictByFilePath(String filepath) throws FileNotFoundException {
        addMultiPinyinDict(filepath, FILE_PATH);
    }

    public static void addMultiPinyinDictByFilePath(String filepath, String charsetName) throws
            FileNotFoundException, UnsupportedEncodingException {
        addMultiPinyinDict(filepath, FILE_PATH, charsetName);
    }

    public static void addMultiPinyinDictByClassPath(String classpath) throws FileNotFoundException {
        addMultiPinyinDict(classpath, CLASS_PATH);
    }

    public static void addMultiPinyinDictByClassPath(String classpath, String charsetName) throws
            FileNotFoundException, UnsupportedEncodingException {
        addMultiPinyinDict(classpath, CLASS_PATH, charsetName);
    }

}
