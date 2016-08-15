package com.hippo.unifile.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.hippo.unifile.UniFile;
import com.hippo.unifile.UriTestCreator;

import java.io.IOException;
import java.io.RandomAccessFile;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE_DOCUMENT = 0;
    public static final int REQUEST_CODE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            try {
                startActivityForResult(intent, REQUEST_CODE_DOCUMENT);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "error_cant_find_activity", Toast.LENGTH_SHORT).show();
            }
        }
        */



        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_DOCUMENT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Uri treeUri = data.getData();
                UniFile dir = UniFile.fromTreeUri(this, treeUri);
                UniFile file = dir.createFile("aaa.txt");


                Uri uri = file.getUri();
                Log.i(TAG, "Start test: " + uri.toString());
                test(new UriTestCreator(this, uri));
                Log.i(TAG, "End test");
            }
        } else if (requestCode == REQUEST_CODE_SELECT) {
            Uri uri = data.getData();
            Log.i(TAG, "Start test: " + uri.toString());
            test(new UriTestCreator(this, uri));
            Log.i(TAG, "End test");
        }
    }

    public void test(TestCreator creator) {
        try {
            RandomAccessFile raf;

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_close(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_getChannel(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_getFD(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_getFilePointer(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_length(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_write(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read$B(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read$BII(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeBoolean(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeByte(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeChar(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeDouble(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeFloat(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeInt(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeLong(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeShort(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_read_writeUTF(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_readFully$B_writeBytesLjava_lang_String(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_readFully$BII(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_readUnsignedByte(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_readUnsignedShort(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_readLine(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_seekJ(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_skipBytesI(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_setLengthJ(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_write$B(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_write$BII(creator);

            raf = creator.createRandomAccessFile("rw");
            raf.setLength(0);
            raf.close();
            RandomAccessFileTest.test_writeCharsLjava_lang_String(creator);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
