package com.example.FlappyPlane;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;


public class Plane {

    public static final float BITMAP_WIDTH = 1047f;
    public static final float BITMAP_HEIGHT = 903f;

    public static final float PLANE_WIDTH = 55.8f;
    public static final float PLANE_HEIGHT = 40f;

    protected static final float MAX_DROP_SPEED = 12.0f;
    protected static final float GRAVITY = 0.04f;
    protected static final float FLAP_POWER = 6f;

    protected static final float PLANE_MAX_FLAP_ANGLE = -20;
    protected static final float PLANE_MAX_DROP_ANGLE = 90;
    protected static final float FLAP_ANGLE_DRAG = 4.0f;
    protected static final float PLANE_FLAP_ANGLE_POWER = 15.0f;

    private AnimatedSprite mSprite;

    protected float mAcceleration = GRAVITY;
    protected float mVerticalSpeed;
    protected float mCurrentPlaneAngle = PLANE_MAX_FLAP_ANGLE;


    private static TiledTextureRegion mPlaneTextureRegion;

    // sounds
    private static Sound mJumpSound;

    public static void onCreateResources(SimpleBaseGameActivity activity){

        // plane
        BuildableBitmapTextureAtlas mPlaneBitmapTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), (int) BITMAP_WIDTH, (int) BITMAP_HEIGHT, TextureOptions.NEAREST);
        mPlaneTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mPlaneBitmapTextureAtlas, activity, "planemap.png", 3, 3);
        try {
            mPlaneBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
            mPlaneBitmapTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }

        try {
            mJumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "jump.ogg");
        } catch (final IOException e) {
            Debug.e(e);
        }

    }


    private float mPlaneYOffset, mPlaneXOffset;

    public Plane(float birdXOffset, float birdYOffset, VertexBufferObjectManager mVertexBufferObjectManager, Scene mScene) {

        this.mPlaneXOffset = birdXOffset;
        this.mPlaneYOffset = birdYOffset;

        mSprite = new AnimatedSprite(mPlaneXOffset, mPlaneYOffset, 55.8f, 40, mPlaneTextureRegion, mVertexBufferObjectManager);
        mSprite.animate(25);
        mSprite.setZIndex(2);
        mScene.attachChild(mSprite);

    }

    public void restart(){
        mSprite.animate(25);
        mSprite.setY(mPlaneYOffset);
        mSprite.setX(mPlaneXOffset);
        mCurrentPlaneAngle = 0;
        mSprite.setRotation(mCurrentPlaneAngle);
    }

    public float move(){

        float newY = mSprite.getY() + mVerticalSpeed;
        newY = Math.max(newY, 0);
        newY = Math.min(newY, MyActivity.FLOOR_BOUND);
        mSprite.setY(newY);

        // now calculate the new speed
        mAcceleration += GRAVITY;
        mVerticalSpeed += mAcceleration;
        mVerticalSpeed = Math.min(mVerticalSpeed, MAX_DROP_SPEED);

        if(mVerticalSpeed <= (FLAP_POWER)){
            mCurrentPlaneAngle -= PLANE_FLAP_ANGLE_POWER;
        }else{
            mCurrentPlaneAngle += FLAP_ANGLE_DRAG;
        }

        mCurrentPlaneAngle = Math.max(mCurrentPlaneAngle, PLANE_MAX_FLAP_ANGLE);
        mCurrentPlaneAngle = Math.min(mCurrentPlaneAngle, PLANE_MAX_DROP_ANGLE);

        // now apply bird angle based on current speed
        mSprite.setRotation(mCurrentPlaneAngle);

        return newY;
    }

    public void flap(){
        mVerticalSpeed = (-FLAP_POWER);
        mAcceleration = 0;
        mJumpSound.play();
    }

    private float mHoverStep = 0;

    public void hover(){
        mHoverStep+=0.13f;
        float WRAPAROUND_POINT = (float) (2 * Math.PI);
        if(mHoverStep > WRAPAROUND_POINT) mHoverStep = 0;

        float newY = mPlaneYOffset + ((float) (14 * Math.sin(mHoverStep)));
        mSprite.setY(newY);

    }

    public AnimatedSprite getSprite() {
        return mSprite;
    }

}
