package org.automata.elements;

import org.automata.storage.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.signum;

public abstract class ElementBase {
    protected final float[] color=new float[4];
    protected ElementType type;
    protected ElementCategory category;
    protected static HashMap<ElementType, float[][]> colorMap=new HashMap<ElementType, float[][]>();

    ElementBase(){
        setColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.type=ElementType.NONE;
        this.category=ElementCategory.NONE;
    }

    protected ElementBase(float r, float g, float b, float a, ElementType type, ElementCategory category){
        setColor(r, g, b, a);
        this.type=type;
        this.category=category;


    }

    protected ElementBase(float[] rgba, ElementType type, ElementCategory category){
        setColor(rgba);
        this.type=type;
        this.category=category;
    }

    public void update(int x, int y, World world){
        throw new RuntimeException("Element base should not be ever be updated");
    }

    public ElementType getType(){
        return type;
    }

    public float[] getColor() {
        return color.clone();
    }

    public void setColor(float r, float g, float b, float a){
        color[0]=r;
        color[1]=g;
        color[2]=b;
        color[3]=a;
    }

    public void setColor(float[] rgba) {
        this.color[0] = rgba[0];
        this.color[1] = rgba[1];
        this.color[2] = rgba[2];
        this.color[3] = rgba[3];
    }

    public void scaleColor(float scale) {
        this.color[0] = scale*color[0];
        this.color[1] = scale*color[1];
        this.color[2] = scale*color[2];
        this.color[3] = scale*color[3];
    }

    public void setOpacity(float opacity) {
        this.color[3] = opacity;
    }
}
