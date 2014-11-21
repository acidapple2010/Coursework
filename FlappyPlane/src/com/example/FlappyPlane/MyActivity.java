package com.example.FlappyPlane;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;

public class MyActivity extends SimpleBaseGameActivity {

    public static final float PAUSE_BEFORE_GAME_RESTART = 3.0f;
    public static float CAMERA_WIDTH = 485;
    public static final float CAMERA_HEIGHT = 800;
    private static final float SCROLL_SPEED = 5.5f;
    public static final float FLOOR_BOUND = 601;
    protected static final int RING_SPAWN_INTERVAL = 100;

    // game states
    protected static final int STATE_READY = 1;
    protected static final int STATE_PLAYING = 2;
    protected static final int STATE_DYING = 3;
    protected static final int STATE_DEAD = 4;

    private int GAME_STATE = STATE_READY;

    // objects
    private TimerHandler mTimer;
    private SceneManager mSceneManager;
    private ResourceManager mResourceManager;
    private Scene mScene;

    private ArrayList<Ring> rings = new ArrayList<Ring>();

    // game variables
    private int mScore = 0;
    protected float mCurrentWorldPosition;

    @Override
    public EngineOptions onCreateEngineOptions() {

        CAMERA_WIDTH = com.example.FlappyPlane.ScreenSizeHelper.calculateScreenWidth(this, CAMERA_HEIGHT);

        Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT) {

            private int mRingSpawnCounter;

            @Override
            public void onUpdate(float pSecondsElapsed) {

                switch (GAME_STATE) {

                    case STATE_READY:
                        ready();
                        break;

                    case STATE_PLAYING:
                        play();
                        break;

                    case STATE_DYING:
                        die();
                        break;

                    default:
                        break;
                }

                super.onUpdate(pSecondsElapsed);
            }

            private void ready() {

                mCurrentWorldPosition -= SCROLL_SPEED;
                mSceneManager.mPlane.hover();

                if (!mResourceManager.mMusic.isPlaying()) {
                    mResourceManager.mMusic.play();
                }
            }

            private void die() {
                float newY = mSceneManager.mPlane.move();
                if (newY >= FLOOR_BOUND) dead();
            }

            private void play() {

                mCurrentWorldPosition -= SCROLL_SPEED;
                float newY = mSceneManager.mPlane.move(); // get the plane to update itself
                if (newY >= FLOOR_BOUND) gameOver();

                // now create rings
                mRingSpawnCounter++;

                if (mRingSpawnCounter > RING_SPAWN_INTERVAL) {
                    mRingSpawnCounter = 0;
                    spawnNewRing();
                }

                mScene.sortChildren();

                // now render the rings
                for (int i = 0; i < rings.size(); i++) {
                    Ring ring = rings.get(i);
                    if (ring.isOnScreen()) {
                        ring.move(SCROLL_SPEED);

                        if ((ring.collidesWith(mSceneManager.mPlane.getSprite()))) {
                            gameOver();
                        }

                        if (ring.isCleared(mSceneManager.mPlane.getSprite())) {
                            score();
                        }

                    } else {
                        ring.destroy();
                        rings.remove(ring);
                    }
                }
            }
        };

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);

        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getAudioOptions().setNeedsMusic(true);

        return engineOptions;
    }

    protected void spawnNewRing() {
        final int Min = 100;
        final int Max = 597;
        int spawn = Min + (int) (Math.random() * ((Max - Min) +1));
        Ring newRings = new Ring(spawn, this.getVertexBufferObjectManager(), mScene);
        rings.add(newRings);
    }

    @Override
    protected void onCreateResources() {
        mResourceManager = new ResourceManager(this);
        mResourceManager.createResources();
    }

    @Override
    protected Scene onCreateScene() {

        ParallaxBackground mBackground = new ParallaxBackground(82 / 255f, 190 / 255f, 206 / 255f) {

            float prevX = 0;
            float parallaxValueOffset = 0;

            @Override
            public void onUpdate(float pSecondsElapsed) {

                if (GAME_STATE==STATE_READY || GAME_STATE== STATE_PLAYING){
                    final float cameraCurrentX = mCurrentWorldPosition;

                    if (prevX != cameraCurrentX)
                    {
                        parallaxValueOffset += cameraCurrentX - prevX;
                        this.setParallaxValue(parallaxValueOffset);
                        prevX = cameraCurrentX;
                    }
                }

                super.onUpdate(pSecondsElapsed);
            }
        };

        mSceneManager = new SceneManager(this, mResourceManager, mBackground);
        mScene = mSceneManager.createScene();

        mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {

            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionDown()) {

                    switch (GAME_STATE) {

                        case STATE_READY:
                            startPlaying();
                            break;

                        case STATE_PLAYING:
                            mSceneManager.mPlane.flap();
                            break;

                        case STATE_DEAD:
                            mTimer.setTimerSeconds(0.1f);
                            mTimer.reset();

                            break;

                        default:
                            break;
                    }
                }
                return false;
            }
        });

        updateScore();

        return mScene;
    }

    private void score() {
        mScore++;
        mResourceManager.mScoreSound.play();
        updateScore();
    }

    private void updateScore() {

        if (GAME_STATE == STATE_READY) {
            mSceneManager.displayBestScore(ScoreManager.GetBestScore(this));
        } else {
            mSceneManager.displayCurrentScore(mScore);
        }
    }

    // STATE SWITCHES

    private void startPlaying() {

        GAME_STATE = STATE_PLAYING;

        mResourceManager.mMusic.pause();
        mResourceManager.mMusic.seekTo(0);
        mScene.detachChild(mSceneManager.mGetReadyText);
        mScene.detachChild(mSceneManager.mInstructionsSprite);
        mScene.detachChild(mSceneManager.mCopyText);
        updateScore();
        mSceneManager.mPlane.flap();
    }

    private void gameOver() {

        GAME_STATE = STATE_DYING;

        mResourceManager.mDieSound.play();
        mScene.attachChild(mSceneManager.mYouLooseText);
        mSceneManager.mPlane.getSprite().stopAnimation();
        ScoreManager.SetBestScore(this, mScore);
    }

    private void dead() {

        GAME_STATE = STATE_DEAD;

        mTimer = new TimerHandler(PAUSE_BEFORE_GAME_RESTART, false, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mScene.detachChild(mSceneManager.mYouLooseText);
                restartGame();
                mScene.unregisterUpdateHandler(mTimer);
            }
        });

        mScene.registerUpdateHandler(mTimer);
    }

    private void restartGame() {
        GAME_STATE = STATE_READY;
        mResourceManager.mMusic.resume();
        mSceneManager.mPlane.restart();
        mScore = 0;
        updateScore();

        for (Ring ring : rings) {
            ring.destroy();
        }
        rings.clear();

        mScene.attachChild(mSceneManager.mGetReadyText);
        mScene.attachChild(mSceneManager.mInstructionsSprite);
        mScene.attachChild(mSceneManager.mCopyText);
    }

    @Override
    public final void onPause() {
        super.onPause();
        if (mResourceManager!=null && mResourceManager.mMusic!=null) {
            mResourceManager.mMusic.pause();
        }
    }
}
