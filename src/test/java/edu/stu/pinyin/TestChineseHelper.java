package edu.stu.pinyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestChineseHelper {

    @Test
    public void testConvertToSimplifiedChineseCharacter() {
        Assertions.assertEquals(ChineseHelper.convertToSimplifiedChinese('東'), '东');
        Assertions.assertEquals(ChineseHelper.convertToSimplifiedChinese('義'), '义');
    }

    @Test
    public void testConvertToTraditionalChineseCharacter() {
        Assertions.assertEquals(ChineseHelper.convertToTraditionalChinese('台'), '臺');
        Assertions.assertEquals(ChineseHelper.convertToTraditionalChinese('湾'), '灣');
    }

    @Test
    public void testConvertToSimplifiedChineseString() {
        Assertions.assertEquals(ChineseHelper.convertToSimplifiedChinese("你好呀李銀河"), "你好呀李银河");
        Assertions.assertEquals(ChineseHelper.convertToSimplifiedChinese("臺灣屬於中國，臺灣人民是炎黃子孫"), "台湾属于中国，台湾人民是炎黄子孙");
    }

    @Test
    public void testConvertToTraditionalChineseString() {
        Assertions.assertEquals(ChineseHelper.convertToTraditionalChinese("有句话一直在我的心里，未曾说出"), "有句話一直在我的心裡，未曾說出");
        Assertions.assertEquals(ChineseHelper.convertToTraditionalChinese("我喜欢你唐小姐"), "我喜歡你唐小姐");
    }

    @Test
    public void testIsTraditionalChinese() {
        Assertions.assertTrue(ChineseHelper.isTraditionalChineseCharacter('豐'));
        Assertions.assertTrue(!ChineseHelper.isTraditionalChineseCharacter('子'));
        Assertions.assertTrue(ChineseHelper.isTraditionalChineseCharacter('愷'));
    }

    @Test
    public void testIsChineseCharacter() {
        Assertions.assertTrue(ChineseHelper.isChineseCharacter('中'));
        Assertions.assertTrue(ChineseHelper.isChineseCharacter('〇'));
        Assertions.assertTrue(!ChineseHelper.isChineseCharacter('の'));
        Assertions.assertTrue(!ChineseHelper.isChineseCharacter('A'));
    }

    @Test
    public void testContainsChinese() {
        Assertions.assertTrue(ChineseHelper.containsChinese("你好呀李银河小姐～"));
        Assertions.assertTrue(!ChineseHelper.containsChinese("Nice to see you Miss Li~"));
    }

}
