package com.example.lr_15_lamaev;



import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamesFragment extends Fragment {

    public ArrayList<Game> games = new ArrayList<>();
    public GameViewModel gameViewModel;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef = database.getReference("Games");
    ListView GameList;
    GameAdapter adapter;

    public GamesFragment() {
    }

    public static GamesFragment newInstance(String param1, String param2) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GameList = view.findViewById(R.id.gameList);
        gameViewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);

        gameViewModel.getGames().observe(getActivity(), games1 ->
        {
            adapter = new GameAdapter(getContext(), R.layout.list_item, games1);
            GameList.setAdapter(adapter);
        });

        getDataFromDB();

        FloatingActionButton AddBt = view.findViewById(R.id.addBt);
        AddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGame(v);
            }
        });

        GameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Game game = adapter.getGameByPosition(position);
                UpdateGame(view, game);
            }
        });

        GameList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Game game = adapter.getGameByPosition(position);
                DeleteGame(view, game);
                return false;
            }
        });

    }

    public void AddGame(View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.add_fragment);
        final EditText nameAddText = dialog.findViewById(R.id.AddName);
        final EditText publicherAddText = dialog.findViewById(R.id.AddPublicher);

        FloatingActionButton btOk = dialog.findViewById(R.id.btOk);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = gameRef.push().getKey();
                String name = nameAddText.getText().toString();
                String publisher = publicherAddText.getText().toString();

                if (!name.isEmpty() && !publisher.isEmpty()) {
                    Game game = new Game(userId, name, publisher, R.drawable.picture);
                    gameRef.child(userId).setValue(game);
                    nameAddText.setText("");
                    publicherAddText.setText("");
                    Toast.makeText(getContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Сначало, заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton btCan = dialog.findViewById(R.id.btCan);
        btCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void UpdateGame(View v, Game game)
    {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_fragment);

        final EditText nameEdText = dialog.findViewById(R.id.EditName);
        final EditText publicherEdText = dialog.findViewById(R.id.EditPublicher);

        nameEdText.setText(game.getName());
        publicherEdText.setText(game.getPublisher());

        FloatingActionButton btOk = dialog.findViewById(R.id.btOkk);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String newName = nameEdText.getText().toString();
                String newPublisher = publicherEdText.getText().toString();

                if(!newName.isEmpty() && !newPublisher.isEmpty())
                {
                    gameRef.child(game.getId()).child("name").setValue(newName);
                    gameRef.child(game.getId()).child("publisher").setValue(newPublisher);

                    Toast.makeText(getContext(), "Данные обновлены", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "Сначало, заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton btCan = dialog.findViewById(R.id.btCann);
        btCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DeleteGame (View v, Game game)
    {
        gameRef.child(game.getId()).removeValue();
    }

    private void getDataFromDB()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (games.size() > 0) games.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Game game = ds.getValue(Game.class);
                    assert game !=null;
                    games.add(game);
                }
                gameViewModel.setData(games);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };
        gameRef.addValueEventListener(vListener);
    }
}