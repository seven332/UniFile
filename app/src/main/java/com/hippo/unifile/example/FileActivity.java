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
 * Created by Hippo on 11/20/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hippo.unifile.FilenameFilter;
import com.hippo.unifile.UniFile;
import com.hippo.unifile.UniRandomAccessFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileActivity extends Activity {

    private Uri mUri;
    private UniFile mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mUri = intent.getData();
        if (mUri == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mFile = UniFile.fromUri(this, mUri);
        if (mFile == null) {
            Log.w("TAG", "Can't recognize the uri: " + mUri);
            Toast.makeText(this, "Can't recognize the uri", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_file);

        findViewById(R.id.refresh_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshDetail();
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        findViewById(R.id.list_files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFiles();
            }
        });
        findViewById(R.id.list_files_filename_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFilesFilenameFilter();
            }
        });
        findViewById(R.id.find_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFile();
            }
        });
        findViewById(R.id.create_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFile();
            }
        });
        findViewById(R.id.create_directory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDirectory();
            }
        });
        findViewById(R.id.open_output_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOutputStream();
            }
        });
        findViewById(R.id.open_input_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInputStream();
            }
        });
        findViewById(R.id.create_random_access_file_r).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRandomAccessFileR();
            }
        });
        findViewById(R.id.create_random_access_file_rw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRandomAccessFileRW();
            }
        });

        refreshDetail();
    }

    private void refreshDetail() {
        TextView detail = (TextView) findViewById(R.id.detail);
        StringBuilder sb = new StringBuilder();
        sb.append("Uri: ").append(mUri.toString()).append("\n");
        sb.append("Class: ").append(mFile.getClass().getName()).append("\n");
        sb.append("getUri(): ").append(mFile.getUri()).append("\n");
        sb.append("getName(): ").append(mFile.getName()).append("\n");
        sb.append("getType(): ").append(mFile.getType()).append("\n");
        sb.append("getFilePath(): ").append(mFile.getFilePath()).append("\n");
        sb.append("isDirectory(): ").append(mFile.isDirectory()).append("\n");
        sb.append("isFile(): ").append(mFile.isFile()).append("\n");
        sb.append("lastModified(): ").append(mFile.lastModified()).append("\n");
        sb.append("length(): ").append(mFile.length()).append("\n");
        sb.append("canRead(): ").append(mFile.canRead()).append("\n");
        sb.append("canWrite(): ").append(mFile.canWrite()).append("\n");
        sb.append("exists(): ").append(mFile.exists()).append("\n");
        detail.setText(sb.toString());
    }

    private void delete() {
        ((TextView) findViewById(R.id.delete_result)).setText("" + mFile.delete());
    }

    private void listFiles() {
        UniFile[] files = mFile.listFiles();
        if (files == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } else if (files.length == 0) {
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Uri> uris = new ArrayList<>();
            for (UniFile f : files) {
                uris.add(f.getUri());
            }
            Intent intent = new Intent(this, FileListActivity.class);
            intent.putParcelableArrayListExtra(FileListActivity.KEY_URIS, uris);
            startActivity(intent);
        }
    }

    private void listFilesFilenameFilter() {
        UniFile[] files = mFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(UniFile dir, String filename) {
                return filename.startsWith("a");
            }
        });
        if (files == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        } else if (files.length == 0) {
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Uri> uris = new ArrayList<>();
            for (UniFile f : files) {
                uris.add(f.getUri());
            }
            Intent intent = new Intent(this, FileListActivity.class);
            intent.putParcelableArrayListExtra(FileListActivity.KEY_URIS, uris);
            startActivity(intent);
        }
    }

    private void findFile() {
        String filename = ((EditText) findViewById(R.id.find_file_param)).getText().toString();
        UniFile file = mFile.findFile(filename);
        if (file != null) {
            Intent intent = new Intent(this, FileActivity.class);
            intent.setData(file.getUri());
            startActivity(intent);
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFile() {
        String filename = ((EditText) findViewById(R.id.create_file_param)).getText().toString();
        UniFile file = mFile.createFile(filename);
        if (file != null) {
            Intent intent = new Intent(this, FileActivity.class);
            intent.setData(file.getUri());
            startActivity(intent);
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void createDirectory() {
        String filename = ((EditText) findViewById(R.id.create_directory_param)).getText().toString();
        UniFile file = mFile.createDirectory(filename);
        if (file != null) {
            Intent intent = new Intent(this, FileActivity.class);
            intent.setData(file.getUri());
            startActivity(intent);
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void openOutputStream() {
        boolean result;
        OutputStream os = null;
        try {
            os = mFile.openOutputStream();
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ((TextView) findViewById(R.id.open_output_stream_result)).setText("" + result);
    }

    private void openInputStream() {
        boolean result;
        InputStream is = null;
        try {
            is = mFile.openInputStream();
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ((TextView) findViewById(R.id.open_input_stream_result)).setText("" + result);
    }

    private void createRandomAccessFileR() {
        boolean result;
        UniRandomAccessFile uraf = null;
        try {
            uraf = mFile.createRandomAccessFile("r");
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            if (uraf != null) {
                try {
                    uraf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ((TextView) findViewById(R.id.create_random_access_file_r_result)).setText("" + result);
    }

    private void createRandomAccessFileRW() {
        boolean result;
        UniRandomAccessFile uraf = null;
        try {
            uraf = mFile.createRandomAccessFile("rw");
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            if (uraf != null) {
                try {
                    uraf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ((TextView) findViewById(R.id.create_random_access_file_rw_result)).setText("" + result);
    }
}
