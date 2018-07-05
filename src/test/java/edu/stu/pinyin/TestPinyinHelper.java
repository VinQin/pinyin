package edu.stu.pinyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestPinyinHelper {
    private final String tmpFilePath = "/tmp/mytmp/test.txt";

    public static void print(String[] r) {
        System.out.print("{");
        for (int i = 0, len = r.length; i < len; i++) {
            System.out.print(r[i]);
            if (i < len - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    private String readFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.equals("")) {
                    continue;
                }
                sb.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Test
    public void testConvertWithToneNumber() {
        String pinyinString = "mā,ma,duì,nǐ,liǎo,rú,zhǐ,zhǎng";
        String[] r = PinyinHelper.convertWithToneNumber(pinyinString);
        Assertions.assertArrayEquals(r, new String[]{"ma1", "ma5", "dui4", "ni3", "liao3", "ru2", "zhi3", "zhang3"});
    }

    @Test
    public void testConvertWithoutTone() {
        String pinyinString = "mā,ma,duì,nǐ,liǎo,rú,zhǐ,zhǎng";
        String[] r = PinyinHelper.convertWithoutTone(pinyinString);
        Assertions.assertArrayEquals(r, new String[]{"ma", "ma", "dui", "ni", "liao", "ru", "zhi", "zhang"});
    }

    @Test
    public void testConvertToPinyinArray() {
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('为'), new String[]{"wèi", "wéi"});
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('为', PinyinFormat.WITH_TONE_MARK),
                new String[]{"wèi", "wéi"});
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('为', PinyinFormat.WITH_TONE_NUMBER),
                new String[]{"wei4", "wei2"});
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('为', PinyinFormat.WITHOUT_TONE),
                new String[]{"wei"});

        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('一'), new String[]{"yī"});
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('一', PinyinFormat.WITH_TONE_MARK),
                new String[]{"yī"});
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('一', PinyinFormat.WITH_TONE_NUMBER),
                new String[]{"yi1"});
        Assertions.assertArrayEquals(PinyinHelper.convertToPinyinArray('一', PinyinFormat.WITHOUT_TONE),
                new String[]{"yi"});
    }

    @Test
    public void testConvertToPinyinString() {
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("你好世界", ","), "nǐ,hǎo,shì,jiè");
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("世界你好", ",", PinyinFormat.WITH_TONE_MARK),
                "shì,jiè,nǐ,hǎo");
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("你好世界", ",", PinyinFormat.WITH_TONE_NUMBER),
                "ni3,hao3,shi4,jie4");
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("你好世界", ",", PinyinFormat.WITHOUT_TONE),
                "ni,hao,shi,jie");

        Assertions.assertEquals(PinyinHelper.convertToPinyinString("绰绰有余", ","), "chuò,chuò,yǒu,yú");
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("绰绰有余", ",", PinyinFormat.WITH_TONE_MARK),
                "chuò,chuò,yǒu,yú");
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("绰绰有余", ",", PinyinFormat.WITH_TONE_NUMBER),
                "chuo4,chuo4,you3,yu2");
        Assertions.assertEquals(PinyinHelper.convertToPinyinString("绰绰有余", ",", PinyinFormat.WITHOUT_TONE),
                "chuo,chuo,you,yu");
    }

    @Test
    public void testConvertToPinyinString1() throws PinyinException {
        String statement = readFromFile(tmpFilePath);
        String res = PinyinHelper.convertToPinyinString(statement, " ", PinyinFormat.WITH_TONE_MARK, false);
        System.out.println(res);
    }

    @Test
    public void testConvertToShortPinyin() {
        String statement = readFromFile(tmpFilePath);
        String res = PinyinHelper.convertToShortPinyin(statement);
        System.out.println(res);
    }

    @Test
    public void testGetShortOfMultiPinyin() throws PinyinException {
        Assertions.assertArrayEquals(PinyinHelper.getShortOfMultiPinyin("强者"), new String[]{"qz", "jz"});
        Assertions.assertArrayEquals(PinyinHelper.getShortOfMultiPinyin("单小强", false), new String[]{"dxq", "dxj", "sxq",
                "sxj", "cxq", "cxj"});
        Assertions.assertArrayEquals(PinyinHelper.getShortOfMultiPinyin("上善若水", true), new String[]{"ssrs"});
    }

    @Test
    public void testGetFullOfMultiPinyin() {
        Assertions.assertArrayEquals(PinyinHelper.getFullOfMultiPinyin("单车"), new String[]{"dānchē", "dānjū",
                "shànchē", "shànjū", "chánchē", "chánjū"});
        try {
            String[] r1 = PinyinHelper.getFullOfMultiPinyin("成都の阳亚妮", true);
            Assertions.fail("Expected an PinyinException to be thrown");
        } catch (PinyinException e) {
            Assertions.assertEquals(e.getMessage(), "Can't convert to pinyin: <の>");
        }
    }

    @Test
    public void testConvertWithToneMark1() throws PinyinException {
        Assertions.assertEquals(PinyinHelper.convertWithToneMark("le5"), "le");
        Assertions.assertEquals(PinyinHelper.convertWithToneMark("lv4"), "lǜ");
        Assertions.assertEquals(PinyinHelper.convertWithToneMark("ai4"), "ài");
        String[] pinyinWithToneNumberArray = new String[]{"ni3", "hao3", "a1", "li3", "yin2", "he2"};
        Assertions.assertEquals(PinyinHelper.convertWithToneMark(
                pinyinWithToneNumberArray, " ", true), "nǐ hǎo ā lǐ yín hé");
    }

    @Test
    public void testConvertWithToneMark2() throws PinyinException {
        String pinyinString = PinyinHelper.convertToPinyinString(readFromFile(tmpFilePath), " ", PinyinFormat
                .WITH_TONE_NUMBER);
        System.out.println(pinyinString);
        String[] pinyinWithToneNumberArray = pinyinString.split(" ");
        System.out.println(PinyinHelper.convertWithToneMark(pinyinWithToneNumberArray, "|", false));

    }

}
