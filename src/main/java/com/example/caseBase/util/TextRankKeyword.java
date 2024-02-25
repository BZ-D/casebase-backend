package com.example.caseBase.util;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

/**
 * TextRank关键词提取
 *
 * @author hankcs
 */
public class TextRankKeyword {
    public static final int nKeyword = 6;
    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    static final float d = 0.85f;
    /**
     * 最大迭代次数
     */
    static final int max_iter = 200;
    static final float min_diff = 0.001f;

    public TextRankKeyword() {
        // jdk bug : Exception in thread "main" java.lang.IllegalArgumentException: Comparison method violates its general contract!
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public static int countWord(String content){
        List<Term> termList = HanLP.segment(content);
//        System.out.println(termList);
        List<String> wordList = new ArrayList<>();
        for (Term t : termList) {
            if (shouldInclude(t)) {
                wordList.add(t.word);
            }
        }
        return wordList.size();
    }

    public static Map<String,Double> getKeyword(String title, String content) {
        List<Term> termList = HanLP.segment(title + content);
//        System.out.println(termList);
        List<String> wordList = new ArrayList<>();
        for (Term t : termList) {
            if (shouldInclude(t)) {
                wordList.add(t.word);
            }
        }
//        System.out.println(wordList);
        Map<String, Set<String>> words = new HashMap<>();
        Queue<String> que = new LinkedList<>();
        for (String w : wordList) {
            if (!words.containsKey(w)) {
                words.put(w, new HashSet<>());
            }
            que.offer(w);
            if (que.size() > 5) {
                que.poll();
            }

            for (String w1 : que) {
                for (String w2 : que) {
                    if (w1.equals(w2)) {
                        continue;
                    }

                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
        }
        Map<String, Float> score = new HashMap<>();
        for (int i = 0; i < max_iter; ++i) {
            Map<String, Float> m = new HashMap<>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String other : value) {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
        }
        List<Map.Entry<String, Float>> entryList = new ArrayList<>(score.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return (o1.getValue() - o2.getValue() > 0 ? -1 : 1);
            }
        });
//        System.out.println(entryList);
        Map<String,Double> result = new HashMap<>();
        int num = Math.min(nKeyword, entryList.size());
        for (int i = 0; i < num; ++i) {
            result.put(entryList.get(i).getKey(), Double.valueOf(entryList.get(i).getValue()));
        }
        return result;
    }

    /**
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     *
     * @param term
     * @return 是否应当
     */
    public static boolean shouldInclude(Term term) {
        return CoreStopWordDictionary.shouldInclude(term);
    }

    public static void main(String[] args) {
        String content = "便民宝业务办理过程中所需的资金，均为商户自有资金，商户须有足够的实力、资金从事便民宝相关业务。\n";
        System.out.println(TextRankKeyword.getKeyword("", content));
    }
}
