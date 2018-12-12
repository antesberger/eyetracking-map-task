package gazetracking.lmu.com.eyetracking_map_webview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    String participant = "empty";
    String startTime = "no-date";
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            participant = bundle.getString("participant");
            startTime = bundle.getString("startTime");

        WebView webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.google.de/maps");

        webView.setOnTouchListener(onTouchListener);
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

        //init headline of logs
        String log = String.format(Locale.GERMAN, "%s; %s; %s; %s; %s, %s; %s, %s; %s, %s, %s, %s; %s",
                "pointerId",
                "eventTime",
                "action",
                "relativeX",
                "relativeY",
                "rawX",
                "rawY",
                "xPrecision",
                "yPrecision",
                "downTime",
                "orientation",
                "pressure",
                "size",
                "edgeFlags",
                "actionButton",
                "metaState"
        );

        writeFileOnInternalStorage(MainActivity.this, "motionEvents.txt", log);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int historySize = event.getHistorySize();
            int pointerCount = event.getPointerCount();

            //Action Move events are batched together -> loop thorugh historical data since last event trigger
            for (int h = 0; h < historySize; h++) {
                for (int p = 0; p < pointerCount; p++) {
                    try {
                        Integer pointerId = event.getPointerId(p);
                        Long eventTime = event.getHistoricalEventTime(h);
                        String action = MotionEvent.actionToString(event.getAction());
                        Float relativeX = event.getHistoricalY(p, h);
                        Float relativeY = event.getHistoricalX(p, h);
                        Float rawX = null;
                        Float rawY = null;
                        Float xPrecision = null;
                        Float yPrecision = null;
                        Long downTime = null;
                        Float orientation = event.getHistoricalOrientation(p,h);
                        Float pressure = event.getHistoricalPressure(p,h);
                        Float size = event.getHistoricalSize(p,h);
                        Integer edgeFlags = null;
                        Integer actionButton = null;
                        Integer metaState = null;

                        String log = String.format(Locale.GERMAN, "%d; %o; %s; %f; %f, %f; %f, %f; %f, %o, %f, %f; %f",
                                pointerId,
                                eventTime,
                                action,
                                relativeX,
                                relativeY,
                                rawX,
                                rawY,
                                xPrecision,
                                yPrecision,
                                downTime,
                                orientation,
                                pressure,
                                size,
                                edgeFlags,
                                actionButton,
                                metaState
                        );

                        writeFileOnInternalStorage(MainActivity.this, "motionEvents.txt", log);
                    } catch (Exception e) {
                        Log.e("Historical", "onTouch", e );
                    }
                }
            }

            //most current event data
            for (int p = 0; p < pointerCount; p++) {
                try {
                    Integer pointerId = event.getPointerId(p);
                    Long eventTime = event.getEventTime();
                    String action = MotionEvent.actionToString(event.getAction());
                    Float relativeX = event.getX(p);
                    Float relativeY = event.getY(p);
                    Float rawX = event.getRawX();
                    Float rawY = event.getRawY();
                    Float xPrecision = event.getXPrecision();
                    Float yPrecision = event.getYPrecision();
                    Long downTime = event.getDownTime();
                    Float orientation = event.getOrientation(p);
                    Float pressure = event.getPressure(p);
                    Float size = event.getSize(p);
                    Integer edgeFlags = event.getEdgeFlags();
                    Integer actionButton = event.getActionButton();
                    Integer metaState = event.getMetaState();

                    String log = String.format(Locale.GERMAN, "%d; %o; %s; %f; %f, %f; %f, %f; %f, %o, %f, %f; %f",
                            pointerId,
                            eventTime,
                            action,
                            relativeX,
                            relativeY,
                            rawX,
                            rawY,
                            xPrecision,
                            yPrecision,
                            downTime,
                            orientation,
                            pressure,
                            size,
                            edgeFlags,
                            actionButton,
                            metaState
                    );

                    writeFileOnInternalStorage(MainActivity.this, "motionEvents.txt", log);
                } catch (Exception e) {
                    Log.e("Historical", "onTouch", e );
                }
            }

            if (mDetector.onTouchEvent(event)) {
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onSingleTapConfirmed: " + event.toString());
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onDoubleTap: " + event.toString());
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onDoubleTapEvent: " + event.toString());
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onDown: " + event.toString());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onShowPress: " + event.toString());

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onSingleTapUp: " + event.toString());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onScroll: " + event1.toString() + ", " + event2.toString() + ", " + distanceX + ", " + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onLongPress: " + event.toString());
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        writeFileOnInternalStorage(MainActivity.this, "gesture.txt", "onFling: " + event1.toString() + ", " + event2.toString() + ", " + velocityX + ", " + velocityY);
        return false;
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        File file = new File(mcoContext.getFilesDir(), participant + "_" + startTime);

        if(!file.exists()){
            file.mkdir();
        }

        try{
            File outFile = new File(file, sFileName);
            FileWriter writer = new FileWriter(outFile, true);
            writer.append(timestamp.format(new Date()));
            writer.append("; ");
            writer.append(sBody);
            writer.append("\n");
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    };
}
