package com.nauka.purchases;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/*Класс который обеспечивает корректный клик по элементам CardView который
* находиться внутри RecycleView*/

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private final GestureDetector mGestureDetector;
    private final GestureListener mGestureListener;

    private final OnItemMotionEventListener mListener;

    public RecyclerItemClickListener(Context context, RecyclerView recyclerView, OnItemMotionEventListener listener) {
        mListener = listener;
        mGestureListener = new GestureListener();

        mGestureDetector = new GestureDetector(context, mGestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

        mGestureListener.setRecyclerView(view);

        mGestureDetector.onTouchEvent(e);

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private RecyclerView mRecyclerView;

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View view = findView(e);
            if (validate(view, e)) {
                int position = findPosition(view);
                mListener.onItemClick(view, position);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View view = findView(e);
            if (validate(view, e)) {
                int position = findPosition(view);
                mListener.onItemLongClick(view, position);
            }
        }

        private boolean validate(View view, MotionEvent event) {
            if (view != null && mListener != null) {
                return !handleClickOfView(view, event);
            } else {
                return false;
            }
        }

        private View findView(MotionEvent e) {
            View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            return childView;
        }

        private int findPosition(View view) {
            return mRecyclerView.getChildPosition(view);
        }

        public void setRecyclerView(RecyclerView mRecyclerView) {
            this.mRecyclerView = mRecyclerView;
        }
    }

    private boolean handleClickOfView(View view, MotionEvent e) {
        boolean isClickOnView = isPointInsideView(e.getRawX(), e.getRawY(), view);
        if (!isClickOnView) {
            return false;
        }

        if (view.hasOnClickListeners()) {
            return true;
        } else if (view instanceof ViewGroup) {
            ViewGroup parent = ((ViewGroup) view);
            boolean isClickOccured = false;

            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                boolean isClickOccuredTemp = handleClickOfView(child, e);
                if (isClickOccuredTemp) {
                    isClickOccured = true;
                }
            }

            return isClickOccured;
        }
        return false;
    }

    /**
     * Determines if given points are inside view
     *
     * @param x    - x coordinate of point
     * @param y    - y coordinate of point
     * @param view - view object to compare
     * @return true if the points are within view bounds, false otherwise
     */
    private boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if ((x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()))) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnItemMotionEventListener {
        public void onItemClick(View view, int position);

        public void onItemLongClick(View view, int position);
    }

    public static class SimpleItemMotionEventListener implements OnItemMotionEventListener {

        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}
