package com.hippo.unifile.example;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hippo.unifile.UniFile;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class MainActivity extends Activity {

    String unihw = "\u0048\u0065\u006C\u0801\u006C\u006F\u0020\u0057\u0081\u006F\u0072\u006C\u0064";

    static final String testString = "Lorem ipsum dolor sit amet,\n" +
            "consectetur adipisicing elit,\nsed do eiusmod tempor incididunt ut" +
            "labore et dolore magna aliqua.\n";
    static final int testLength = testString.length();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            try {
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "error_cant_find_activity", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Uri treeUri = data.getData();

            Log.d("TAG", treeUri.toString());

            UniFile dir = UniFile.fromTreeUri(this, treeUri);
            UniFile file = dir.createFile("aaa.txt");

            try {
                file.openOutputStream().close();
                test_close(file);

                file.openOutputStream().close();
                test_getChannel(file);

                file.openOutputStream().close();
                test_getFD(file);

                file.openOutputStream().close();
                test_getFilePointer(file);

                file.openOutputStream().close();
                test_length(file);

                file.openOutputStream().close();
                test_read_write(file);

                file.openOutputStream().close();
                test_read$B(file);

                file.openOutputStream().close();
                test_read$BII(file);

                file.openOutputStream().close();
                test_read_writeBoolean(file);

                file.openOutputStream().close();
                test_read_writeByte(file);

                file.openOutputStream().close();
                test_read_writeChar(file);

                file.openOutputStream().close();
                test_read_writeDouble(file);

                file.openOutputStream().close();
                test_read_writeFloat(file);

                file.openOutputStream().close();
                test_read_writeInt(file);

                file.openOutputStream().close();
                test_read_writeLong(file);

                file.openOutputStream().close();
                test_read_writeShort(file);

                file.openOutputStream().close();
                test_read_writeUTF(file);

                file.openOutputStream().close();
                test_readFully$B_writeBytesLjava_lang_String(file);

                file.openOutputStream().close();
                test_readFully$BII(file);

                file.openOutputStream().close();
                test_readUnsignedByte(file);

                file.openOutputStream().close();
                test_readUnsignedShort(file);

                file.openOutputStream().close();
                test_readLine(file);

                file.openOutputStream().close();
                test_seekJ(file);

                file.openOutputStream().close();
                test_skipBytesI(file);

                file.openOutputStream().close();
                test_setLengthJ(file);

                file.openOutputStream().close();
                test_write$B(file);

                file.openOutputStream().close();
                test_write$BII(file);

                file.openOutputStream().close();
                test_writeCharsLjava_lang_String(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * java.io.RandomAccessFile#close()
     */
    public void test_close(UniFile file) {
        // Test for method void java.io.RandomAccessFile.close()
        try {
            RandomAccessFile raf = file.createRandomAccessFile("rw");
            raf.close();
            raf.write("Test".getBytes(), 0, 4);
            fail("Failed to close file properly.");
        } catch (IOException e) {}
    }

    /**
     * java.io.RandomAccessFile#getChannel()
     */
    public void test_getChannel(UniFile file) throws IOException {

        RandomAccessFile raf = file.createRandomAccessFile("rw");
        FileChannel fc = raf.getChannel();

        // Indirect test: If the file's file pointer moves then the position
        // in the channel has to move accordingly.
        assertTrue("Test 1: Channel position expected to be 0.", fc.position() == 0);

        raf.write(testString.getBytes());
        assertEquals("Test 2: Unexpected channel position.",
                testLength, fc.position());
        assertTrue("Test 3: Channel position is not equal to file pointer.",
                fc.position() == raf.getFilePointer());
        raf.close();
    }

    /**
     * java.io.RandomAccessFile#getFD()
     */
    public void test_getFD(UniFile file) throws IOException {
        // Test for method java.io.FileDescriptor
        // java.io.RandomAccessFile.getFD()

        RandomAccessFile raf = file.createRandomAccessFile("rw");
        assertTrue("Test 1: Returned invalid fd.", raf.getFD().valid());

        raf.close();
        assertFalse("Test 2: Returned valid fd after close", raf.getFD().valid());
    }

    /**
     * java.io.RandomAccessFile#getFilePointer()
     */
    public void test_getFilePointer(UniFile file) throws IOException {
        // Test for method long java.io.RandomAccessFile.getFilePointer()
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.write(testString.getBytes(), 0, testLength);
        assertEquals("Test 1: Incorrect filePointer returned. ", testLength, raf
                .getFilePointer());
        raf.close();
        try {
            raf.getFilePointer();
            fail("Test 2: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#length()
     */
    public void test_length(UniFile file) throws IOException {
        // Test for method long java.io.RandomAccessFile.length()
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.write(testString.getBytes());
        assertEquals("Test 1: Incorrect length returned. ", testLength,
                raf.length());
        raf.close();
        try {
            raf.length();
            fail("Test 2: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#read()
     */
    public void test_read_write(UniFile file) throws IOException {
        int i;
        byte[] testBuf = testString.getBytes();
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        for (i = 0; i < testString.length(); i++) {
            try {
                raf.write(testBuf[i]);
            } catch (Exception e) {
                fail("Test 1: Unexpected exception while writing: "
                        + e.getMessage());
            }
        }

        raf.seek(0);

        for (i = 0; i < testString.length(); i++) {
            assertEquals(String.format("Test 2: Incorrect value written or read at index %d; ", i),
                    testBuf[i], raf.read());
        }

        assertTrue("Test 3: End of file indicator (-1) expected.", raf.read() == -1);

        raf.close();
        try {
            raf.write(42);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.read();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#read(byte[])
     */
    public void test_read$B(UniFile file) throws IOException {
        OutputStream fos = file.openOutputStream();
        fos.write(testString.getBytes(), 0, testLength);
        fos.close();

        RandomAccessFile raf = file.createRandomAccessFile("r");
        byte[] rbuf = new byte[testLength + 10];

        int bytesRead = raf.read(rbuf);
        assertEquals("Test 1: Incorrect number of bytes read. ",
                testLength, bytesRead);
        assertEquals("Test 2: Incorrect bytes read. ", testString,
                new String(rbuf, 0, testLength));

        bytesRead = raf.read(rbuf);
        assertTrue("Test 3: EOF (-1) expected. ", bytesRead == -1);

        raf.close();
        try {
            bytesRead = raf.read(rbuf);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#read(byte[], int, int)
     */
    public void test_read$BII(UniFile file) throws IOException {
        int bytesRead;
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        byte[] rbuf = new byte[4000];

        OutputStream fos = file.openOutputStream();
        fos.write(testString.getBytes(), 0, testLength);
        fos.close();

        // Read half of the file contents.
        bytesRead = raf.read(rbuf, 10, testLength / 2);
        assertEquals("Test 1: Incorrect number of bytes read. ",
                testLength / 2, bytesRead);
        assertEquals("Test 2: Incorrect bytes read. ",
                testString.substring(0, testLength / 2),
                new String(rbuf, 10, testLength / 2));

        // Read the rest of the file contents.
        bytesRead = raf.read(rbuf, 0, testLength);
        assertEquals("Test 3: Incorrect number of bytes read. ",
                testLength - (testLength / 2), bytesRead);
        assertEquals("Test 4: Incorrect bytes read. ",
                testString.substring(testLength / 2, (testLength / 2) + bytesRead),
                new String(rbuf, 0, bytesRead));

        // Try to read even more.
        bytesRead = raf.read(rbuf, 0, 1);
        assertTrue("Test 5: EOF (-1) expected. ", bytesRead == -1);

        // Illegal parameter value tests.
        try {
            raf.read(rbuf, -1, 1);
            fail("Test 6: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }
        try {
            raf.read(rbuf, 0, -1);
            fail("Test 7: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }
        try {
            raf.read(rbuf, 2000, 2001);
            fail("Test 8: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }

        // IOException test.
        raf.close();
        try {
            bytesRead = raf.read(rbuf, 0, 1);
            fail("Test 9: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readBoolean()
     * java.io.RandomAccessFile#writeBoolean(boolean)
     */
    public void test_read_writeBoolean(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeBoolean(true);
        raf.writeBoolean(false);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                true, raf.readBoolean());
        assertEquals("Test 2: Incorrect value written or read;",
                false, raf.readBoolean());

        try {
            raf.readBoolean();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeBoolean(false);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readBoolean();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readByte()
     * java.io.RandomAccessFile#writeByte(byte)
     */
    public void test_read_writeByte(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeByte(Byte.MIN_VALUE);
        raf.writeByte(11);
        raf.writeByte(Byte.MAX_VALUE);
        raf.writeByte(Byte.MIN_VALUE - 1);
        raf.writeByte(Byte.MAX_VALUE + 1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                Byte.MIN_VALUE, raf.readByte());
        assertEquals("Test 2: Incorrect value written or read;",
                11, raf.readByte());
        assertEquals("Test 3: Incorrect value written or read;",
                Byte.MAX_VALUE, raf.readByte());
        assertEquals("Test 4: Incorrect value written or read;",
                127, raf.readByte());
        assertEquals("Test 5: Incorrect value written or read;",
                -128, raf.readByte());

        try {
            raf.readByte();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeByte(13);
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readByte();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readChar()
     * java.io.RandomAccessFile#writeChar(char)
     */
    public void test_read_writeChar(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeChar(Character.MIN_VALUE);
        raf.writeChar('T');
        raf.writeChar(Character.MAX_VALUE);
        raf.writeChar(Character.MIN_VALUE - 1);
        raf.writeChar(Character.MAX_VALUE + 1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                Character.MIN_VALUE, raf.readChar());
        assertEquals("Test 2: Incorrect value written or read;",
                'T', raf.readChar());
        assertEquals("Test 3: Incorrect value written or read;",
                Character.MAX_VALUE, raf.readChar());
        assertEquals("Test 4: Incorrect value written or read;",
                0xffff, raf.readChar());
        assertEquals("Test 5: Incorrect value written or read;",
                0, raf.readChar());

        try {
            raf.readChar();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeChar('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readChar();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readDouble()
     * java.io.RandomAccessFile#writeDouble(double)
     */
    public void test_read_writeDouble(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeDouble(Double.MAX_VALUE);
        raf.writeDouble(424242.4242);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                Double.MAX_VALUE, raf.readDouble());
        assertEquals("Test 2: Incorrect value written or read;",
                424242.4242, raf.readDouble());

        try {
            raf.readDouble();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeDouble(Double.MIN_VALUE);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readDouble();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readFloat()
     * java.io.RandomAccessFile#writeFloat(double)
     */
    public void test_read_writeFloat(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeFloat(Float.MAX_VALUE);
        raf.writeFloat(555.55f);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read. ",
                Float.MAX_VALUE, raf.readFloat());
        assertEquals("Test 2: Incorrect value written or read. ",
                555.55f, raf.readFloat());

        try {
            raf.readFloat();
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeFloat(Float.MIN_VALUE);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readFloat();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readInt()
     * java.io.RandomAccessFile#writeInt(char)
     */
    public void test_read_writeInt(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeInt(Integer.MIN_VALUE);
        raf.writeInt('T');
        raf.writeInt(Integer.MAX_VALUE);
        raf.writeInt(Integer.MIN_VALUE - 1);
        raf.writeInt(Integer.MAX_VALUE + 1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                Integer.MIN_VALUE, raf.readInt());
        assertEquals("Test 2: Incorrect value written or read;",
                'T', raf.readInt());
        assertEquals("Test 3: Incorrect value written or read;",
                Integer.MAX_VALUE, raf.readInt());
        assertEquals("Test 4: Incorrect value written or read;",
                0x7fffffff, raf.readInt());
        assertEquals("Test 5: Incorrect value written or read;",
                0x80000000, raf.readInt());

        try {
            raf.readInt();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeInt('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readInt();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readLong()
     * java.io.RandomAccessFile#writeLong(char)
     */
    public void test_read_writeLong(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeLong(Long.MIN_VALUE);
        raf.writeLong('T');
        raf.writeLong(Long.MAX_VALUE);
        raf.writeLong(Long.MIN_VALUE - 1);
        raf.writeLong(Long.MAX_VALUE + 1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                Long.MIN_VALUE, raf.readLong());
        assertEquals("Test 2: Incorrect value written or read;",
                'T', raf.readLong());
        assertEquals("Test 3: Incorrect value written or read;",
                Long.MAX_VALUE, raf.readLong());
        assertEquals("Test 4: Incorrect value written or read;",
                0x7fffffffffffffffl, raf.readLong());
        assertEquals("Test 5: Incorrect value written or read;",
                0x8000000000000000l, raf.readLong());

        try {
            raf.readLong();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeLong('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readLong();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readShort()
     * java.io.RandomAccessFile#writeShort(short)
     */
    public void test_read_writeShort(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeShort(Short.MIN_VALUE);
        raf.writeShort('T');
        raf.writeShort(Short.MAX_VALUE);
        raf.writeShort(Short.MIN_VALUE - 1);
        raf.writeShort(Short.MAX_VALUE + 1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                Short.MIN_VALUE, raf.readShort());
        assertEquals("Test 2: Incorrect value written or read;",
                'T', raf.readShort());
        assertEquals("Test 3: Incorrect value written or read;",
                Short.MAX_VALUE, raf.readShort());
        assertEquals("Test 4: Incorrect value written or read;",
                0x7fff, raf.readShort());
        assertEquals("Test 5: Incorrect value written or read;",
                (short) 0x8000, raf.readShort());

        try {
            raf.readShort();
            fail("Test 6: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeShort('E');
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readShort();
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readUTF()
     * java.io.RandomAccessFile#writeShort(char)
     */
    public void test_read_writeUTF(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeUTF(unihw);
        raf.seek(0);
        assertEquals("Test 1: Incorrect UTF string written or read;",
                unihw, raf.readUTF());

        try {
            raf.readUTF();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeUTF("Already closed.");
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readUTF();
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#writeBytes(java.lang.String)
     * java.io.RandomAccessFile#readFully(byte[])
     */
    public void test_readFully$B_writeBytesLjava_lang_String(UniFile file) throws IOException {
        byte[] buf = new byte[testLength];
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeBytes(testString);
        raf.seek(0);

        try {
            raf.readFully(null);
            fail("Test 1: NullPointerException expected.");
        } catch (NullPointerException e) {
            // Expected.
        }

        raf.readFully(buf);
        assertEquals("Test 2: Incorrect bytes written or read;",
                testString, new String(buf));

        try {
            raf.readFully(buf);
            fail("Test 3: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.writeBytes("Already closed.");
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
        try {
            raf.readFully(buf);
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#writeBytes(java.lang.String)
     * java.io.RandomAccessFile#readFully(byte[], int, int)
     */
    public void test_readFully$BII(UniFile file) throws IOException {
        byte[] buf = new byte[testLength];
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeBytes(testString);
        raf.seek(0);

        try {
            raf.readFully(null);
            fail("Test 1: NullPointerException expected.");
        } catch (NullPointerException e) {
            // Expected.
        }

        raf.readFully(buf, 5, testLength - 10);
        for (int i = 0; i < 5; i++) {
            assertEquals("Test 2: Incorrect bytes read;", 0, buf[i]);
        }
        assertEquals("Test 3: Incorrect bytes written or read;",
                testString.substring(0, testLength - 10),
                new String(buf, 5, testLength - 10));

        // Reading past the end of the file.
        try {
            raf.readFully(buf, 3, testLength - 6);
            fail("Test 4: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        // Passing invalid arguments.
        try {
            raf.readFully(buf, -1, 1);
            fail("Test 5: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }
        try {
            raf.readFully(buf, 0, -1);
            fail("Test 6: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }
        try {
            raf.readFully(buf, 2, testLength);
            fail("Test 7: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException e) {
            // Expected.
        }

        // Reading from a closed file.
        raf.close();
        try {
            raf.readFully(buf);
            fail("Test 8: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readUnsignedByte()
     */
    public void test_readUnsignedByte(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeByte(-1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                255, raf.readUnsignedByte());

        try {
            raf.readUnsignedByte();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.readUnsignedByte();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readUnsignedShort()
     */
    public void test_readUnsignedShort(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeShort(-1);
        raf.seek(0);

        assertEquals("Test 1: Incorrect value written or read;",
                65535, raf.readUnsignedShort());

        try {
            raf.readUnsignedShort();
            fail("Test 2: EOFException expected.");
        } catch (EOFException e) {
            // Expected.
        }

        raf.close();
        try {
            raf.readUnsignedShort();
            fail("Test 3: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#readLine()
     */
    public void test_readLine(UniFile file) throws IOException {
        // Test for method java.lang.String java.io.RandomAccessFile.readLine()
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        String s = "Goodbye\nCruel\nWorld\n";
        raf.write(s.getBytes(), 0, s.length());
        raf.seek(0);

        assertEquals("Test 1: Incorrect line read;", "Goodbye", raf.readLine());
        assertEquals("Test 2: Incorrect line read;", "Cruel", raf.readLine());
        assertEquals("Test 3: Incorrect line read;", "World", raf.readLine());
        assertNull("Test 4: Incorrect line read; null expected.", raf.readLine());

        raf.close();
        try {
            raf.readLine();
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }

    }

    /**
     * java.io.RandomAccessFile#seek(long)
     */
    public void test_seekJ(UniFile file) throws IOException {
        // Test for method void java.io.RandomAccessFile.seek(long)
        RandomAccessFile raf = file.createRandomAccessFile("rw");

        try {
            raf.seek(-1);
            fail("Test 1: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }

        raf.write(testString.getBytes(), 0, testLength);
        raf.seek(12);
        assertEquals("Test 3: Seek failed to set file pointer.", 12,
                raf.getFilePointer());

        raf.close();
        try {
            raf.seek(1);
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#skipBytes(int)
     */
    public void test_skipBytesI(UniFile file) throws IOException {
        byte[] buf = new byte[5];
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeBytes("HelloWorld");
        raf.seek(0);

        assertTrue("Test 1: Nothing should be skipped if parameter is less than zero",
                raf.skipBytes(-1) == 0);

        assertEquals("Test 4: Incorrect number of bytes skipped; ",
                5, raf.skipBytes(5));

        raf.readFully(buf);
        assertEquals("Test 3: Failed to skip bytes.",
                "World", new String(buf, 0, 5));

        raf.seek(0);
        assertEquals("Test 4: Incorrect number of bytes skipped; ",
                10, raf.skipBytes(20));

        raf.close();
        try {
            raf.skipBytes(1);
            fail("Test 5: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#skipBytes(int)
     */
    public void test_setLengthJ(UniFile file) throws IOException {
        int bytesRead;
        long truncLength = (long) (testLength * 0.75);
        byte[] rbuf = new byte[testLength + 10];

        // Setup the test file.
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.write(testString.getBytes());
        assertEquals("Test 1: Incorrect file length;",
                testLength, raf.length());

        // Truncate the file.
        raf.setLength(truncLength);
        assertTrue("Test 2: File pointer not moved to the end of the truncated file.",
                raf.getFilePointer() == truncLength);

        raf.close();
        raf = file.createRandomAccessFile("rw");
        assertEquals("Test 3: Incorrect file length;",
                truncLength, raf.length());
        bytesRead = raf.read(rbuf);
        assertEquals("Test 4: Incorrect number of bytes read;",
                truncLength, bytesRead);
        assertEquals("Test 5: Incorrect bytes read. ",
                testString.substring(0, bytesRead),
                new String(rbuf, 0, bytesRead));

        // Expand the file.
        raf.setLength(testLength + 2);
        assertTrue("Test 6: File pointer incorrectly moved.",
                raf.getFilePointer() == truncLength);
        assertEquals("Test 7: Incorrect file length;",
                testLength + 2, raf.length());

        // Exception testing.
        try {
            raf.setLength(-1);
            fail("Test 9: IllegalArgumentException expected.");
        } catch (IOException expected) {
        } catch (IllegalArgumentException expected) {
        }

        raf.close();
        try {
            raf.setLength(truncLength);
            fail("Test 10: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }

    /**
     * java.io.RandomAccessFile#write(byte[])
     */
    public void test_write$B(UniFile file) throws IOException {
        byte[] rbuf = new byte[4000];
        RandomAccessFile raf = file.createRandomAccessFile("rw");

        byte[] nullByteArray = null;
        try {
            raf.write(nullByteArray);
            fail("Test 1: NullPointerException expected.");
        } catch (NullPointerException e) {
            // Expected.
        }

        try {
            raf.write(testString.getBytes());
        } catch (Exception e) {
            fail("Test 2: Unexpected exception: " + e.getMessage());
        }

        raf.close();

        try {
            raf.write(new byte[0]);
        } catch (IOException e) {
            fail("Test 3: Unexpected IOException: " + e.getMessage());
        }

        try {
            raf.write(testString.getBytes());
            fail("Test 4: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }

        InputStream fis = file.openInputStream();
        fis.read(rbuf, 0, testLength);
        assertEquals("Incorrect bytes written", testString, new String(rbuf, 0,
                testLength));
    }

    /**
     * java.io.RandomAccessFile#write(byte[], int, int)
     */
    public void test_write$BII(UniFile file) throws Exception {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        byte[] rbuf = new byte[4000];
        byte[] testBuf = null;
        int bytesRead;

        try {
            raf.write(testBuf, 1, 1);
            fail("Test 1: NullPointerException expected.");
        } catch (NullPointerException e) {
            // Expected.
        }

        testBuf = testString.getBytes();

        try {
            raf.write(testBuf, -1, 10);
            fail("Test 2: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            raf.write(testBuf, 0, -1);
            fail("Test 3: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            raf.write(testBuf, 5, testLength);
            fail("Test 4: IndexOutOfBoundsException expected.");
        } catch (IndexOutOfBoundsException expected) {
        }

        // Positive test: The following write should not fail.
        try {
            raf.write(testBuf, 3, testLength - 5);
        } catch (Exception e) {
            fail("Test 5: Unexpected exception: " + e.getMessage());
        }

        raf.close();

        // Writing nothing to a closed file should not fail either.
        try {
            raf.write(new byte[0]);
        } catch (IOException e) {
            fail("Test 6: Unexpected IOException: " + e.getMessage());
        }

        // Writing something to a closed file should fail.
        try {
            raf.write(testString.getBytes());
            fail("Test 7: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }

        InputStream fis = file.openInputStream();
        bytesRead = fis.read(rbuf, 0, testLength);
        assertEquals("Test 8: Incorrect number of bytes written or read;",
                testLength - 5, bytesRead);
        assertEquals("Test 9: Incorrect bytes written or read; ",
                testString.substring(3, testLength - 2),
                new String(rbuf, 0, bytesRead));
    }

    /**
     * java.io.RandomAccessFile#writeChars(java.lang.String)
     */
    public void test_writeCharsLjava_lang_String(UniFile file) throws IOException {
        RandomAccessFile raf = file.createRandomAccessFile("rw");
        raf.writeChars(unihw);
        char[] hchars = new char[unihw.length()];
        unihw.getChars(0, unihw.length(), hchars, 0);
        raf.seek(0);
        for (int i = 0; i < hchars.length; i++)
            assertEquals("Test 1: Incorrect character written or read at index " + i + ";",
                    hchars[i], raf.readChar());
        raf.close();
        try {
            raf.writeChars("Already closed.");
            fail("Test 2: IOException expected.");
        } catch (IOException e) {
            // Expected.
        }
    }
}
