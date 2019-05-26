package com.moshenskyi.bullsheepandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moshenskyi.bullsheepandroid.adapter.AnimalsAdapter;

import java.util.Arrays;
import java.util.List;

import static com.moshenskyi.bullsheepandroid.App.getContext;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText parentMailEt;
    private ImageView usersPhoto;
    private TextView currentScoreTv;
    private TextView levelTv;
    private TextView priceScoreTv;
    private CardView cardAnimalBackground;
    private CardView topCardView;
    private RelativeLayout generalLayout;
    private List<String> colors = Arrays.asList("#e3f2fd",
            "#b2fab4",
            "#ff94c2");
    private List<String> alphaColors = Arrays.asList("#50e3f2fd",
            "#50b2fab4",
            "#50ff94c2");

    private int currentPosition = 0;

    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initUi();
        initAdapter();
        initRecycler();
    }

    private void initUi() {
        recyclerView = findViewById(R.id.animals_recycler);
        parentMailEt = findViewById(R.id.parent_et);
        usersPhoto = findViewById(R.id.user_image);
        currentScoreTv = findViewById(R.id.score_tv);
        priceScoreTv = findViewById(R.id.price_score_tv);
        cardAnimalBackground = findViewById(R.id.profile_background);
        topCardView = findViewById(R.id.name_card_view);
        generalLayout = findViewById(R.id.profile_general_background);
        levelTv = findViewById(R.id.level_tv);
        usersPhoto.setImageResource(R.drawable.profile_image);

        initScrollingListening();
    }

    private void initAdapter() {
        List<Integer> imageIds = Arrays.asList(R.drawable.cartoon_first,
                R.drawable.cartoon_second,
                R.drawable.cartoon_third);
        adapter = new AnimalsAdapter(imageIds);
    }

    private void initRecycler() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void showStar() {
        findViewById(R.id.price_star_image).setVisibility(View.VISIBLE);
    }

    private void hideStar() {
        findViewById(R.id.price_star_image).setVisibility(View.GONE);
    }

    private void initScrollingListening() {
        findViewById(R.id.arrow_left).setOnClickListener(view -> {
            currentPosition = --currentPosition;
            if (currentPosition >= 0) {
                Log.i("SSS", "" + currentPosition);
                recyclerView.getLayoutManager().scrollToPosition(currentPosition);
            } else {
                currentPosition = 2;
                recyclerView.getLayoutManager().scrollToPosition(2);
                Log.i("SSS", "" + currentPosition);
            }

            if (currentPosition == 0) {
                priceScoreTv.setText("Selected");
                hideStar();
            } else {
                showStar();
                priceScoreTv.setText(String.valueOf(currentPosition * 123 + 21));
            }
            cardAnimalBackground.setCardBackgroundColor(Color.parseColor(colors.get(currentPosition)));
            topCardView.setCardBackgroundColor(Color.parseColor(colors.get(currentPosition)));
            generalLayout.setBackgroundColor(Color.parseColor(alphaColors.get(currentPosition)));
            levelTv.setText("Level: " + (currentPosition + 1));
        });
        findViewById(R.id.arrow_right).setOnClickListener(view -> {
            currentPosition = ++currentPosition;
            if (currentPosition <= 2) {
                recyclerView.getLayoutManager().scrollToPosition(currentPosition);
                Log.i("SSS", "" + currentPosition);
            } else {
                currentPosition = 0;
                recyclerView.getLayoutManager().scrollToPosition(currentPosition);
                Log.i("SSS", "" + currentPosition);
            }
            if (currentPosition == 0) {
                priceScoreTv.setText("Selected");
                hideStar();
            } else {
                showStar();
                priceScoreTv.setText(String.valueOf(currentPosition * 123 + 21));
            }
            cardAnimalBackground.setCardBackgroundColor(Color.parseColor(colors.get(currentPosition)));
            topCardView.setCardBackgroundColor(Color.parseColor(colors.get(currentPosition)));
            generalLayout.setBackgroundColor(Color.parseColor(alphaColors.get(currentPosition)));
            levelTv.setText("Level: " + (currentPosition + 1));
        });
    }
}
