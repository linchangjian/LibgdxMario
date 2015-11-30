package com.lcj.supermario.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lcj.supermario.SuperMario;
import com.lcj.supermario.screen.PlayScreen;

/**
 * Created by aniu on 15/11/30.
 */
public class Turtle extends Enemy {
    public static final int KICK_LEFT_SPEED = -2;
    public static final int KICK_RIGHT_SPEED = 2;

    public enum State{
        WALKING,
        SHELL,
        MOVING,
        DEAD
    }
    public State currentState;
    public State previousState;

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestory;
    private boolean destoryed;

    private float deadRotationDegrees;

    private TextureRegion shell;
    private TextureRegion moving;
    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),0,0,16,24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),16,0,16,24));

        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"),64, 0,16,24);
        moving = new TextureRegion(screen.getAtlas().findRegion("turtle"),80, 0,16,24);
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        deadRotationDegrees = 0;
        setBounds(getX(), getY(), 16 / SuperMario.PPM,24/SuperMario.PPM);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ SuperMario.PPM);

        fdef.filter.maskBits =
                SuperMario.GROUND_BIT |
                        SuperMario.COIN_BIT |
                        SuperMario.BRICK_BIT |
                        SuperMario.ENEMY_BIT |
                        SuperMario.MARIO_BIT|
                        SuperMario.OBJECT_BIT;

        fdef.shape = shape;
        fdef.filter.categoryBits = SuperMario.ENEMY_BIT;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] =  new Vector2(-6 ,8).scl(1 / SuperMario.PPM);
        vertice[1] =  new Vector2(6 ,8).scl(1 / SuperMario.PPM);
        vertice[2] =  new Vector2(-3 ,3).scl(1 / SuperMario.PPM);
        vertice[3] =  new Vector2(3 ,3).scl(1 / SuperMario.PPM);
        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = SuperMario.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hitOnHead(Mario mario) {
        if (currentState != State.SHELL){
            currentState = State.SHELL;
            velocity.x = 0;
        }else{
            kick(mario.getX() <= this.getX()? KICK_RIGHT_SPEED:KICK_LEFT_SPEED);
        }
    }

    public void draw(Batch batch){
        if(!destoryed){
            //super.draw(batch);
        }
    }
    public void kick(int speed){
        velocity.x = speed;
        currentState = State.MOVING;

    }

    public State getCurrentState(){
        return currentState;
    }
    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if(currentState == State.SHELL && stateTime > 5){
            currentState = State.WALKING;
            velocity.x = 1;
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8/SuperMario.PPM);

        if(currentState == State.DEAD){
            deadRotationDegrees += 3;
            rotate(deadRotationDegrees);
            if (stateTime > 5 && !destoryed){
                world.destroyBody(b2body);
                destoryed = true;
            }
        }else{

            b2body.setLinearVelocity(velocity);
        }

    }

    @Override
    public void onEnmeyHit(Enemy enemy) {
        if(enemy instanceof Turtle){
            if(((Turtle)enemy).currentState == State.MOVING && currentState != State.MOVING){
                killed();
            }else if(currentState == State.MOVING && ((Turtle)(enemy)).currentState == State.WALKING){
                return;
            }else{
                reverseVelocity(true,false);
            }
        }else if(currentState == State.MOVING){
            reverseVelocity(true, false);

        }
    }

    public void killed(){
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = SuperMario.NOTHING_BIT;
        for(Fixture fixture : b2body.getFixtureList()){
            fixture.setFilterData(filter);
        }
        b2body.applyLinearImpulse(new Vector2(0, 5f),b2body.getWorldCenter(),true);

    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            case SHELL:
                region = shell;
                break;
            case MOVING:
                region = moving;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        if (velocity.x > 0 && region.isFlipX() == false){
            region.flip(true,false);
        }
        if (velocity.x < 0 && region.isFlipX() == true){
            region.flip(true,false);
        }
        stateTime = currentState == previousState? stateTime+dt : 0;
        previousState = currentState;
        return region;
    }
}
