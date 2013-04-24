package com.abplus.secords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.*;

/**
 * User: kazhida
 * Date: 2012/08/15
 * Time: 16:02
 */
public class DrawView extends SurfaceView {

    public interface Callback {
        void onDown(PointF position);

        void onUp(PointF position);

        void onScroll(PointF position, PointF distance);

        void onScale(PointF focus, float scaleFactor);
    }

    public interface Drawer {
        void draw(Canvas canvas);
    }

    private Callback callback;
    private Drawer drawer;
    private GestureDetector detector1;
    private ScaleGestureDetector detector2;
//    private Transform transform = new Transform();

    public DrawView(Context context) {
        super(context);

        getHolder().addCallback(new HolderCallback());
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT
        ));
        setFocusable(true);

        detector1 = new GestureDetector(context, new GestureListener());
        detector2 = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector1.onTouchEvent(event);
        detector2.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            PointF p = new PointF(event.getX(), event.getY());
//            transform.inverse(p);
            if (callback != null) callback.onUp(p);
        }

        return true;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    public void redraw() {
        if (drawer != null) {
            Canvas canvas = getHolder().lockCanvas();
            try {
                canvas.save();
//                transform.apply(canvas);
                drawer.draw(canvas);
            } finally {
                canvas.restore();
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //nop
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            transform.bounds = new RectF(0, 0, width, height);  //仮
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //nop
        }
    }

    private class GestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            if (callback != null) {
                PointF p = new PointF(event.getX(), event.getY());
//                transform.inverse(p);
                callback.onDown(p);
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent event) {
            //nop
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            //nop
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
            if (callback != null) {
                PointF p = new PointF(event2.getX(), event2.getY());
                PointF d = new PointF(distanceX, distanceY);
//                PointF d = new PointF(distanceX / transform.scale, distanceY / transform.scale);
//                transform.inverse(p);
                callback.onScroll(p, d);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            return true;
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (callback != null) {
                PointF p = new PointF(detector.getFocusX(), detector.getFocusY());
//                transform.inverse(p);
                callback.onScale(p, detector.getScaleFactor());
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //nop
        }
    }
}
