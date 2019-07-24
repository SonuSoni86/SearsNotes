package com.example.searsnotes.Dao;

import com.example.searsnotes.model.DictionaryMeaning;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WordApi
{
    @GET("words/{word}/definitions")
    Call<DictionaryMeaning>getMeaning(@Path("word")String word);
}
