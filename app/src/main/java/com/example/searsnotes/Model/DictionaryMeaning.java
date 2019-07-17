package com.example.searsnotes.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DictionaryMeaning {

    @SerializedName("word")
    private String word;

    @SerializedName("definitions")
    private List<Definitions> definitions;

    public String getWord() {
        return word;
    }

    public List<Definitions> getDefinitions() {
        return definitions;
    }

    public DictionaryMeaning(List<Definitions> definitions) {
        this.definitions = definitions;
    }

    public DictionaryMeaning() {
    }
}
