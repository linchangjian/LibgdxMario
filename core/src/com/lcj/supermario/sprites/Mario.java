package com.lcj.supermario.sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.screen.PlayScreen;

/**
 * Created by aniu on 15/11/23.
 */
public class Mario extends Sprite {




    public enum State{
        FALLING,
        JUMPING,
        STANDING,
        RUNNING,
        GROWING

    }
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    public TextureRegion marioStand;

    private Animation marioRun;
    private Animation marioJump;

    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private float stateTimer;
    private Music music;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;

    public Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0 ;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1 ; i < 4 ; i++){
            frames.add(new TextureRegion(getTexture(),i*16,10,16,16));
        }
        marioRun = new Animation(0.1f,frames);
        frames.clear();
        for (int i = 4 ; i < 6 ; i++){
            frames.add(new TextureRegion(getTexture(),i*16,10,16,16));
        }
        marioJump = new Animation(0.1f,frames);


        marioStand = new TextureRegion(getTexture(),0,10,16,16);
        setBounds(0, 0, 16 / SuperMario.PPM, 16 / SuperMario.PPM);

        //big Mario create animation and texture
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);

        frames.clear();
        for(int i = 1; i < 4 ;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),i*16,0,16,32));
        }
        bigMarioRun = new Animation(0.1f,frames);

        frames.clear();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));

        growMario = new Animation(0.2f,frames);
        frames.clear();

        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ SuperMario.PPM, 32/ SuperMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ SuperMario.PPM);

        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                        SuperMario.COIN_BIT |
                        SuperMario.BRICK_BIT |
                        SuperMario.OBJECT_BIT |
                        SuperMario.ENEMY_BIT |
                        SuperMario.ENEMY_HEAD_BIT|
                        SuperMario.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ SuperMario.PPM, 8/ SuperMario.PPM),new Vector2(2/ SuperMario.PPM, 8/ SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        music = SuperMario.manager.get("audio/music/mario_music.ogg",Music.class);
        music.setLooping(true);
        music.play();
    }

    public void update(float dt){
        if(marioIsBig){
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2- 6/SuperMario.PPM);

        }else{
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        }
        setRegion(getFrame(dt));
        //setRegion(new TextureRegion(getTexture(), 0, 0, 1, 1));
        if(timeToDefineBigMario){
            defineBigMario();
        }
        if(timeToRedefineMario){
            redefineMario();
        }
    }

    private void redefineMario() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);


        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ SuperMario.PPM);

        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.ENEMY_HEAD_BIT|
                SuperMario.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ SuperMario.PPM, 8/ SuperMario.PPM),new Vector2(2/ SuperMario.PPM, 8/ SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        timeToRedefineMario = false;

    }

    private void defineBigMario() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);


        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10/SuperMario.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ SuperMario.PPM);

        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.GROUND_BIT |
                SuperMario.COIN_BIT |
                SuperMario.BRICK_BIT |
                SuperMario.OBJECT_BIT |
                SuperMario.ENEMY_BIT |
                SuperMario.ENEMY_HEAD_BIT|
                SuperMario.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 /SuperMario.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ SuperMario.PPM, 8/ SuperMario.PPM),new Vector2(2/ SuperMario.PPM, 8/ SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = marioIsBig ?bigMarioJump : marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer,true):marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = marioIsBig?bigMarioStand:marioStand;
                    break;
        }
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight)&& region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState? stateTimer+dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState(){
        if(runGrowAnimation){
            return State.GROWING;
        }

        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }
    public void grow(){
        runGrowAnimation = true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(),getY(),getWidth(),getHeight()*2);

        SuperMario.manager.get("audio/sounds/powerup.wav",Sound.class).play();

    }
    public boolean isBig(){
        return marioIsBig;
    }
    public void hit() {
        if(marioIsBig){
            marioIsBig = false;
            timeToRedefineMario = true;
            setBounds(getX(), getY(),getWidth(),getHeight()/2);
            SuperMario.manager.get("audio/sounds/powerdown.wav",Sound.class).play();

        }else {
            SuperMario.manager.get("audio/sounds/mariodie.wav",Sound.class).play();

        }

    }
}
