package com.example.FlappyPlane;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;


public class Ring {
    public static final int RING_LEFT_RIGHT_HEIGHT = 133;
    public static final int RING_LEFT_RIGHT_WIDTH = 20;
    public static final int RING_TOP_HEIGHT = 9;
    public static final int RING_TOP_WIDTH = 25;
    public static final int RING_DOWN_WIDTH = 24;
    public static final int RING_DOWN_HEIGHT = 7;
    public static final int RING_LEFT_OFFSET = (-12);
    public static final int RING_RIGHT_OFFSET = 17;
    public static final int RING_OFFSCREEN_OFFSET = 100;
    private static final float RING_Y_OFFSET = MyActivity.CAMERA_WIDTH + RING_OFFSCREEN_OFFSET; // кольцо создаётся за экраном

    private static TextureRegion mRingTopTexture;
    private static TextureRegion mRingDownTexture;
    private static TextureRegion mRingLeftTexture;
    private static TextureRegion mRingRightTexture;
    boolean counted = false;
    private Scene mScene;
    private Sprite mRingTop;
    private Sprite mRingDown;
    private Sprite mRingLeft;
    private Sprite mRingRight;

    public Ring(int mOpeningHeight, VertexBufferObjectManager mVertexBufferObjectManager, Scene mScene) {
        super();
        this.mScene = mScene;

        //noinspection SuspiciousNameCombination
        mRingTop = new Sprite(RING_Y_OFFSET, mOpeningHeight, RING_TOP_WIDTH, RING_TOP_HEIGHT, mRingTopTexture, mVertexBufferObjectManager);
        mRingTop.setZIndex(2);
        mScene.attachChild(mRingTop);

        //noinspection SuspiciousNameCombination
        mRingDown = new Sprite(RING_Y_OFFSET, mOpeningHeight + RING_TOP_HEIGHT + RING_LEFT_RIGHT_HEIGHT, RING_DOWN_WIDTH, RING_DOWN_HEIGHT, mRingDownTexture, mVertexBufferObjectManager);
        mRingDown.setZIndex(2);
        mScene.attachChild(mRingDown);

        mRingLeft = new Sprite(RING_Y_OFFSET + RING_LEFT_OFFSET, mOpeningHeight + RING_TOP_HEIGHT, RING_LEFT_RIGHT_WIDTH, RING_LEFT_RIGHT_HEIGHT, mRingLeftTexture, mVertexBufferObjectManager);
        mRingLeft.setZIndex(1);
        mScene.attachChild(mRingLeft);

        mRingRight = new Sprite(RING_Y_OFFSET + RING_RIGHT_OFFSET, mOpeningHeight + RING_TOP_HEIGHT, RING_LEFT_RIGHT_WIDTH, RING_LEFT_RIGHT_HEIGHT, mRingRightTexture, mVertexBufferObjectManager);
        mRingRight.setZIndex(3);
        mScene.attachChild(mRingRight);
    }

    public static void onCreateResources(SimpleBaseGameActivity activity) {

        BitmapTextureAtlas upperRingTopTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), RING_TOP_WIDTH, RING_TOP_HEIGHT, TextureOptions.BILINEAR);
        mRingTopTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingTopTextureAtlas, activity, "ringTop.png", 0, 0);
        upperRingTopTextureAtlas.load();

        BitmapTextureAtlas upperRingDownTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), RING_DOWN_WIDTH, RING_DOWN_HEIGHT, TextureOptions.BILINEAR);
        mRingDownTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingDownTextureAtlas, activity, "ringDown.png", 0, 0);
        upperRingDownTextureAtlas.load();

        BitmapTextureAtlas upperRingLeftTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), RING_LEFT_RIGHT_WIDTH, RING_LEFT_RIGHT_HEIGHT, TextureOptions.BILINEAR);
        mRingLeftTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingLeftTextureAtlas, activity, "ringLeft.png", 0, 0);
        upperRingLeftTextureAtlas.load();

        BitmapTextureAtlas upperRingRightTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), RING_LEFT_RIGHT_WIDTH, RING_LEFT_RIGHT_HEIGHT, TextureOptions.BILINEAR);
        mRingRightTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingRightTextureAtlas, activity, "ringRight.png", 0, 0);
        upperRingRightTextureAtlas.load();
    }

    public void move(float offset) {
        mRingTop.setPosition(mRingTop.getX() - offset, mRingTop.getY());
        mRingDown.setPosition(mRingDown.getX() - offset, mRingDown.getY());
        mRingLeft.setPosition(mRingLeft.getX() - offset, mRingLeft.getY());
        mRingRight.setPosition(mRingRight.getX() - offset, mRingRight.getY());
    }

    public boolean isOnScreen() {
        return mRingLeft.getX() >= -RING_OFFSCREEN_OFFSET;
    }

    public boolean isCleared(Sprite plane) {

        if (!counted) {
            if (mRingRight.collidesWith(plane)) {
                counted = true;
            }
        } else {
            if (!mRingRight.collidesWith(plane)) {
                counted = false;
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        mScene.detachChild(mRingTop);
        mScene.detachChild(mRingLeft);
        mScene.detachChild(mRingDown);
        mScene.detachChild(mRingRight);

    }

    public boolean collidesWith(Sprite plane) {

        return mRingTop.collidesWith(plane) || mRingDown.collidesWith(plane);

    }
}
