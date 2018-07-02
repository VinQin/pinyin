package edu.stu.pinyin;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPinyinResource {

    private static Logger log = Logger.getLogger(TestPinyinResource.class);

    @Test
    public void testLog() {
        //test the log4j configuration
        log.debug("debug");
        log.error("error");
        System.out.println("Hello Log4J");
    }

    @Test
    public void testPinyinResource() {
        Assertions.assertTrue(PinyinResource.getChineseResource().size() > 0);
        Assertions.assertTrue(PinyinResource.getPinyinResource().size() > 0);
        Assertions.assertTrue(PinyinResource.getMultiPinyinResource().size() > 0);
    }


}
