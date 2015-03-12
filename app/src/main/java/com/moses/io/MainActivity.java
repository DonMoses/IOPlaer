package com.moses.io;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener {
    TextView mInfoTxt;
    ImageView mImageView;
    Button mInternalReadBtn, mInternalWriteBtn, mInternalCacheReadBtn, mInternalCacheWriteBtn,
            mInternalBitmapReadBtn, mInternalBitmapWriteBtn, mExternalReadBtn, mExternalWriteBtn,
            mListFilesBtn, mMp3PlayBtn;

    FileInputStream mFIS;
    FileOutputStream mFOS;
    BufferedInputStream mBIS;
    BufferedOutputStream mBOS;

    /**
     * imgGirls   infoTxt
     * <p/>
     * 内部读internalRead    内部写internalWrite    内部缓存读internalCacheRead    内部缓存写internalCacheWrite    内部读图片internalBitmapRead
     * 内部写图片internalBitmapWrite    外部读externalRead     外部写externalWrite
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiViews();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 初始化控件
     */
    public void intiViews() {
        mInfoTxt = (TextView) findViewById(R.id.infoTxt);
        mImageView = (ImageView) findViewById(R.id.imgGirls);

        mInternalReadBtn = (Button) findViewById(R.id.internalRead);
        mInternalWriteBtn = (Button) findViewById(R.id.internalWrite);
        mInternalCacheReadBtn = (Button) findViewById(R.id.internalCacheRead);
        mInternalBitmapReadBtn = (Button) findViewById(R.id.internalBitmapRead);
        mInternalCacheWriteBtn = (Button) findViewById(R.id.internalCacheWrite);
        mInternalBitmapReadBtn = (Button) findViewById(R.id.internalBitmapRead);
        mInternalBitmapWriteBtn = (Button) findViewById(R.id.internalBitmapWrite);
        mExternalReadBtn = (Button) findViewById(R.id.externalRead);
        mExternalWriteBtn = (Button) findViewById(R.id.externalWrite);
        mListFilesBtn = (Button) findViewById(R.id.listFilesTest);
        mMp3PlayBtn = (Button) findViewById(R.id.search_file_btn);

        mInternalReadBtn.setOnClickListener(this);
        mInternalWriteBtn.setOnClickListener(this);
        mInternalCacheReadBtn.setOnClickListener(this);
        mInternalBitmapReadBtn.setOnClickListener(this);
        mInternalCacheWriteBtn.setOnClickListener(this);
        mInternalBitmapReadBtn.setOnClickListener(this);
        mInternalBitmapWriteBtn.setOnClickListener(this);
        mExternalReadBtn.setOnClickListener(this);
        mExternalWriteBtn.setOnClickListener(this);
        mListFilesBtn.setOnClickListener(this);
        mMp3PlayBtn.setOnClickListener(this);

    }


    /**
     * 封装i一个方法，判断外部空间是否可用
     *
     * @param
     */
    public boolean isExternalStorageAvailable() {
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else return false;

    }

    /**
     * 封装一个方法，获得外部公共空间和并读写文件
     *
     * @param
     */
    public File makeExternalStoragePublicFile(File fileDir, String fileName) {
        File theFile = new File(fileDir, fileName);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        if (!theFile.exists()) {
            try {
                theFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return theFile;
    }

    @Override
    public void onClick(View v) {
        File internalFile = new File(getFilesDir(), "moses.txt");
        File internalCacheFile = new File(getCacheDir(), "cache_me.txt");
        byte[] buffer = new byte[1024];
        int len;

        String str = "";
        if (!internalFile.exists()) {
            try {
                internalFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (!internalCacheFile.exists()) {
            try {
                internalCacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        switch (v.getId()) {
            case R.id.internalRead:

                try {
                    mFIS = new FileInputStream(internalFile);
                    mBIS = new BufferedInputStream(mFIS);
                    while ((len = mBIS.read(buffer)) != -1) {
                        str += new String(buffer, 0, len);
                    }
                    mInfoTxt.setText(str);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOCloseHelper.closeBufferedInputStreamB(mBIS);
                    IOCloseHelper.closeFileInputStream(mFIS);
                }

                break;
            case R.id.internalWrite:

                try {
                    mFOS = new FileOutputStream(internalFile);
                    mBOS = new BufferedOutputStream(mFOS);
                    str = "2,本程序使用前请先运行文件夹内的绿化程序,如果你是XP系统,请你确认是用管理员身份登录才可以正常使用,如果你是win7系统或者win8系统,请你右键点击绿化,以管理员身体运行绿化程序.\n" +
                            "亲，能看VIP电影的话，给个好评哦";
                    try {
                        mBOS.write(str.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOCloseHelper.closeBufferedOutputStream(mBOS);
                        IOCloseHelper.closeFileOutputStream(mFOS);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.internalCacheRead:

                try {
                    mFIS = new FileInputStream(internalCacheFile);
                    mBIS = new BufferedInputStream(mFIS);
                    while ((len = mBIS.read(buffer)) != -1) {
                        str += new String(buffer, 0, len);
                    }
                    mInfoTxt.setText("我在缓存目录》》》" + str);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOCloseHelper.closeBufferedInputStreamB(mBIS);
                    IOCloseHelper.closeFileInputStream(mFIS);
                }
                break;
            case R.id.internalCacheWrite:
                try {
                    mFOS = new FileOutputStream(internalFile);
                    mBOS = new BufferedOutputStream(mFOS);
                    str = "从缓存目录中读取文件~ Done";
                    try {
                        mBOS.write(str.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOCloseHelper.closeBufferedOutputStream(mBOS);
                        IOCloseHelper.closeFileOutputStream(mFOS);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.internalBitmapRead:
                String path = getFilesDir() + "/" + "girls.jpg";
                File imgFile = new File(path);
                Bitmap bitmapRead = BitmapFactory.decodeFile(path);

                if (!imgFile.exists()) {
                    Toast.makeText(MainActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        mFIS = new FileInputStream(imgFile);
                        mBIS = new BufferedInputStream(mFIS);
                        try {
                            while ((len = mBIS.read(buffer)) != -1) {
                                str += new String(buffer, 0, len);
                            }
                            mImageView.setImageBitmap(bitmapRead);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        IOCloseHelper.closeBufferedInputStreamB(mBIS);
                        IOCloseHelper.closeFileInputStream(mFIS);
                    }
                }
                break;
            case R.id.internalBitmapWrite:
                File imgFileW = new File(getFilesDir(), "girls.jpg");
                Bitmap bitmapWrite = BitmapFactory.decodeResource(getResources(), R.drawable.girls);
                if (!imgFileW.exists()) {
                    try {
                        imgFileW.createNewFile();
                        mFOS = new FileOutputStream(imgFileW);
                        mBOS = new BufferedOutputStream(mFOS);
                        bitmapWrite.compress(Bitmap.CompressFormat.JPEG, 90, mBOS);
                        mBOS.flush();
                        Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOCloseHelper.closeBufferedOutputStream(mBOS);
                        IOCloseHelper.closeFileOutputStream(mFOS);
                    }
                }

                break;
            case R.id.externalRead:
                if (!isExternalStorageAvailable()) {
                    Toast.makeText(MainActivity.this, "外部空间不可用", Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "privateExternal.txt");
                    if (!file.exists()) {
                        Toast.makeText(MainActivity.this, "指定文件不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            mFIS = new FileInputStream(file);
                            mBIS = new BufferedInputStream(mFIS);
                            try {
                                while ((len = mBIS.read(buffer)) != -1) {
                                    str += new String(buffer, 0, len);
                                }
                                mInfoTxt.setText("这是从外部私有空间读取数据：" + str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            IOCloseHelper.closeBufferedInputStreamB(mBIS);
                            IOCloseHelper.closeFileInputStream(mFIS);
                        }
                    }
                }

                break;
            case R.id.externalWrite:
                if (!isExternalStorageAvailable()) {
                    Toast.makeText(MainActivity.this, "外部空间不可用", Toast.LENGTH_SHORT).show();
                } else {
                    File file = makeExternalStoragePublicFile(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "external.txt");
                    try {
                        mFOS = new FileOutputStream(file);
                        mBOS = new BufferedOutputStream(mFOS);
                        str = "这是写入到外部空间";
                        try {
                            mBOS.write(str.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        IOCloseHelper.closeBufferedOutputStream(mBOS);
                        IOCloseHelper.closeFileOutputStream(mFOS);
                    }

                }
                break;
            case R.id.listFilesTest:
                startActivity(new Intent(MainActivity.this, ListFilesActivity.class));
                break;
            case R.id.search_file_btn:
                startActivity(new Intent(MainActivity.this, SearchFileActivity.class));
            default:
                break;
        }
    }
}

