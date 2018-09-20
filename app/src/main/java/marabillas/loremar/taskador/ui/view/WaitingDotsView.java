package marabillas.loremar.taskador.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import marabillas.loremar.taskador.R;

public class WaitingDotsView extends FrameLayout {
    private BouncingDotView[] dots;
    private Handler handler;
    private Runnable[] runnables;
    private WaveEndAction waveEndAction;
    private int startInterval;

    private final int numDots = 4;

    public WaitingDotsView(Context context) {
        super(context);
        init(context);
    }

    public WaitingDotsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.activity_splash_dots, this);

        handler = new Handler(Looper.getMainLooper());
        dots = new BouncingDotView[numDots];
        runnables = new Runnable[numDots];

        dots[0] = findViewById(R.id.dot1);
        dots[1] = findViewById(R.id.dot2);
        dots[2] = findViewById(R.id.dot3);
        dots[3] = findViewById(R.id.dot4);

        startInterval = getResources().getInteger(R.integer.activity_splash_dot_start_interval);
    }

    public void startAnimation() {
        for (int i = 0; i < numDots; ++i) {
            runnables[i] = new DotLoopRunnable(dots[i]);
            handler.postDelayed(runnables[i], (i + 1) * startInterval);
        }
    }

    public void makeAWaveAnimation() {
        waveEndAction = new WaveEndAction();
        for (int i = 0; i < numDots; ++i) {
            runnables[i] = new DotBounceRunnable(dots[i]);
            handler.postDelayed(runnables[i], i * startInterval);
        }
    }

    public void stopAnimation() {
        handler.removeCallbacks(waveEndAction);

        for (int i = 0; i < numDots; ++i) {
            dots[i].stop();
            handler.removeCallbacks(runnables[i]);
        }
    }

    private class DotLoopRunnable implements Runnable {
        private BouncingDotView dot;

        private DotLoopRunnable(BouncingDotView dot) {
            this.dot = dot;
        }

        @Override
        public void run() {
            TypedValue bounceHeight = new TypedValue();
            getResources().getValue(R.integer.activity_splash_bounce_height,
                    bounceHeight, true);
            dot.loop(bounceHeight.getFloat());
        }
    }

    private class DotBounceRunnable implements Runnable {
        private BouncingDotView dot;

        private DotBounceRunnable(BouncingDotView dot) {
            this.dot = dot;
        }

        @Override
        public void run() {
            TypedValue bounceHeight = new TypedValue();
            getResources().getValue(R.integer.activity_splash_bounce_height,
                    bounceHeight, true);
            if (dot.getId() != R.id.dot4) {
                dot.bounce(bounceHeight.getFloat(), null);
            } else {
                dot.bounce(bounceHeight.getFloat(), waveEndAction);
            }
        }
    }

    private class WaveEndAction implements Runnable {
        @Override
        public void run() {
            makeAWaveAnimation();
        }
    }
}
