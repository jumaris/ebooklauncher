package com.eink.norefresh;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

/**
 * Nook Touch NoRefresh.
 *
 * @author DairyKnight <dairyknight@gmail.com>
 * http://forum.xda-developers.com/showthread.php?t=1183173
 */
public class A2Service extends Service {
    View dummyView;
    private float lastY;
    private float lastX;
    private int downHit;
    private int upHit;
    private static int HIT_COUNT_TARGET = 4;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OnTouchListener touch = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                if ((x > A2Service.this.lastX) && (y > A2Service.this.lastY)) {
                    A2Service.this.downHit++;
                } else {
                    A2Service.this.downHit = 0;
                }

                if ((x < A2Service.this.lastX) && (y < A2Service.this.lastY)) {
                    A2Service.this.upHit++;
                } else {
                    A2Service.this.upHit = 0;
                }

                A2Service.this.lastX = x;
                A2Service.this.lastY = y;

                if (A2Service.this.downHit == (A2Service.HIT_COUNT_TARGET - 1)) {
                    N2EpdController.enterA2Mode();
                    A2Service.this.downHit = 0;
                } else if (A2Service.this.upHit == (A2Service.HIT_COUNT_TARGET - 1)) {
                    N2EpdController.exitA2Mode();
                    N2EpdController.setGL16Mode(1);
                    A2Service.this.upHit = 0;
                }
                return false;
            }
        };
        /*
         * Invisible dummy view to receive touch events
         */
        this.dummyView = new View(this);
        this.dummyView.setOnTouchListener(touch);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(this.dummyView, params);

        this.downHit = this.upHit = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.dummyView != null) {
            ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).removeView(this.dummyView);
            this.dummyView = null;
        }
    }
}
