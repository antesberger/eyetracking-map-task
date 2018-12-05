package gazetracking.lmu.com.eyetracking_map_webview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String participant = "empty";
    String startTime = "no-date";

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

    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            String action = MotionEvent.actionToString(event.getAction());
            String rawX = String.valueOf(event.getRawX());
            String rawY = String.valueOf(event.getRawY());
            String downtime = String.valueOf(event.getDownTime());
            writeFileOnInternalStorage(MainActivity.this, "MotionEvents.txt", action + "; " + rawX + "; " + rawY + "; " + downtime);
            return false;
        }
    };

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
