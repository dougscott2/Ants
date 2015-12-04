package com.theironyard;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends Application {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int ANT_COUNT = 500;

    ArrayList<Ant> ants;
    long lastTimestamp = 0;


    ArrayList<Ant> createAnts(){
        ArrayList<Ant> ants = new ArrayList<>();
        for (int i = 0; i < ANT_COUNT; i ++){
            Random r = new Random();
            ants.add(new Ant(r.nextInt(WIDTH), r.nextInt(HEIGHT)));
        }
        return ants;
    }

    void drawAnts(GraphicsContext context){
        context.clearRect(0, 0, WIDTH, HEIGHT);
        for (Ant ant : ants){
                context.setFill(ant.antColor);
                context.fillOval(ant.x, ant.y, ant.size, ant.size);
        }
    }

    Ant aggravateAnts(Ant ant) {

        //oldSchool
       /* ArrayList<Ant> closeAnts = new ArrayList<>();

        for (Ant otherAnt : ants) {
            if (Math.abs(ant.x - otherAnt.x) <= 20 && Math.abs(ant.y - otherAnt.y) <= 20) {
                closeAnts.add(ant);
            }
        }
        if (closeAnts.size() > 1) {
           ant.antColor = Color.RED;
        } else {
            ant.antColor = Color.BLACK;
        }
        return ant; */

        //newSchool
        ArrayList<Ant> aggroAnts = ants.stream()
                .filter(anthony -> {
                    return Math.abs(ant.x - anthony.x) <= 20
                            &&
                            Math.abs (ant.y - anthony.y) <= 20;
                })
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
        if (aggroAnts.size() > 1){
            ant.antColor = Color.GREEN;
        } else {
            ant.antColor = Color.RED;
        }
        return ant;
    }
    Ant hulkAnt (Ant ant) {
        ArrayList<Ant> hulkAnts = ants.stream()
                .filter(hulk -> {
                    return Math.abs(ant.x - hulk.x) <= 20
                            &&
                            Math.abs(ant.y - hulk.y) <= 20;
                })
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
        if (hulkAnts.size() > 1) {
            ant.size = 20;
          //  ant.height = 20;
        } else {
            ant.size = 10;
            //ant.height = 10;
        }
    return ant;
    }
    Ant speedAnt (Ant ant) {
        ArrayList<Ant> speedAnts = ants.stream()
                .filter(hulk -> {
                    return Math.abs(ant.x - hulk.x) <= 20
                            &&
                            Math.abs(ant.y - hulk.y) <= 20;
                })
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
        if (speedAnts.size() > 1) {
            ant.speed = 2;
            //  ant.height = 20;
        } else {
            ant.speed = 0.5;
            //ant.height = 10;
        }
        return ant;
    }

    double randomStep (){
        return Math.random() * 2 -1 ;
    }

    Ant moveAnt (Ant ant) {
        ant.x += ant.speed * (randomStep()*2);
        ant.y += ant.speed * randomStep()*2;
        return ant;
    }

    void updateAnts() {
        ants = ants.parallelStream()
                .map(this::moveAnt)
                .map(this::aggravateAnts)
                .map(this::hulkAnt)
                .map(this::speedAnt)

                .collect(Collectors.toCollection(ArrayList::new));
    }

    int fps(long now){
        double diff = now - lastTimestamp;
        double diffSeconds = diff / 1000000000;
        return (int) (1/diffSeconds);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = (Canvas) scene.lookup("#canvas");
        Label fpsLabel = (Label) scene.lookup("#fps");
        GraphicsContext context = canvas.getGraphicsContext2D();

        primaryStage.setTitle("Ants");
        primaryStage.setScene(scene);
        primaryStage.show();

        ants = createAnts();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsLabel.setText(String.valueOf(fps(now)));
                lastTimestamp = now;

                updateAnts();
                drawAnts(context);
            }
        };
        timer.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
