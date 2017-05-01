package model;

import java.util.ArrayList;

/**
 * Created by theo on 12/04/17.
 */
public class Turtle{

    protected static final double ratioDegRad = 0.0174533; // Rapport radians/degres (pour la conversion)

    private ArrayList<Segment> segments;
    private int x, y;
    private int dir;
    private boolean visible;
    private int color;

    public Turtle() {
        this(0,0);
    }

    public Turtle(int newX, int newY) {
        segments = new ArrayList<Segment>();
        reset();
        x = newX;
        y = newY;

    }

    public void setColor(int n) {
        color = n;
    }

    public int getColor() {
        return color;
    }

    public void reset() {
        x = 0;
        y = 0;
        dir = -90;
        color = 0;
        visible = true;
        segments.clear();
    }

    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void setDir(int newDir){
        dir = newDir;
    }

    public void forward(int dist, int width, int height) {
        System.out.println(dir);
        int realX = (int) Math.round(x+dist*Math.cos(ratioDegRad*dir));
        int realY = (int) Math.round(y+dist*Math.sin(ratioDegRad*dir));
        int endX = realX;
        int endY = realY;
        int newX = realX;
        int newY = realY;
        if(realX<0){
            endX = 0;
            newX = width;
            endY = (int) Math.round(y+((endX-x)/Math.cos(ratioDegRad*dir))*Math.sin(ratioDegRad*dir));
            newY = endY;
        } else if (realX > width) {
            endX = width;
            newX = 0;
            endY = (int) Math.round(y+((endX-x)/Math.cos(ratioDegRad*dir))*Math.sin(ratioDegRad*dir));
            newY = endY;
        }
        if(realY<0){
            endY = 0;
            newY = height;
            endX = (int) Math.round(x+((endY-y)/Math.sin(ratioDegRad*dir))*Math.cos(ratioDegRad*dir));
            newX = endX;
        } else if (realY > height) {
            endY = height;
            newY = 0;
            endX = (int) Math.round(x+((endY-y)/Math.sin(ratioDegRad*dir))*Math.cos(ratioDegRad*dir));
            newX = endX;
        }

        if (visible ==true) {
            Segment seg = new Segment();

            seg.getPtStart().setX(x);
            seg.getPtStart().setY(y);
            seg.getPtEnd().setX(endX);
            seg.getPtEnd().setY(endY);
            seg.setColor(color);

            segments.add(seg);
        }
        setPosition(newX,newY);
        if(endX != realX || endY != realY){
            dist = (int) Math.round(Math.sqrt(Math.pow(realX - endX,2)+Math.pow(realY - endY,2)));
            forward(dist,width,height);
        }

    }

    public void right(int ang) {
        setDir((dir + ang) % 360);
    }

    public void left(int ang) {
        setDir((dir - ang) % 360);
    }

    public void setVisible() {
        visible = true;
    }

    public void setInvisible() {
        visible = false;
    }

    public void color(int n) {
        color = n % 12;
    }

    public void couleurSuivante() {
        color(color +1);
    }

    /** quelques classiques */

    public void square(int width, int height) {
        for (int i=0;i<4;i++) {
            forward(100,width,height);
            right(90);
        }
    }

    public void poly(int n, int a, int width, int height) {
        for (int j=0;j<a;j++) {
            forward(n,width,height);
            right(360/a);
        }
    }

    public void spiral(int n, int k, int a,int width, int height) {
        for (int i = 0; i < k; i++) {
            color(color +1);
            forward(n,width, height);
            right(360/a);
            n = n+1;
        }
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDir() {
        return dir;
    }

    public boolean isVisible() {
        return visible;
    }

    public static double getRatioDegRad() {
        return ratioDegRad;
    }
}
