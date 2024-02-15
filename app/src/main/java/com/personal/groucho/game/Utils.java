package com.personal.groucho.game;

import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.GameWorld.physicalSize;
import static com.personal.groucho.game.GameWorld.screenSize;
import static com.personal.groucho.game.constants.CharacterProperties.enemyFovInRad;
import static com.personal.groucho.game.constants.System.characterDimY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Utils {

    // Screen conversions
    public static float fromScreenToBufferX(float x) { return x/screenSize.width*bufferWidth; }
    public static float fromScreenToBufferY(float y) { return y/screenSize.height*bufferHeight; }

    public static float fromMetersToBufferX(float xLocal) {
        return ((xLocal - physicalSize.xmin) / (physicalSize.xmax - physicalSize.xmin)) * bufferWidth;
    }
    public static float fromMetersToBufferY(float yLocal) {
        return ((yLocal - physicalSize.ymin) / (physicalSize.ymax - physicalSize.ymin)) * bufferHeight;
    }

    public static float fromBufferToMetersX(float xGlobal) {
        return (xGlobal / bufferWidth) * (physicalSize.xmax - physicalSize.xmin) + physicalSize.xmin;
    }
    public static float fromBufferToMetersY(float yGlobal) {
        return (yGlobal / bufferHeight) * (physicalSize.ymax - physicalSize.ymin) + physicalSize.ymin;
    }

    public static float toPixelsXLength(float x) {return x/physicalSize.width*bufferWidth;}
    public static float toPixelsYLength(float y)
    {
        return y/physicalSize.height*bufferHeight;
    }
    public static float toMetersXLength(float x) {return x/bufferWidth * physicalSize.width;}
    public static float toMetersYLength(float y) {return y/bufferHeight * physicalSize.height;}

    public static boolean isInCircle(float centerX, float centerY, float x, float y, float distSqr) {
        double pointerPositionSqr = Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
        return pointerPositionSqr <= distSqr;
    }

    public static boolean isInTriangle(float originX, float originY, float x, float y, float dist, float rad) {
        float[] vecO = new float[]{originX - x, originY - y};
        float[] vecA = new float[]{
                (float)(originX + dist*Math.cos(rad) - x),
                (float)(originY + dist*Math.sin(rad) - y)};
        float[] vecB = new float[]{
                (float)(originX + dist*Math.cos(rad-enemyFovInRad) - x),
                (float)(originY + dist*Math.sin(rad-enemyFovInRad) - y)};

        double crossProduct1 = vecO[0] * vecA[1] - vecO[1] * vecA[0];
        double crossProduct2 = vecA[0] * vecB[1] - vecA[1] * vecB[0];
        double crossProduct3 = vecB[0] * vecO[1] - vecB[1] * vecO[0];

        return (crossProduct1 >= 0 && crossProduct2 >= 0 && crossProduct3 >= 0)
                || (crossProduct1 <= 0 && crossProduct2 <= 0 && crossProduct3 <= 0);
    }

    public static float distBetweenVec(Vec2 vec1, Vec2 vec2){
        float deltaX = vec2.getX() - vec1.getX();
        float deltaY = vec2.getY() - vec1.getY();
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static Orientation directionBetweenGO(GameObject go, GameObject goToTurn) {
        Vec2 posGO = ((PositionComponent) go.getComponent(POSITION)).getPos();
        Vec2 posGOToTurn = ((PositionComponent) goToTurn.getComponent(POSITION)).getPos();

        if (posGO.getY() > posGOToTurn.getY() - characterScaleFactor*characterDimY &&
            posGO.getY() < posGOToTurn.getY() + characterScaleFactor*characterDimY
        ) {
            if (posGO.getX() < posGOToTurn.getX()) {
                return Orientation.LEFT;
            }
            if (posGO.getX() > posGOToTurn.getX()) {
                return Orientation.RIGHT;
            }
        }
        else {
            if (posGO.getY() > posGOToTurn.getY()) {
                return Orientation.DOWN;
            }
            if (posGO.getY() < posGOToTurn.getY()) {
                return Orientation.UP;
            }
        }

        return ((PositionComponent)goToTurn.getComponent(POSITION)).getOrientation();
    }
}
