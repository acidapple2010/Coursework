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


    private static TextureRegion mRingTopTexture;
    private static TextureRegion mRingDownTexture;
    private static TextureRegion mRingLeftTexture;
    private static TextureRegion mRingRightTexture;

    public static void onCreateResources(SimpleBaseGameActivity activity) {

        BitmapTextureAtlas upperRingTopTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 25, 9, TextureOptions.BILINEAR);
        mRingTopTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingTopTextureAtlas, activity, "ringTop.png", 0, 0);
        upperRingTopTextureAtlas.load();

        BitmapTextureAtlas upperRingDownTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 24, 7, TextureOptions.BILINEAR);
        mRingDownTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingDownTextureAtlas, activity, "ringDown.png", 0,0);
        upperRingDownTextureAtlas.load();

        BitmapTextureAtlas upperRingLeftTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 20, 133, TextureOptions.BILINEAR);
        mRingLeftTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingLeftTextureAtlas, activity, "ringLeft.png", 0, 0);
        upperRingLeftTextureAtlas.load();

        BitmapTextureAtlas upperRingRightTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 20, 133, TextureOptions.BILINEAR);
        mRingRightTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperRingRightTextureAtlas, activity, "ringRight.png", 0, 0);
        upperRingRightTextureAtlas.load();
    }

    private Scene mScene;

    private Sprite mRingTop;
    private Sprite mRingDown;
    private Sprite mRingLeft;
    private Sprite mRingRight;


    private static final float RING_Y_OFFSET = MyActivity.CAMERA_WIDTH + 100; // кольцо создаётся за экраном

    public Ring(int mOpeningHeight, VertexBufferObjectManager mVertexBufferObjectManager, Scene mScene) {
        super();
        this.mScene = mScene;

        mRingTop = new Sprite(RING_Y_OFFSET, mOpeningHeight - 100, 25, 9, mRingTopTexture, mVertexBufferObjectManager);
        mRingTop.setZIndex(2);
        mScene.attachChild(mRingTop);

        mRingDown = new Sprite(RING_Y_OFFSET, mOpeningHeight + 42, 24, 7, mRingDownTexture, mVertexBufferObjectManager);
        mRingDown.setZIndex(2);
        mScene.attachChild(mRingDown);

        mRingLeft = new Sprite(RING_Y_OFFSET - 12, mOpeningHeight - 91, 20, 133, mRingLeftTexture, mVertexBufferObjectManager);
        mRingLeft.setZIndex(1);
        mScene.attachChild(mRingLeft);

        mRingRight = new Sprite(RING_Y_OFFSET + 17, mOpeningHeight - 91, 20, 133, mRingRightTexture, mVertexBufferObjectManager);
        mRingRight.setZIndex(3);
        mScene.attachChild(mRingRight);
    }


    public void move(float offset) {
        mRingTop.setPosition(mRingTop.getX() - offset, mRingTop.getY());
        mRingDown.setPosition(mRingDown.getX() - offset , mRingDown.getY());
        mRingLeft.setPosition(mRingLeft.getX() - offset , mRingLeft.getY());
        mRingRight.setPosition(mRingRight.getX() - offset , mRingRight.getY());
    }

    public boolean isOnScreen() {

        if (mRingLeft.getX() < -100) {
            return false;
        }
        return true;
    }

    boolean counted = false;

    public boolean isCleared(Sprite plane) {

        if (!counted) {
            if(mRingRight.collidesWith(plane)){
                counted = true;
            }
        }else
        {
            if(!mRingRight.collidesWith(plane)) {
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

        if (mRingTop.collidesWith(plane)||mRingDown.collidesWith(plane) ){
            return true;
        }
        return false;

    }
}
