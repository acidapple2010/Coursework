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
    Sprite mInstructions2Sprite;
    Text mYouLooseText;
    Plane mPlane;
    MyOrientationManager mOrientation;
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

        mOrientation = new MyOrientationManager(mContext);

        // plane
        float planeStartXOffset = (MyActivity.CAMERA_WIDTH / 6) - (Plane.PLANE_WIDTH / 4) - 20;
        float planeYOffset = (MyActivity.CAMERA_HEIGHT / 2) - (Plane.PLANE_HEIGHT / 4) + 20;
        mPlane = new Plane(planeStartXOffset, planeYOffset, mContext.getVertexBufferObjectManager(), mScene, mOrientation);

        //score
        mScoreText = new Text(0, 60, mResourceManager.mScoreFont, "        ", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
        mScoreText.setZIndex(3);
        mScene.attachChild(mScoreText);

        // get ready text
        mGetReadyText = new Text(0, 170, mResourceManager.mGetReadyFont, "Get Ready!", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
        mGetReadyText.setZIndex(3);
        mScene.attachChild(mGetReadyText);
        centerText(mGetReadyText);

        // instructions image
        mInstructionsSprite = new Sprite(0, 0, 200, 172, mResourceManager.mInstructionsTexture, mContext.getVertexBufferObjectManager());
        mInstructionsSprite.setZIndex(3);
        mScene.attachChild(mInstructionsSprite);
        centerSprite(mInstructionsSprite);
        mInstructionsSprite.setY(mInstructionsSprite.getY() - 60);

        mInstructions2Sprite = new Sprite(0, 0, 200, 53, mResourceManager.mInstructions2Texture, mContext.getVertexBufferObjectManager());
        mInstructions2Sprite.setZIndex(3);
        mScene.attachChild(mInstructions2Sprite);
        centerSprite(mInstructions2Sprite);
        mInstructions2Sprite.setY(mInstructions2Sprite.getY() + 70);

        // you loose text
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
