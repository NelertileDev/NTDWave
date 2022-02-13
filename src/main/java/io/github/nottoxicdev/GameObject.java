package io.github.nottoxicdev;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

    protected int x, y;
    protected ID id;
    protected GroupID gid;
    protected int velX, velY;

    public GameObject(int x, int y, ID id, GroupID gid) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.gid = gid;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract Rectangle getBounds();

    // set & get pos
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // set & get id
    public void setID(ID id) {
        this.id = id;
    }

    public ID getID() {
        return id;
    }

    // set & get gid
    public void setGID(GroupID gid) {
        this.gid = gid;
    }

    public GroupID getGID() {
        return gid;
    }

    // set & get velocity
    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }
}
