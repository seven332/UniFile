/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.unifile.example;

/*
 * Created by Hippo on 11/25/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hippo.unifile.UniFile;

import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends Activity {

    public static final String KEY_URIS = "uris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ArrayList<Uri> uris = intent.getParcelableArrayListExtra(KEY_URIS);
        if (uris == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final List<UniFile> files = new ArrayList<>();
        for (Uri uri : uris) {
            UniFile f = UniFile.fromUri(this, uri);
            if (f == null) {
                throw new IllegalStateException("Can't convert Uri to UniFile: " + uri);
            }
            files.add(f);
        }

        setContentView(R.layout.activity_file_list);

        ListView listView = (ListView) findViewById(R.id.file_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return files.size();
            }

            @Override
            public Object getItem(int i) {
                return files.get(i);
            }

            @Override
            public long getItemId(int i) {
                return files.get(i).hashCode();
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, viewGroup, false);
                }
                ((TextView) ((ViewGroup) view).getChildAt(0)).setText(files.get(i).getName());
                ((TextView) ((ViewGroup) view).getChildAt(1)).setText(files.get(i).getUri().toString());
                return view;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FileListActivity.this, FileActivity.class);
                intent.setData(files.get(i).getUri());
                startActivity(intent);
            }
        });
    }
}
