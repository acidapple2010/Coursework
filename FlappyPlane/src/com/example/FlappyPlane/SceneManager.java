package com.example.FlappyPlane;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

public class SceneManager {

    // text objects
    Text mScoreText;
    Text mGetReadyText;
    Sprite mInstructionsSprite;
    Text mCopyText;
    Text mYouLooseText;
    Plane mPlane;
    private SimpleBaseGameActivity mContext;
    private ResourceManager mResourceManager;
    private ParallaxBackground mParallaxBackground;

    public SceneManager(SimpleBaseGameActivity context, ResourceManager resourceManager, ParallaxBackground parallaxBackground) {
        this.mContext = context;
        this.mResourceManager = resourceManager;
        this.mParallaxBackground = parallaxBackground;
    }

    public static void centerSprite(Sprite sprite) {
        sprite.setX((MyActivity.CAMERA_WIDTH / 2) - (sprite.getWidth() / 2));
        sprite.setY((MyActivity.CAMERA_HEIGHT / 2) - (sprite.getHeight() / 2));
    }

    public Scene createScene() {

        Scene mScene = new Scene();
        VertexBufferObjectManager vbo = mContext.getVertexBufferObjectManager();

        Sprite backgroundSprite = new Sprite(0, 0, mResourceManager.mBackgroundTextureRegion, vbo);
        mParallaxBackground.attachParallaxEntity(new ParallaxEntity(1, backgroundSprite));

        mScene.setBackground(mParallaxBackground);
        mScene.setBackgroundEnabled(true);


        // plane
        float planeStartXOffset = (MyActivity.CAMERA_WIDTH / 4) - (Plane.PLANE_WIDTH / 4);
        float planeYOffset = (MyActivity.CAMERA_HEIGHT / 2) - (Plane.PLANE_HEIGHT / 4);
        mPlane = new Plane(planeStartXOffset, planeYOffset, mContext.getVertexBufferObjectManager(), mScene);

        //score
        mScoreText = new Text(0, 60, mResourceManager.mScoreFont, "        ", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
        mScoreText.setZIndex(3);
        mScene.attachChild(mScoreText);

        // get ready text
        mGetReadyText = new Text(0, 220, mResourceManager.mGetReadyFont, "Get Ready!", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
        mGetReadyText.setZIndex(3);
        mScene.attachChild(mGetReadyText);
        centerText(mGetReadyText);

        // instructions image
        mInstructionsSprite = new Sprite(0, 0, 200, 172, mResourceManager.mInstructionsTexture, mContext.getVertexBufferObjectManager());
        mInstructionsSprite.setZIndex(3);
        mScene.attachChild(mInstructionsSprite);
        centerSprite(mInstructionsSprite);
        mInstructionsSprite.setY(mInstructionsSprite.getY() + 20);


        // copy text //"(c) Dean Wild 2014"
        mCopyText = new Text(0, 750, mResourceManager.mCopyFont, "", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
        mCopyText.setZIndex(3);
        mScene.attachChild(mCopyText);
        centerText(mCopyText);


        // you suck text
        mYouLooseText = new Text(0, MyActivity.CAMERA_HEIGHT / 2 - 100, mResourceManager.mYouLooseFont, "You Loose!", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
        mYouLooseText.setZIndex(3);
        centerText(mYouLooseText);

        return mScene;
    }

    public void displayCurrentScore(int score) {
        mScoreText.setText("" + score);
        centerText(mScoreText);
    }

    public void displayBestScore(int score) {
        mScoreText.setText("Best - " + score);
        centerText(mScoreText);
    }

    private void centerText(Text text) {
        text.setX((MyActivity.CAMERA_WIDTH / 2) - (text.getWidth() / 2));
    }
}
