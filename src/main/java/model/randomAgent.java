package model;


import controler.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hagoterio on 01/05/17.
 */
public class randomAgent implements Runnable {

    private Model model;
    private Turtle turtle;

    public randomAgent(Model model, Turtle turtle) {
        this.model = model;
        this.turtle = turtle;
    }

    public void run() {
        while (true) {
            try {
                Random rand = new Random();
                int speed = rand.nextInt(100) + 1;
                int dir = rand.nextInt(361);
                turtle.setDir(dir);
                turtle.forward(speed,model.getWidth(),model.getHeight());
                model.notifyView();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}