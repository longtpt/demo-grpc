package io.enotion.demo.mockito;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary {
    Map<String, String> wordMap;
    Map<String, String> wordMap2;

    public MyDictionary() {
        wordMap = new HashMap<String, String>();
    }
    public void add(final String word, final String meaning) {
        wordMap.put(word, meaning);
    }
    public String getMeaning(String word) {
        return wordMap.get(word);
    }
}