package com.moshenskyi.bullsheepandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.moshenskyi.bullsheepandroid.pref.UserPrefManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class AudioActivity extends AppCompatActivity {

    private static final int VOLUME_MAX = 30;
    private ArFragment arFragment;

    private ModelRenderable modelRenderable;

    private ImageView playBtn;
    private ImageView pauseBtn;
    private SeekBar volumeSeekBar;
    private TextView volumeValue;

    private MediaPlayer player;

    private int items = 0;

    private AudioManager audioManager;

    private Subject<MediaPlayer> musicSubject;
    private Observer<MediaPlayer> observer = new Observer<MediaPlayer>() {
        @Override
        public void onSubscribe(Disposable d) {
            player.setLooping(true); // Set looping
            player.setVolume(VOLUME_MAX, VOLUME_MAX);
            player.start();
        }

        @Override
        public void onNext(MediaPlayer mediaPlayer) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            player.pause();
        }
    };
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar
            .OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            volumeValue.setText("Volume: ");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context
                .AUDIO_SERVICE);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id
                .ar_fragment_audio);

        player = MediaPlayer.create(AudioActivity.this, R.raw.tsawyer);

        musicSubject = PublishSubject.create();

        TextView mood = findViewById(R.id.statisticMoodPointTv);
        mood.setText(String.valueOf(UserPrefManager.getInstance().getMoodPoint()));

        initializeGallery();
    }

    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView gramophoneImageView = new ImageView(this);
        gramophoneImageView.setImageResource(R.drawable.gramophone_img);
        gramophoneImageView.setOnClickListener(view -> placeObject(Uri.parse("gramophone.sfb")));

        gallery.addView(gramophoneImageView);
    }

    private void placeObject(Uri pathToModel) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose()) && items++ < 1) {
                    placeObjectByCoords(hit.createAnchor(), pathToModel);
                    break;

                }
            }
        }
    }

    private void placeObjectByCoords(Anchor anchor, Uri pathToModel) {
        ModelRenderable.builder()
                .setSource(this, pathToModel)
                .build()
                .thenAccept(renderable -> placeNodes(anchor, renderable));
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    private void placeNodes(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(renderable);
        node.setLocalRotation(new Quaternion(new Vector3(1f, 0, 0), 0f));

        setMenu(anchorNode, node);
    }

    private void setMenu(AnchorNode anchorNode, TransformableNode transformableNode) {
        ViewRenderable.builder()
                .setView(this, R.layout.music_control)
                .build()
                .thenAccept(viewRenderable -> {
                    playBtn = viewRenderable.getView().findViewById(R.id.play_btn);
                    pauseBtn = viewRenderable.getView().findViewById(R.id.pause_btn);
                    volumeValue = viewRenderable.getView().findViewById(R.id.volume_value);
                    volumeValue.setText("Volume: ");
                    volumeSeekBar = viewRenderable.getView().findViewById(R.id.volume_controls);
                    setupVolumeControl();
                    initClickListeners();

                    TransformableNode musicControlView = new TransformableNode(arFragment
                            .getTransformationSystem());
                    musicControlView.setLocalPosition(new Vector3(0f, transformableNode
                            .getLocalPosition().y + 1f, 0f));
                    musicControlView.setParent(anchorNode);
                    musicControlView.setRenderable(viewRenderable);
                });
    }

    private void setupVolumeControl() {
        volumeSeekBar.getProgressDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        volumeSeekBar.setPadding(10, 0, 10, 0);

        volumeSeekBar.setMax(VOLUME_MAX);
        volumeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        int streamVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setProgress(streamVolume);
    }

    private void subscribe() {
        musicSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void initClickListeners() {
        playBtn.setOnClickListener(view -> {
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            subscribe();
            musicSubject.onNext(player);
            ImageView smile = findViewById(R.id.sm1);
            smile.setVisibility(View.VISIBLE);
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(smile, "translationY", -(point.y - 500));
            translateAnimator.setInterpolator(new LinearInterpolator());
            translateAnimator.setDuration(5000);

            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(smile, "alpha", 0f, 1f);
            alphaAnimator.setInterpolator(new LinearInterpolator());
            alphaAnimator.setDuration(5000);

            alphaAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    smile.setVisibility(View.GONE);

                    TextView points = findViewById(R.id.statisticMoodPointTv);
                    ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(points, "alpha", 1, 0);
                    translateAnimator.setInterpolator(new LinearInterpolator());
                    translateAnimator.setDuration(700);
                    translateAnimator.start();
                    translateAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(points, "alpha", 0, 1);
                            alphaAnimator.setInterpolator(new LinearInterpolator());
                            alphaAnimator.setDuration(700);
                            alphaAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    Integer pointVal = Integer.valueOf(points.getText().toString()) + 5;
                                    points.setText(String.valueOf(pointVal));
                                    UserPrefManager.getInstance().writeMoodPoint(pointVal);
                                }
                            });
                            alphaAnimator.start();
                        }
                    });
                }
            });

            AnimatorSet animationSet = new AnimatorSet();
            animationSet.play(translateAnimator).with(alphaAnimator);
            animationSet.setStartDelay(200);
            animationSet.start();

        });

        pauseBtn.setOnClickListener(view -> {
            playBtn.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.GONE);
            observer.onComplete();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        observer.onComplete();
    }

}
