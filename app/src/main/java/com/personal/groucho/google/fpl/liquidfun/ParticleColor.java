/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.personal.groucho.google.fpl.liquidfun;

public class ParticleColor {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ParticleColor(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ParticleColor obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_ParticleColor(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ParticleColor() {
    this(liquidfunJNI.new_ParticleColor__SWIG_0(), true);
  }

  public ParticleColor(short r, short g, short b, short a) {
    this(liquidfunJNI.new_ParticleColor__SWIG_1(r, g, b, a), true);
  }

  public ParticleColor(Color color) {
    this(liquidfunJNI.new_ParticleColor__SWIG_2(Color.getCPtr(color), color), true);
  }

  public boolean isZero() {
    return liquidfunJNI.ParticleColor_isZero(swigCPtr, this);
  }

  public void set(short r_, short g_, short b_, short a_) {
    liquidfunJNI.ParticleColor_set__SWIG_0(swigCPtr, this, r_, g_, b_, a_);
  }

  public void set(Color color) {
    liquidfunJNI.ParticleColor_set__SWIG_1(swigCPtr, this, Color.getCPtr(color), color);
  }

  public boolean equals(ParticleColor color) {
    return liquidfunJNI.ParticleColor_equals(swigCPtr, this, ParticleColor.getCPtr(color), color);
  }

  public void mix(ParticleColor mixColor, int strength) {
    liquidfunJNI.ParticleColor_mix(swigCPtr, this, ParticleColor.getCPtr(mixColor), mixColor, strength);
  }

  public static void mixColors(ParticleColor colorA, ParticleColor colorB, int strength) {
    liquidfunJNI.ParticleColor_mixColors(ParticleColor.getCPtr(colorA), colorA, ParticleColor.getCPtr(colorB), colorB, strength);
  }

  public void setR(short value) {
    liquidfunJNI.ParticleColor_r_set(swigCPtr, this, value);
  }

  public short getR() {
    return liquidfunJNI.ParticleColor_r_get(swigCPtr, this);
  }

  public void setG(short value) {
    liquidfunJNI.ParticleColor_g_set(swigCPtr, this, value);
  }

  public short getG() {
    return liquidfunJNI.ParticleColor_g_get(swigCPtr, this);
  }

  public void setB(short value) {
    liquidfunJNI.ParticleColor_b_set(swigCPtr, this, value);
  }

  public short getB() {
    return liquidfunJNI.ParticleColor_b_get(swigCPtr, this);
  }

  public void setA(short value) {
    liquidfunJNI.ParticleColor_a_set(swigCPtr, this, value);
  }

  public short getA() {
    return liquidfunJNI.ParticleColor_a_get(swigCPtr, this);
  }

}
