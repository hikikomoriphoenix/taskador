package marabillas.loremar.taskador.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import marabillas.loremar.taskador.R;

/**
 * Dots that bounce in wave-like motion. There are two options for animation. One is dots bounce
 * continuously. The other is dots bounce once per wave motion that is dots wait in place until
 * the last dot completes its bounce.
 */
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

    /**
     * Dots continuously bounce in wavelike motion.
     */
    public void animateContinuousWavesOfDots() {
        // Dots start bouncing one after another for every specified time interval.
        for (int i = 0; i < numDots; ++i) {
            runnables[i] = new DotLoopRunnable(dots[i]);
            handler.postDelayed(runnables[i], (i + 1) * startInterval);
        }
    }

    /**
     * Dots bounce in wavelike motion. Each dot bounces once until the end of a wave animation,
     * and then another wave is made.
     */
    public void animateSingleWavesofDots() {
        makeAWaveAnimation();
    }

    private void makeAWaveAnimation() {
        waveEndAction = new WaveEndAction();

        // Dots bounce one after another for every specified time interval.
        for (int i = 0; i < numDots; ++i) {
            runnables[i] = new DotBounceRunnable(dots[i]);
            handler.postDelayed(runnables[i], i * startInterval);
        }
    }

    /**
     * Completely stop animation.
     */
    public void stopAnimation() {
        handler.removeCallbacks(waveEndAction);

        for (int i = 0; i < numDots; ++i) {
            handler.removeCallbacks(runnables[i]);
            dots[i].stop();
        }
    }

    /**
     * Runnable for dot to bounce in a loop
     */
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

    /**
     * Runnable for dot to bounce once. If dot is last then set it to run
     * {@link marabillas.loremar.taskador.ui.view.WaitingDotsView.WaveEndAction} after its bounce.
     */
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

    /**
     * Calls {@link WaitingDotsView#makeAWaveAnimation()} to make another wave
     */
    private class WaveEndAction implements Runnable {
        @Override
        public void run() {
            makeAWaveAnimation();
        }
    }
}
