package edu.stu.pinyin;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Load the resource file(.dict).
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
final class PinyinResource {
    private PinyinResource() {

    }

    /**
     * Load the resource file from class path to java.io.Reader
     * <p><b>Note: </b>This method use UTF-8 charset as default. Make sure your .dict file is encoded by UTF-8 before
     * call this method.</p>
     *
     * @param classpath The class path of .dict file
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
     * Load the resource file from class path to java.io.Reader using a specified charset.
     *
     * @param classpath   The class path of .dict file
     * @param charsetName The name of a supported charset {@link java.nio.charset.Charset charset}
     * @return java.io.Reader
     * @throws UnsupportedEncodingException If the named charset is not supported
     */
    static Reader newClassPathReader(String classpath, String charsetName) throws UnsupportedEncodingException {
        InputStream is = PinyinResource.class.getResourceAsStream(classpath);
        return new InputStreamReader(is, charsetName);
    }

    /**
     * Load the resource file from file path to java.io.Reader. This method creates a Reader that uses the platform's
     * default charset.
     *
     * @param filepath The file path of .dict file
     * @return java.io.Reader
     * @throws FileNotFoundException If the specified filepath is invalid
     */
    static Reader newFileReader(String filepath) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(filepath));
    }

    /**
     * Load the resource file from file path to java.io.Reader using a specified charset.
     *
     * @param filepath    The file path of .dict file
     * @param charsetName The name of a supported charset {@link java.nio.charset.Charset charset}
     * @return java.io.Reader
     * @throws FileNotFoundException        If the specified filepath is invalid
     * @throws UnsupportedEncodingException If the named charset is not supported
     */
    static Reader newFileReader(String filepath, String charsetName) throws FileNotFoundException,
            UnsupportedEncodingException {
        return new InputStreamReader(new FileInputStream(filepath), charsetName);
    }


    /**
     * Return the instance of map from the specified resource stream.
     *
     * @param reader The resource stream
     * @return The instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getResource(Reader reader) {
        Map<String, String> map = new ConcurrentHashMap<>();
        try {
            BufferedReader br = new BufferedReader(reader);
            String line;
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
     * Convert the pinyin.dict file to map
     *
     * @return The instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getPinyinResource() {
        return getResource(newClassPathReader("/data/pinyin.dict"));
    }

    /**
     * Convert the multi_pinyin.dict file to map
     *
     * @return The instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getMultiPinyinResource() {
        return getResource(newClassPathReader("/data/multi_pinyin.dict"));
    }

    /**
     * Convert the chinese.dict file to map
     *
     * @return The instance of map in which the key is the left string of character '=' and the value is the right
     * string of character '='.
     */
    static Map<String, String> getChineseResource() {
        return getResource(newClassPathReader("/data/chinese.dict"));
    }

}
