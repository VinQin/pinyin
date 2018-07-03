package edu.stu.pinyin;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Load the resource files(.dict)
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
final class PinyinResource {
    private PinyinResource() {

    }

    /**
     * Convert the resource file from class path to java.io.Reader
     *
     * @param classpath the class path of .dict file
     * @return java.io.Reader, or null if the .dict file does not support the UTF-8 encoding during converting
     */
    static Reader newClassPathReader(String classpath) {
        InputStream is = PinyinResource.class.getResourceAsStream(classpath);
        try {
            return new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Convert the resource file from file path to java.io.Reader
     *
     * @param filepath the file path of .dict file
     * @return java.io.Reader, or null if the .dict file does not support the UTF-8 encoding during converting
     * @throws FileNotFoundException if the specified filepath is invalid
     */
    static Reader newFileReader(String filepath) throws FileNotFoundException {
        try {
            return new InputStreamReader(new FileInputStream(filepath), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Return the instance of map from the specified resource stream.
     *
     * @param reader the resource stream
     * @return the instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getResource(Reader reader) {
        Map<String, String> map = new ConcurrentHashMap<>();
        try {
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("=");
                map.put(tokens[0].trim(), tokens[1].trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    /**
     * Convert the pinyin.dict to map
     *
     * @return the instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getPinyinResource() {
        return getResource(newClassPathReader("/data/pinyin.dict"));
    }

    /**
     * Convert the multi_pinyin.dict to map
     *
     * @return the instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getMultiPinyinResource() {
        return getResource(newClassPathReader("/data/multi_pinyin.dict"));
    }

    /**
     * Convert the chinese.dict to map
     *
     * @return the instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getChineseResource() {
        return getResource(newClassPathReader("/data/chinese.dict"));
    }


}
