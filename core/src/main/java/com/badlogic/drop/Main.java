package com.badlogic.drop;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    Texture backgroundTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music dropMusic;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite bucketSprite;

    Vector2 touchPosition;

    Array<Sprite> dropSprites;

    float dropTimer;

    Rectangle bucketRectangle;
    Rectangle dropRectangle;


    @Override
    public void create() {
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        dropMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1,1);

        touchPosition = new Vector2();

        dropSprites = new Array<>();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        dropMusic.setLooping(true);
        dropMusic.setVolume(.2f); // f means float it is used like a % here the 0.5f
        dropMusic.play();


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera (0, 0) becomes the corner
    }

    @Override
    public void render() { // all of this is done every frame
        input();
        logic();
        draw();

    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            bucketSprite.translateX(speed * delta); // speed moves per frame 30 frames = 3 units 60 frames = 6unites p second. delta fixes it through speed * 1/fps it multiplies with 1/fps so that when you multiply by fps it becomes speed. for example: speed = 4  4 * 1/30 = 0.133 0.133 * 30 = 4          4* 1/60 = 0.67  0.67 * 60 = 4
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            bucketSprite.translateX(-speed * delta);
        }

        if (Gdx.input.isTouched()){
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition); // converts units to our world units
            bucketSprite.setCenterX(touchPosition.x); // change horizontal position of the bucket


        }


    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();

        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth)); // clamp forces the X position of bucketSprite to be (current Position, minimum, maximum)x is the buckets left edge so if I click it can go out of the worldWidth, Therefore we subtract the worldWidth with the bucketWidth.

        float delta = Gdx.graphics.getDeltaTime();

        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);



        for(int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);

            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            if(dropSprite.getY() < -dropHeight) { // -dropHeight means that the whole drop is out of the screen because y is at the bottom of the droplet
                dropSprites.removeIndex(i);
            } else if (bucketRectangle.overlaps(dropRectangle)){
                dropSprites.removeIndex(i);
                dropSound.play();
            }
        }


        dropTimer += delta; // Add the time since the last frame
        if (dropTimer > 1f){
            dropTimer = 0;
            createDroplet();
        }


    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.begin();


        spriteBatch.draw(backgroundTexture, 0, 0 , worldWidth, worldHeight);
        bucketSprite.draw(spriteBatch); // looks at what I wrote in set size and what I gave the object gives those thing to spriteBatch (bucketTexture, 0, 0, 1, 1)

        for(Sprite dropSprite : dropSprites) {  //for each dropSprite in the array dropSprites call it dropSprite and draw it
            dropSprite.draw(spriteBatch);
        }



        spriteBatch.end();

    }

    private void createDroplet() {
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); // f stands for float, therefore a droplet can spawn even at 0.25
        dropSprite.setY(worldHeight); // y of dropSprite is at the bottom therefore the dropSprite will spawn outside the window
        dropSprites.add(dropSprite);


    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
