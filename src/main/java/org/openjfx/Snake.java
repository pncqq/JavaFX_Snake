package org.openjfx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

enum Directions {
    LEFT, RIGHT, UP, DOWN
}

public class Snake extends Application {

    /**
     * Rysuje na płótnie.
     */
    GraphicsContext graphicsContext;


    //Plansza
    private static final int width = 600, height = 600;
    private static final int rows = 20, columns = 20;
    private static final int squareSize = width / rows;

    //Wąż
    private ArrayList<Point> snakeFigure = new ArrayList<>();
    private Point snakeHead;
    private Directions actualDirection = Directions.RIGHT;

    //Jedzenie
    private static final String applePath = "64x64.png";
    private static final String badApplePath = "apple2.png";
    private static final String snakeHeadPath = "head.png";
    private Image apple;
    private int appleX;
    private int appleY;
    private boolean isActualAppleGood;

    //Reszta
    private boolean gameIsOver = false;
    private final Random rnd = new Random();
    private static int score = 0;
    private static int applesCounter = 0;
    private Timeline timeline;


    /**
     * Warstwy, mechanika poruszania sie, poczatkowe ustawienia.
     */
    @Override
    public void start(Stage stage) {
        //Plotno
        Canvas canvas = new Canvas(width, height);

        //Kontener
        VBox box = new VBox(10);
        box.getChildren().add(canvas);

        //Zawartosc okna
        Scene scene = new Scene(box);

        //Okno
        stage.setTitle("Wonrz");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        graphicsContext = canvas.getGraphicsContext2D();

        //Startowy waz
        for (int i = 0; i < 3; i++)
            snakeFigure.add(new Point(columns / 2, rows / 2));
        snakeHead = snakeFigure.get(0);

        //Poruszanie się
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode key = keyEvent.getCode();
                if (key == KeyCode.UP || key == KeyCode.W) {
                    if (actualDirection != Directions.DOWN) actualDirection = Directions.UP;
                }
                if (key == KeyCode.DOWN || key == KeyCode.S) {
                    if (actualDirection != Directions.UP) actualDirection = Directions.DOWN;
                }
                if (key == KeyCode.LEFT || key == KeyCode.A) {
                    if (actualDirection != Directions.RIGHT) actualDirection = Directions.LEFT;
                }
                if (key == KeyCode.RIGHT || key == KeyCode.D) {
                    if (actualDirection != Directions.LEFT) actualDirection = Directions.RIGHT;
                }
            }
        });

        //Jedzenie
        generateApples();

        //Animacja klatka po klatce
        timeline = new Timeline(new KeyFrame(Duration.millis(123), actionEvent -> run()));
        //Ile razy ma sie powtarzac animacja (tutaj - niezdefiniowane)
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void run() {
        //Sprawdzanie, czy gra zakonczona
        if (gameIsOver) {
            graphicsContext.setFill(Color.web("#3b1818"));
            graphicsContext.setFont(new Font("Comic Sans MS", 60));
            graphicsContext.fillText("Przegrałeś!", width / 3.92, height / 2);
            return;
        }

        //Generacja planszy i rzeczy
        makeBackground();
        paintApples();
        paintSnake();
        paintScore();

        //Podazanie weza za glowa
        for (int i = snakeFigure.size() - 1; i >= 1; i--) {
            snakeFigure.get(i).x = snakeFigure.get(i - 1).x;
            snakeFigure.get(i).y = snakeFigure.get(i - 1).y;
        }

        //Ruch
        changeDirection();

        //Jedzenie
        eatApple();

        isGameOver();
    }

    /**
     * Kolorowanie tła
     */
    public void makeBackground() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //Wybieramy farbe
                if ((i + j) % 2 == 0)
                    graphicsContext.setFill(Color.web("#f58742"));
                else
                    graphicsContext.setFill(Color.web("#f56042"));

                //Wypelniamy kolorem
                graphicsContext.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);

            }
        }
    }

    /**
     * Generowanie owoców w losowym miejscu
     */
    public void generateApples() {
        bigloop:
        while (true) {
            //Randomowa pozycja jablka
            appleX = rnd.nextInt(rows);
            appleY = rnd.nextInt(columns);

            //Sprawdzanie, czy waz dotknal jablka
            for (Point s : snakeFigure)
                if (s.getX() == appleX && s.getY() == appleY)
                    continue bigloop;

            //1:10 pojawia sie zle jablko
            if (applesCounter % 10 == 0 && applesCounter != 0) {
                apple = new Image(badApplePath);
                isActualAppleGood = false;
            } else {
                apple = new Image(applePath);
                isActualAppleGood = true;
            }
            applesCounter++;

            break;
        }
    }

    /**
     * Rysuje jablko na koordynatach X,Y o rozmiarze jednego pola
     */
    public void paintApples() {
        graphicsContext.drawImage(apple, appleX * squareSize, appleY * squareSize, squareSize, squareSize);
    }

    /**
     * Rysuje snake
     */
    public void paintSnake() {
        //Glowa
        graphicsContext.drawImage(new Image(snakeHeadPath), snakeHead.getX() * squareSize, snakeHead.getY() * squareSize, squareSize, squareSize);

        //Cialo
        graphicsContext.setFill(Color.web("#5fa637"));
        for (int i = 1; i < snakeFigure.size(); i++) {
            Point point = snakeFigure.get(i);
            graphicsContext.fillRoundRect(point.getX() * squareSize,
                    point.getY() * squareSize, squareSize, squareSize, 15, 15);
        }
    }

    public void moveRight() {
        snakeHead.x++;
    }

    public void moveLeft() {
        snakeHead.x--;
    }

    public void moveDown() {
        snakeHead.y++;
    }

    public void moveUp() {
        snakeHead.y--;
    }

    /**
     * Zmienia aktualny kierunek
     */
    public void changeDirection() {
        switch (actualDirection) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
    }

    /**
     * Koniec gry, jesli waz uderzy w sciane lub w siebie.
     */
    public void isGameOver() {
        //W sciane
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * squareSize >= width || snakeHead.y * squareSize >= height) {
            gameIsOver = true;
        }

        //W siebie
        for (int i = 1; i < snakeFigure.size(); i++) {
            if (snakeHead.x == snakeFigure.get(i).getX() && snakeHead.getY() == snakeFigure.get(i).getY()) {
                gameIsOver = true;
                break;
            }
        }
    }

    /**
     * Zjedzenie jablka, dodawanie punktow i pojawienie sie nowego
     */
    public void eatApple() {
        if (snakeHead.getX() == appleX && snakeHead.getY() == appleY) {
            if (!isActualAppleGood) {
                if (snakeFigure.size() > 1) {
                    snakeFigure.remove(snakeFigure.size() - 1);
                }
                generateApples();
                score++;
            } else {
                snakeFigure.add(new Point(-1, -1));
                generateApples();
                score++;
            }
        }
    }

    /**
     * Wyświetla wynik w lewym górnym rogu
     */
    public void paintScore() {
        graphicsContext.setFill(Color.WHITESMOKE);
        graphicsContext.setFont(new Font("Comic Sans MS", 25));
        graphicsContext.fillText("Wynik: " + score, 7, 30);
    }


    public static void main(String[] args) {
        launch();
    }

}