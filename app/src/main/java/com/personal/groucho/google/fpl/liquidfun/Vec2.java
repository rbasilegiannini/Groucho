/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.personal.groucho.google.fpl.liquidfun;

public class Vec2 {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected Vec2(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Vec2 obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_Vec2(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public Vec2() {
    this(liquidfunJNI.new_Vec2__SWIG_0(), true);
  }

  public Vec2(float x, float y) {
    this(liquidfunJNI.new_Vec2__SWIG_1(x, y), true);
  }

  public void set(float x_, float y_) {
    liquidfunJNI.Vec2_set(swigCPtr, this, x_, y_);
  }

  public void setX(float value) {
    liquidfunJNI.Vec2_x_set(swigCPtr, this, value);
  }

  public float getX() {
    return liquidfunJNI.Vec2_x_get(swigCPtr, this);
  }

  public void setY(float value) {
    liquidfunJNI.Vec2_y_set(swigCPtr, this, value);
  }

  public float getY() {
    return liquidfunJNI.Vec2_y_get(swigCPtr, this);
  }

}
