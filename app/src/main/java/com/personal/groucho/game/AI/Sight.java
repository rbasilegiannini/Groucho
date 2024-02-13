package com.personal.groucho.game.AI;

import static com.personal.groucho.game.Utils.isInTriangle;
import static com.personal.groucho.game.constants.System.distSight;
import static com.personal.groucho.game.constants.System.maxInvisiblePlayer;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sight {
    private final AIComponent aiComponent;
    private final World world;
    private Orientation orientation;
    private float originX, originY;
    private float playerPosX, playerPosY;
    private float phase;
    private long lastSeenMillis;
    private final List<GameObject> hitGameObjects = new ArrayList<>();
    private final List<Float> fractions = new ArrayList<>();

    public Sight (AIComponent aiComponent, World world , Vec2 origin, Orientation orientation) {
        this.aiComponent = aiComponent;
        this.world = world;
        this.orientation = orientation;

        originX = origin.getX();
        originY = origin.getY();

        changeDirection();
    }

    public void see(GameWorld gameWorld) {
        playerPosX = gameWorld.getPlayerPosition().getX();
        playerPosY = gameWorld.getPlayerPosition().getY();

        if(isInTriangle(originX, originY, playerPosX, playerPosY, distSight, phase)){
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
                    fromBufferToMetersX(originX), fromBufferToMetersY(originY),
                    fromBufferToMetersX(playerPosX), fromBufferToMetersY(playerPosY)
            );
        }

        if (!fractions.isEmpty()) {
            int indexLessFraction = fractions.indexOf(Collections.min(fractions));
            GameObject firstGO = hitGameObjects.get(indexLessFraction);
            if(firstGO.role == Role.PLAYER) {
                if (aiComponent.isPlayerVisible()) {
                    aiComponent.setPlayerEngaged(true);
                    lastSeenMillis = System.currentTimeMillis();
                }
            }
            hitGameObjects.clear();
            fractions.clear();
        }

        if (aiComponent.isPlayerEngaged() && System.currentTimeMillis() - lastSeenMillis > maxInvisiblePlayer) {
            aiComponent.setPlayerEngaged(false);
        }
    }

    public void updateSightPosition(Vec2 origin) {
        originX = origin.getX();
        originY = origin.getY();
    }

    public void setNewOrientation(Orientation orientation){
        if (this.orientation != orientation) {
            this.orientation = orientation;
            changeDirection();
        }
    }

    private void changeDirection() {
        switch (this.orientation) {
            case UP:
                phase = -0.785398f; // degree = -45
                break;

            case DOWN:
                phase = 2.35619f; // degree = 135
                break;

            case RIGHT:
                phase = 0.785398f; // degree = 45
                break;

            case LEFT:
                phase = -2.35619f; // degree = -135
                break;
        }
    }

    public void drawDebugSight(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawLine(originX, originY,
                originX + (float)(distSight* cos(phase)), originY+(float)(distSight * sin(phase)),
                paint);
        canvas.drawLine(originX, originY,
                originX+(float)(distSight *cos(phase -1.5708)), originY+(float)(distSight *sin(phase -1.5708)),
                paint);
        canvas.drawLine(
                originX+(float)(distSight *cos(phase -1.5708)), originY+(float)(distSight *sin(phase -1.5708)),
                originX + (float)(distSight * cos(phase)), originY+(float)(distSight * sin(phase)), paint);

        if(isInTriangle(originX, originY, playerPosX, playerPosY, distSight, phase)){
            paint.setColor(Color.GREEN);
            canvas.drawLine(originX, originY, playerPosX, playerPosY, paint);
        }
    }
}
