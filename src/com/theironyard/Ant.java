package com.theironyard;


import javafx.scene.paint.Color;

/**
 * Created by DrScott on 12/2/15.
 */
public class Ant {
    double x;
    double y;
    double size = 10;
    double speed = 1;
    //double height = 10;
    Color antColor = Color.BLACK;
    //boolean isFight = false;


    public Ant(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Ant(){}

}
