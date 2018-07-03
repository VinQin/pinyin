package edu.stu.pinyin;

import java.util.*;

/**
 * A TreeMap data struct trie.
 *
 * @author vinqin
 * @version create time -- Mon Jul 2 CST 2018
 */
public class TreeMapTrie {

    public static class ChineseWordsTrie {
        public char c; // the character of current node
        public boolean isEnd; // whether the current node is a word or not
        public Map<Character, ChineseWordsTrie> childTrie;

        private ChineseWordsTrie(char c) {
            this.c = c;
            this.isEnd = false;
            childTrie = new TreeMap<>(); // TreeMap<>
        }
    }

    private static TreeMapTrie treeMapTrie;
    private static final ChineseWordsTrie root = new ChineseWordsTrie('0');
    private String[] words; // every element is a Chinese word not a single character, eg: words[0] == 了如指掌

    private TreeMapTrie(String[] words) {
        this.words = words;
        buildChineseWordsTrie();
    }

    public ChineseWordsTrie getRoot() {
        return root;
    }

    public static TreeMapTrie getInstance(String[] words) {
        if (null == treeMapTrie) {
            synchronized (TreeMapTrie.class) {
                if (null == treeMapTrie) {
                    treeMapTrie = new TreeMapTrie(words);
                }
            }
        }
        return treeMapTrie;
    }

    private synchronized void buildChineseWordsTrie() {
        for (String word : words) {
            insertWord(word);
        }
    }

    public synchronized void buildChineseWordsTrie(String[] newWords) {
        for (String word : newWords) {
            insertWord(word);
        }
    }

    private void insertWord(String word) {
        char[] arr = word.toCharArray();
        ChineseWordsTrie currentNode = root;
        for (char c : arr) {
            ChineseWordsTrie child = currentNode.childTrie.get(c);
            if (null == child) {
                child = new ChineseWordsTrie(c);
                currentNode.childTrie.put(c, child);
            }
            currentNode = child;
        }

        currentNode.isEnd = true;
    }

    /**
     * Tokenize the specified string into Chinese word using the exist trie
     *
     * @param str the string need to tokenize
     * @return map key: Chinese word
     * value: the frequency of mapping Chinese word
     */
    public Map<String, Integer> tokenize(String str) {
        char[] arr = str.toCharArray();
        Map<String, Integer> map = new LinkedHashMap<>();

        //记录Trie从root开始匹配的字符
        ChineseWordsTrie currentNode = root;
        StringBuilder sb = new StringBuilder();

        //最后一次匹配到的词语
        String word = "";

        //最后一次匹配时的索引
        int idx = 0;

        for (int i = 0, len = arr.length; i < len; i++) {
            char ch = arr[i];
            ChineseWordsTrie child = currentNode.childTrie.get(ch);
            if (null != child) {
                sb.append(arr[i]);
                currentNode = child;

                if (currentNode.isEnd) {
                    word = sb.toString();
                    idx = i;
                }
            } else if (!word.equals("")) {
                Integer count = map.get(word) == null ? 1 : map.get(word) + 1;
                map.put(word, count);
                sb.setLength(0); //清空sb
                //sb = new StringBuilder();
                currentNode = root;
                word = "";
                i = idx;
            } else if (word.equals("")) {
                if (null != root.childTrie.get(ch)) {
                    //如果根节点的孩子节点包含所要查找的字符
                    i--;
                }
                currentNode = root;
                sb.setLength(0);
            }
        }

        if (!word.equals("")) {
            Integer count = map.get(word) == null ? 1 : map.get(word) + 1;
            map.put(word, count);
        }

        return map;
    }

    public String[] split(String statement) {
        List<String> list = new LinkedList<>(); //按照插入顺序

        for (int i = 0, len = statement.length(); i < len; i++) {
            String str = statement.substring(i);
            String res = getWordFromTrie(str);
            if (!res.equals("")) {
                list.add(res);
                i += res.length() - 1;
            } else {
                list.add(statement.charAt(i) + ""); //这样做的话非常占用空间
            }
        }

        return list.toArray(new String[0]);
    }

    /**
     * 将句子statement按照指定的方式切割
     *
     * @param statement 需要切割的句子
     * @param flag      切割方式
     *                  true: 如果statement中包含词语，则将词语按照一个整体切割。将不是词语的按照单个汉字切割。
     *                  false: 如果statement中包含词语，则将词语按照一个整体切割。将不是词语的按照一句话来切割。
     *                  eg: statement="你好吗？李银河小姐"
     *                  词语： "你好", "小姐"
     *                  flag=true
     *                  ["你好", "吗", "？", "李", "银", "河", "小姐"]
     *                  flag=false
     *                  ["你好", "吗？李银河", "小姐"]
     * @return 切割结果
     */
    public String[] split(String statement, final boolean flag) {
        if (flag) {
            return split(statement);
        }
        List<String> list = new LinkedList<>();
        StringBuilder surplus = new StringBuilder();
        boolean b = false;

        for (int i = 0, len = statement.length(); i < len; i++) {
            String str = statement.substring(i);
            String res = getWordFromTrie(str);
            if (res.equals("")) {
                surplus.append(statement.charAt(i));
                b = true;
            } else {
                if (b) {
                    list.add(surplus.toString());
                    surplus.setLength(0);
                    b = false;
                }
                list.add(res);
                i += res.length() - 1;
            }
        }

        if (b) {
            list.add(surplus.toString());
        }

        return list.toArray(new String[0]);

    }

    public Map<String, Boolean> splitByMultiPinyin(String statement) {
        Map<String, Boolean> map = new LinkedHashMap<>(); //这里千万不能用HashMap只能用LinkedHashMap，因为需要保存元素的插入顺序
        StringBuilder surplus = new StringBuilder();
        boolean b = false;

        for (int i = 0, len = statement.length(); i < len; i++) {
            String str = statement.substring(i);
            String res = getWordFromTrie(str);
            if (res.equals("")) {
                surplus.append(statement.charAt(i));
                b = true;
            } else {
                if (b) {
                    map.put(surplus.toString(), false);
                    surplus.setLength(0);
                    b = false;
                }
                map.put(res, true);
                i += res.length() - 1;
            }
        }

        if (b) {
            map.put(surplus.toString(), false);
        }

        return map;
    }

    private String getWordFromTrie(String str) {
        char[] arr = str.toCharArray();
        ChineseWordsTrie currentNode = root;
        StringBuilder sb = new StringBuilder();

        for (char c : arr) {
            if (currentNode.isEnd) {
                return sb.toString(); //贪心
            }

            ChineseWordsTrie child = currentNode.childTrie.get(c);
            if (null == child) {
                return "";
            }

            sb.append(c);
            currentNode = child;
        }

        if (currentNode.isEnd) {
            return sb.toString();
        }

        return "";

    }

}
