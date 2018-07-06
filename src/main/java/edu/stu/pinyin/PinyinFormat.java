package edu.stu.pinyin;

/**
 * The format of pinyin
 * There are three format: WITH_TONE_MARK, WITH_TONE_NUMBER, WITHOUT_TONE <br>
 * WITH_TONE_MARK: pinyin with tone mark. eg, zhēng <br>
 * WITH_TONE_NUMBER: pinyin with tone expressed by number. eg, zheng1 <br>
 * WITHOUT_TONE: pinyin without tone
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
public enum PinyinFormat {
    WITH_TONE_MARK, WITH_TONE_NUMBER, WITHOUT_TONE;
}
