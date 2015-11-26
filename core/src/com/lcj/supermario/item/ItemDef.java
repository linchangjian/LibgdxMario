package com.lcj.supermario.item;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by aniu on 15/11/26.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type){
        this.position = position;
        this.type = type;
    }
}
