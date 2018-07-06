package test;

import edu.stu.pinyin.AddDictionaryHelper;
import edu.stu.pinyin.PinyinHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class TestAddDictionaryHelper {
    @Test
    public void testAddChineseDictByClassPath() throws FileNotFoundException, UnsupportedEncodingException {
        AddDictionaryHelper.addPinyinDictByClassPath("/test.dict", "UTF-8");
        Assertions.assertArrayEquals(PinyinHelper.getFullOfMultiPinyin("亚妮"),
                new String[]{"yàní", "yàni", "yǎní", "yǎni"});
    }
}
