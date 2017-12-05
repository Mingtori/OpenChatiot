package com.min.openchatiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class AreaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        final Spinner spinner = findViewById(R.id.area);

        Button goButton = findViewById(R.id.button_go);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AreaActivity.this, MainActivity.class);
                intent.putExtra("chatroom", (String) spinner.getSelectedItem());
                startActivity(intent);
            }
        });

    }
}
