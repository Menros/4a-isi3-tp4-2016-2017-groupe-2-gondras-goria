package model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hagoterio on 26/04/17.
 */
public class Model  extends Observable {
    private Turtle currentTurtle;
    private CopyOnWriteArrayList<Turtle> turtles;
    private int height,width;
    private int color;
    private int mode;
    private ArrayList<Obstacle> obstacles;

    public Model(int width, int height, int mode) {
        this.turtles = new CopyOnWriteArrayList<>();
        this.obstacles = new ArrayList<>();
        this.color = 0;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.addObstacleRectangle(new Point(10, 50), 60, 40);
        this.addObstacleCircle(new Point(200, 100), 70);
        this.addObstacleRectangle(new Point(500, 10), 80, 70);
        this.addObstacleCircle(new Point(200, 300), 40);
        this.addObstacleRectangle(new Point(600, 300), 70, 40);
        this.addTurtles(50);
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public synchronized void setPosition(double newX, double newY) {
        currentTurtle.setPosition((int)newX,(int)newY);
        notifyView();
    }

    public synchronized void setDir(int newDir){
        currentTurtle.setDir(newDir);
        notifyView();
    }

    public synchronized void addTurtles(int n){
        for(int i = 0 ; i < n ; i++){
            this.addTurtle();
        }
    }

    public synchronized void addTurtle(){
        Turtle turtle = new Turtle( width/2,height/2);
        if(mode == 2){
            new Thread(new RandomAgent(this, turtle)).start();
        } else if(mode == 3) {
            new Thread(new FlockingAgent(this, turtle)).start();
        }
        this.turtles.add(turtle);
        this.currentTurtle = turtle;
        this.currentTurtle.setColor(color);
        notifyView(turtle);
    }

    public synchronized void addObstacleRectangle(Point point, int height, int width){
        obstacles.add(new ObstacleRectangle(point, height, width));
        notifyView(obstacles.get(this.obstacles.size()-1));
    }

    public synchronized void addObstacleCircle(Point point, int diameter){
        obstacles.add(new ObstacleCircle(point, diameter));
        notifyView(obstacles.get(this.obstacles.size()-1));
    }

    public synchronized void forward(double dist) {
        this.forward(dist, this.currentTurtle);
    }

    public synchronized boolean forward(double dist, Turtle turtle) {
        Vector vect = new Vector(dist, turtle.getDir(), new int[]{this.width, this.height});
        Point startPoint = new Point(turtle.getX(), turtle.getY());
        Point endPoint = new Point(vect.getX(startPoint.getX()), vect.getY(startPoint.getY()));
        Boolean isInObstacle = false;

        for (Obstacle o : this.obstacles) {
            isInObstacle = (isInObstacle || o.isInObstacle(endPoint));
        }
        if(!isInObstacle){
            turtle.forward(dist,width,height);
            notifyView();
        }
        return !isInObstacle;
    }

    public synchronized void right(int ang) {
        this.currentTurtle.right(ang);
        notifyView();
    }

    public synchronized void left(int ang) {
        this.currentTurtle.left(ang);
        notifyView();
    }

    public synchronized void pencilDown() {
        this.currentTurtle.setVisible();
        notifyView();
    }

    public synchronized void pencilUp() {
        this.currentTurtle.setInvisible();
        notifyView();
    }

    public synchronized void square() {
        this.currentTurtle.square(width,height);
        notifyView();
    }

    public synchronized void poly(int n, int a) {
        this.currentTurtle.poly(n,a,width,height);
        notifyView();
    }

    public synchronized void spiral(int n, int k, int a) {
        this.currentTurtle.spiral(n,k,a,width,height);
        notifyView();
    }

    public void setColor(int color){
        this.color = color;
        currentTurtle.setColor(color);
    }

    public synchronized void reset(){
        for (Iterator it = turtles.iterator(); it.hasNext();) {
            Turtle t = (Turtle) it.next();
            t.reset();
            t.setPosition((int)width/2,(int)height/2);
        }
        notifyView();
    }

    public Turtle getCurrentTurtle() {
        return currentTurtle;
    }

    public void setCurrentTurtle (Turtle turtle){
        this.currentTurtle=turtle;
    }

    public void setCurrentTurtle(int X, int Y){
        for(Turtle turtle : turtles) {
            if (Math.sqrt(Math.pow(turtle.getX() - X, 2) + Math.pow(turtle.getY() - Y, 2)) < 10) {
                currentTurtle = turtle;
                break;
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public CopyOnWriteArrayList<Turtle> getTurtles() {
        return turtles;
    }

    public List<Turtle> getNeighbors(Turtle turtle, int dist){
        int[] dimension = {this.getWidth(), this.getHeight()};
        ArrayList<Turtle> neighbors = new ArrayList<>();
        Vector vector;
        for(Turtle t : turtles){
            if(t != turtle){
                vector = new Vector((turtle.getX()),
                        turtle.getY(),
                        t.getX(),
                        t.getY(),
                        dimension);
                if(vector.getDist()<dist){
                    neighbors.add(t);
                }
            }
        }
        return neighbors;
    }

    public List<Turtle> getNeighbors(Turtle turtle, int dist, int angle){
        int[] dimension = {this.getWidth(), this.getHeight()};
        ArrayList<Turtle> neighbors = new ArrayList<>();
        Vector vector;
        for(Turtle t : turtles){
            if(t != turtle){
                vector = new Vector((turtle.getX()),
                        turtle.getY(),
                        t.getX(),
                        t.getY(),
                        dimension);
                if(vector.getDist()<dist){
                    if(canSee(turtle.getDir(), vector.getAngle(), angle)) neighbors.add(t);
                }
            }
        }
        return neighbors;
    }

    public boolean canSee(double angleTurtle, double angleVoisin, int angle){
        double angleMax = angleTurtle + angle/2;
        double angleMin = angleTurtle - angle/2;
        double angleFix;

        if(angleMax > 360){
            angleFix = angleMax - 360;
            return (angleVoisin >= angleMin || angleVoisin <= angleFix);
        }
        if(angleMin < 0){
            angleFix = angleMin + 360;
            return (angleVoisin <= angleMax || angleVoisin >= angleFix);
        }
        else
            return (angleVoisin <= angleMax && angleVoisin >= angleMin);
    }

    public void notifyView(Object arg){
        setChanged();
        notifyObservers(arg);
    }

    public void notifyView(){
        this.notifyView(null);
    }
}
