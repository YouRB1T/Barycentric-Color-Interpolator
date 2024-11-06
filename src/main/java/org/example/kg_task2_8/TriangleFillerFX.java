package org.example.kg_task2_8;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.Arrays;

public class TriangleFillerFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(700, 700);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int[] x = {100, 400, 150};
        int[] y = {100, 100, 500};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

        fillTriangle(gc, x, y, colors);

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Triangle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void fillTriangle(GraphicsContext gc, int x1, int y1, Color color1,
                             int x2, int y2, Color color2,
                             int x3, int y3, Color color3) {

        fillTriangle(gc, x1, y1, x2, y2, x3, y3, color1, color2, color3);
    }

    public void fillTriangle(GraphicsContext gc, int x1, int y1,
                             int x2, int y2,
                             int x3, int y3, Color color) {

        fillTriangle(gc, x1, y1, x2, y2, x3, y3, color, color, color);
    }

    private void fillTriangle(GraphicsContext gc, int[] x, int[] y, Color[] colors) {
        Integer[] indices = {0, 1, 2};

        int x0 = x[indices[0]], y0 = y[indices[0]];
        int x1 = x[indices[1]], y1 = y[indices[1]];
        int x2 = x[indices[2]], y2 = y[indices[2]];

        Color c0 = colors[indices[0]];
        Color c1 = colors[indices[1]];
        Color c2 = colors[indices[2]];

        fillTriangle(gc, x0, y0, x1, y1, x2, y2, c0, c1, c2);
    }

    private void fillTriangle(GraphicsContext gc, int x0, int y0, int x1, int y1, int x2, int y2, Color c0, Color c1, Color c2) {
        float invslope1 = (float)(x2 - x0) / (y2 - y0);
        float invslope2 = (float)(x2 - x1) / (y2 - y1);

        float curx1 = x2;
        float curx2 = x2;

        for (int y = y2; y > y0; y--) {
            float[] bary1 = computeBarycentric(curx1, y, new int[]{x0, x1, x2}, new int[]{y0, y1, y2});
            float[] bary2 = computeBarycentric(curx2, y, new int[]{x0, x1, x2}, new int[]{y0, y1, y2});

            if (bary1 != null && bary2 != null) {
                Color color1 = interpolateColor(new Color[]{c0, c1, c2}, bary1);
                Color color2 = interpolateColor(new Color[]{c0, c1, c2}, bary2);
                drawHorizontalLine(gc, (int)curx1, (int)curx2, y, color1, color2);
            }
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    private void drawHorizontalLine(GraphicsContext gc, int xStart, int xEnd, int y, Color color1, Color color2) {
        if (xStart > xEnd) {
            int temp = xStart;
            xStart = xEnd;
            xEnd = temp;
            Color tempColor = color1;
            color1 = color2;
            color2 = tempColor;
        }
        for (int x = xStart; x <= xEnd; x++) {
            float t = (float)(x - xStart) / (xEnd - xStart);
            Color interpolatedColor = color1.interpolate(color2, t);
            gc.getPixelWriter().setColor(x, y, interpolatedColor);
        }
    }


    private float[] computeBarycentric(float px, float py, int[] x, int[] y) {
        float denom = (float) ((y[1] - y[2]) * (x[0] - x[2]) + (x[2] - x[1]) * (y[0] - y[2]));
        if (denom == 0) return null;

        float w1 = ((y[1] - y[2]) * (px - x[2]) + (x[2] - x[1]) * (py - y[2])) / denom;
        float w2 = ((y[2] - y[0]) * (px - x[2]) + (x[0] - x[2]) * (py - y[2])) / denom;
        float w3 = 1 - w1 - w2;

        if (between(w1) && between(w2) && between(w3)) {
            return new float[]{w1, w2, w3};
        }

        return null;
    }

    private boolean between(double w) {
        return w >=0 && w <= 1;
    }

    private Color interpolateColor(Color[] colors, float[] weights) {
        double red = weights[0] * colors[0].getRed() + weights[1] * colors[1].getRed() + weights[2] * colors[2].getRed();
        double green = weights[0] * colors[0].getGreen() + weights[1] * colors[1].getGreen() + weights[2] * colors[2].getGreen();
        double blue = weights[0] * colors[0].getBlue() + weights[1] * colors[1].getBlue() + weights[2] * colors[2].getBlue();

        return new Color(red, green, blue, 1.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
