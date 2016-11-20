package com.hippo.unifile.example;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE_DOCUMENT = 0;
    public static final int REQUEST_CODE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.action_get_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_CODE_SELECT);
            }
        });


        findViewById(R.id.action_open_document_tree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    try {
                        startActivityForResult(intent, REQUEST_CODE_DOCUMENT);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "error_cant_find_activity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findViewById(R.id.asset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        Intent intent = new Intent(this, FileActivity.class);
        intent.setData(data.getData());
        startActivity(intent);
    }
}
