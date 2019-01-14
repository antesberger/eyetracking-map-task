package gazetracking.lmu.com.eyetracking_map_webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.startscreen);

        final Button startButton = findViewById(R.id.button);
        startButton.setEnabled(false);

        final EditText nameField = findViewById(R.id.editText);
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int count, int i2) {
                if(count >= 4) {
                    startButton.setEnabled(true);
                } else {
                    startButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SimpleDateFormat startTime = new SimpleDateFormat("yyyy-MM-dd");
                String participant = nameField.getText().toString();
                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("participant", participant);
                bundle.putString("startTime", startTime.format(new Date()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
