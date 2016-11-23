package com.hippo.unifile.example;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hippo.unifile.UniFile;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.action_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.action_get_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.action_open_document).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(MainActivity.this, "Only supported in Android4.4+", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.action_open_document_tree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(MainActivity.this, "Only supported in Android5.0+", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.asset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFileActivity(UniFile.fromAsset(getAssets(), "file2/haha").getUri());
            }
        });

        findViewById(R.id.resource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFileActivity(UniFile.fromResource(MainActivity.this, R.layout.activity_file).getUri());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        startFileActivity(data.getData());
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "error_cant_find_activity", Toast.LENGTH_SHORT).show();
        }
    }

    private void startFileActivity(Uri uri) {
        Intent intent = new Intent(this, FileActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }
}
