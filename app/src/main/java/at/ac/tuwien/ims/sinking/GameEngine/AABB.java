package at.ac.tuwien.ims.sinking.GameEngine;


/**
 * Axis Aligned Bounding Box for simple collision detection
 * @author Matthias Huerbe
 */
public class AABB {
    /**
     * x coordinate of the AABB
     */
    public int x;
    /**
     * y coordinate of the AABB
     */
    public int y;
    /**
     * width coordinate of the AABB
     */
    public int width;
    /**
     * height coordinate of the AABB
     */
    public int height;

    public AABB(int inX, int inY, int inWidth, int inHeight){
        x = inX;
        y = inY;
        width = inWidth;
        height = inHeight;
    }

    /**
     * Checks for collision between two AABB boxen
     */
    static public boolean collide(AABB a, AABB b){

        if( (( a.x + a.width) < b.x )|| (a.x > (b.x + b.width))) return false;
        if( (( a.y + a.height) < b.y )|| (a.y > (b.y + b.height))) return false;

        return true;
    }

}
