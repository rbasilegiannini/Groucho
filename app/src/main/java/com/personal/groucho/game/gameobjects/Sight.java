package com.personal.groucho.game.gameobjects;

import static com.personal.groucho.game.constants.System.distanceOfEnemySight;
import static com.personal.groucho.game.constants.System.pointsOfEnemySight;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sight {
    private final AIComponent aiComponent;
    private final World world;
    private Orientation orientation;
    private final Vec2 origin;
    private final Vec2[] points;
    private final float[] angles;
    private final float phase;
    private final int numOfPoints;
    private long lastSeen;
    private final List<GameObject> hitGameObjects = new ArrayList<>();
    private final List<Float> fractions = new ArrayList<>();

    public Sight (AIComponent aiComponent, World world, Vec2 origin) {
        this.aiComponent = aiComponent;
        this.world = world;
        this.origin = origin;
        this.numOfPoints = pointsOfEnemySight;
        this.orientation = Orientation.UP;

        phase = (float) 90 /(pointsOfEnemySight-1);
        points = new Vec2[pointsOfEnemySight];
        angles = new float[pointsOfEnemySight];

        // Orientation: UP
        for (int i = 0; i<pointsOfEnemySight; i++) {
            angles[i] = (i * phase) - 135;
            points[i] = new Vec2();
            points[i].setX((float)(distanceOfEnemySight*cos(Math.toRadians(angles[i]))) + origin.getX());
            points[i].setY((float)(distanceOfEnemySight*sin(Math.toRadians(angles[i]))) + origin.getY());
        }

        // setNewOrientation(orientation);
    }

    public void see() {
        for (Vec2 point : points) {
            world.rayCast(
                    new RayCastCallback() {
                        public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float fraction) {
                            GameObject hitGO = (GameObject) fixture.getBody().getUserData();
                            if (hitGO.role != Role.ENEMY) {
                                hitGameObjects.add(hitGO);
                                fractions.add(fraction);
                            }
                            return fraction;
                        }
                    },
                    fromBufferToMetersX(origin.getX()), fromBufferToMetersY(origin.getY()),
                    fromBufferToMetersX(point.getX()), fromBufferToMetersY(point.getY())
            );
        }

        if (!fractions.isEmpty()) {
            int indexLessFraction = fractions.indexOf(Collections.min(fractions));
            GameObject firstGO = hitGameObjects.get(indexLessFraction);
            if(firstGO.role == Role.PLAYER) {
                Log.i("RayCast", "I see you");
                aiComponent.setPlayerEngaged(true);
                lastSeen = System.currentTimeMillis();
            }
            hitGameObjects.clear();
            fractions.clear();
        }

        if (aiComponent.isPlayerEngaged() && System.currentTimeMillis() - lastSeen > 5000) {
            aiComponent.setPlayerEngaged(false);
        }
    }

    public void updateSight(Vec2 origin) {
        for (Vec2 point : points) {
            point.setX(point.getX() + origin.getX()-this.origin.getX());
            point.setY(point.getY() + origin.getY()-this.origin.getY());
        }
        this.origin.setX(origin.getX());
        this.origin.setY(origin.getY());
    }

    public void setNewOrientation(Orientation orientation){
        if (this.orientation != orientation) {
            switch (orientation) {
                case UP:
                    for (int i = 0; i<numOfPoints; i++)
                        angles[i] = (i * phase) - 135;
                    break;

                case DOWN:
                    for (int i = 0; i<numOfPoints; i++)
                        angles[i] = (i * phase) + 45;
                    break;

                case RIGHT:
                    for (int i = 0; i<numOfPoints; i++)
                        angles[i] = (i * phase) - 45;
                    break;

                case LEFT:
                    for (int i = 0; i<numOfPoints; i++)
                        angles[i] = (i * phase) - 225;
                    break;
            }
            computePoints();
            this.orientation = orientation;
        }
    }

    private void computePoints() {
        for (int i = 0; i<numOfPoints; i++) {
            points[i].setX((float)(distanceOfEnemySight *cos(Math.toRadians(angles[i]))) + origin.getX());
            points[i].setY((float)(distanceOfEnemySight *sin(Math.toRadians(angles[i]))) + origin.getY());
        }
    }

    //
    public void drawDebugRayCast(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        for (Vec2 point : points)
            canvas.drawLine(origin.getX(), origin.getY(), point.getX(), point.getY(), paint);
    }
}
