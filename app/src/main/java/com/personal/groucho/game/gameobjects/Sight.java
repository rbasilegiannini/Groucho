package com.personal.groucho.game.gameobjects;

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

public class Sight {
    private final World world;
    private final Vec2 origin, s1, s2, s3;
    private final float distance;
    private Orientation orientation;

    public Sight (World world, Vec2 origin, float distance) {
        this.world = world;
        this.origin = origin;
        this.distance = distance;
        this.orientation = Orientation.UP;

        // Orientation: UP
        float alpha = (float) Math.toRadians(-135);
        float beta = (float) Math.toRadians(-90);
        float gamma = (float) Math.toRadians(-45);

        s1 = new Vec2(
                (float)(this.distance *cos(alpha)) + origin.getX(),
                (float)(this.distance *sin(alpha)) + origin.getY());
        s2 = new Vec2(
                (float)(this.distance *cos(beta)) + origin.getX(),
                (float)(this.distance *sin(beta)) + origin.getY());
        s3 = new Vec2(
                (float)(this.distance *cos(gamma)) + origin.getX(),
                (float)(this.distance *sin(gamma)) + origin.getY());

        /*
        * s1 = new Vec2();
        * s2 = new Vec2();
        * s3 = new Vec3();
        *
        * setNewOrientation(orientation);
        * */
    }

    public void see() {
//        Log.i("RayCast", "origin: " + origin.getX() + "," + origin.getY());
//        Log.i("RayCast", "s1: " + s1.getX() + "," + s1.getY());
//        Log.i("RayCast", "s2: " + s2.getX() + "," + s2.getY());
//        Log.i("RayCast", "s3: " + s3.getX() + "," + s3.getY());

        // TODO: Use a for
        world.rayCast(
                new RayCastCallback(){
                    public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float fraction) {
                        GameObject hitGO = (GameObject) fixture.getBody().getUserData();
                        if (hitGO.role == Role.PLAYER)
                            Log.i("RayCast", "s1 - Player");
                        return fraction;
                    }
                },
                fromBufferToMetersX(origin.getX()), fromBufferToMetersY(origin.getY()),
                fromBufferToMetersX(s1.getX()), fromBufferToMetersY(s1.getY())
        );

        world.rayCast(
                new RayCastCallback(){
                    public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float fraction) {
                        GameObject hitGO = (GameObject) fixture.getBody().getUserData();
                        if (hitGO.role == Role.PLAYER)
                            Log.i("RayCast", "s2 - Player");
                        return fraction;
                    }
                },
                fromBufferToMetersX(origin.getX()), fromBufferToMetersY(origin.getY()),
                fromBufferToMetersX(s2.getX()), fromBufferToMetersY(s2.getY())
        );

        world.rayCast(
                new RayCastCallback(){
                    public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float fraction) {
                        GameObject hitGO = (GameObject) fixture.getBody().getUserData();
                        if (hitGO.role == Role.PLAYER)
                            Log.i("RayCast", "s3 - Player");
                        return fraction;
                    }
                },
                fromBufferToMetersX(origin.getX()), fromBufferToMetersY(origin.getY()),
                fromBufferToMetersX(s3.getX()), fromBufferToMetersY(s3.getY())
        );
    }

    public void drawDebugRayCast(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawLine(origin.getX(), origin.getY(), s1.getX(), s1.getY(), paint);
        canvas.drawLine(origin.getX(), origin.getY(), s2.getX(), s2.getY(), paint);
        canvas.drawLine(origin.getX(), origin.getY(), s3.getX(), s3.getY(), paint);
    }

    public void updateSight(float increaseX, float increaseY) {
        origin.setX(origin.getX() + increaseX);
        origin.setY(origin.getY() + increaseY);

        s1.setX(s1.getX() + increaseX);
        s1.setY(s1.getY() + increaseY);
        s2.setX(s2.getX() + increaseX);
        s2.setY(s2.getY() + increaseY);
        s3.setX(s3.getX() + increaseX);
        s3.setY(s3.getY() + increaseY);
    }

    public void setNewOrientation(Orientation orientation){
        if (this.orientation != orientation) {
            float alpha = 0;
            float beta = 0;
            float gamma = 0;

            // TODO: Optimize this computations
            switch (orientation) {
                case DOWN:
                    alpha = (float) Math.toRadians(45);
                    beta = (float) Math.toRadians(90);
                    gamma = (float) Math.toRadians(135);
                    break;

                case UP:
                    alpha = (float) Math.toRadians(-135);
                    beta = (float) Math.toRadians(-90);
                    gamma = (float) Math.toRadians(-45);
                    break;

                case RIGHT:
                    alpha = (float) Math.toRadians(-45);
                    beta = (float) Math.toRadians(0);
                    gamma = (float) Math.toRadians(45);
                    break;

                case LEFT:
                    alpha = (float) Math.toRadians(-225);
                    beta = (float) Math.toRadians(-180);
                    gamma = (float) Math.toRadians(-135);
                    break;
            }
            computePoints(alpha, beta, gamma);
            this.orientation = orientation;
        }
    }

    private void computePoints(float alpha, float beta, float gamma) {
        s1.setX((float)(distance*cos(alpha)) + origin.getX());
        s1.setY((float)(distance*sin(alpha)) + origin.getY());

        s2.setX((float)(distance*cos(beta)) + origin.getX());
        s2.setY((float)(distance*sin(beta)) + origin.getY());

        s3.setX((float)(distance*cos(gamma)) + origin.getX());
        s3.setY((float)(distance*sin(gamma)) + origin.getY());
    }

}
