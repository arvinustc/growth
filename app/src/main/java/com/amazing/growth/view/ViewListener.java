package com.amazing.growth.view;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by amazing on 2016/4/17.
 */
public class ViewListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private static final String TAG = "ViewListener";
    private int distance = 200;
    private int velocity = 400;
    private GestureDetector mGestureDetector;

    public ViewListener(Context context) {
        super();
        mGestureDetector = new GestureDetector(context, this);
    }

    public boolean turnLeft() {
        return false;
    }

    public boolean turnRight() {
        return false;
    }

    public boolean turnUp() {
        return false;
    }

    public boolean turnDown() {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // LEFT
        if (e1.getX() - e2.getX() > distance
                && Math.abs(velocityX) > velocity) {
            turnLeft();
        }
        // RIGHT
        if (e2.getX() - e1.getX() > distance
                && Math.abs(velocityX) > velocity) {
            turnRight();
        }

        // UP
        if (e1.getY() - e2.getY() > distance
                && Math.abs(velocityY) > velocity) {
            turnUp();
        }
        // DOWN
        if (e2.getY() - e1.getY() > distance
                && Math.abs(velocityY) > velocity) {
            turnDown();
        }

        return false;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public GestureDetector getGestureDetector() {
        return mGestureDetector;
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.mGestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return false;
    }
}
