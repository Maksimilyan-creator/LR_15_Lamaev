package com.example.lr_15_lamaev;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class GameViewModel extends ViewModel
{
    private final MutableLiveData<List<Game>> games = new MutableLiveData<List<Game>>();

    public void setData(List<Game> games1)
    {
        games.setValue(games1);
    }

    public LiveData<List<Game>> getGames()
    {
        return games;
    }

}
