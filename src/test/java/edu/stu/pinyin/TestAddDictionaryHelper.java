package edu.stu.pinyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class TestAddDictionaryHelper {
    @Test
    public void testAddChineseDictByFilePath() throws FileNotFoundException, UnsupportedEncodingException {
        AddDictionaryHelper.addChineseDictByFilePath("/tmp/mytmp/pinyin/new_chinese.dict", "UTF-8");
        Assertions.assertEquals(ChineseHelper.convertToSimplifiedChinese("裏"), "里");
        AddDictionaryHelper.addPinyinDictByFilePath("/tmp/mytmp/pinyin/new_pinyin.dict", "UTF-8");
        Assertions.assertArrayEquals(PinyinHelper.getFullOfMultiPinyin("亚妮"), new String[]{"yàní", "yàni", "yǎní",
                "yǎni"});
    }
}
