package com.moses.io;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moses.io.mediaplayer.PagerPlayerActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by 丹 on 2014/12/17.
 * <p/>
 * 这里直接使用前面ListFilesActivity中已经写好的适配器
 */
public class SearchFileActivity extends Activity {
    private static final int SEARCH_START = 0;
    private static final int SEARCH_DONE = 1;
    EditText mSearchTxt;
    Button mSearchBtn, mPathBtn;
    ListView mFileLV;
    List<File> files = new ArrayList<>();
    SearchAdapter adapter;
    String inputStr;


    List<File> myFiles = new ArrayList<>();
    List<File> bundleFiles = new ArrayList<>();
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_START:
                    //这里刷新数据，而不在搜索文件的方法中刷新
                    String path = (String) msg.obj;
                    mSearchTxt.setText(path);
                    break;
                case SEARCH_DONE:
                    adapter.setData(files);
                    mSearchTxt.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_file);

        Collections.addAll(files, new File("/storage").listFiles());    //得到files
        intiView();

    }

    /**
     * 初始化控件
     * 设定事件
     */
    public void intiView() {
        mSearchTxt = (EditText) findViewById(R.id.file_search_txt);
        mSearchBtn = (Button) findViewById(R.id.file_search_button);
        mFileLV = (ListView) findViewById(R.id.list_view_search_file);
        mPathBtn = (Button) findViewById(R.id.path_button);

        adapter = new SearchAdapter(SearchFileActivity.this);
        mFileLV.setAdapter(adapter);
        adapter.setData(files);

        mPathBtn.setText(new File("/storage").getPath());

        /**
         * 通过mPathBtn上的路径文本判断点击操作应该获得的新的路径
         */
        mPathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mPathBtn.getText().toString();
                if (path.equals("/storage")) {
                    files.clear();
                    Collections.addAll(files, new File("/storage").listFiles());
                    adapter.setData(files);
                    mPathBtn.setText("/storage");
                } else if (!path.equals("文件路径:") && !path.equals("/storage")) {
                    String parent = new File(path).getParent();
                    files.clear();
                    Collections.addAll(files, new File(parent).listFiles());
                    adapter.setData(files);
                    mPathBtn.setText(parent);
                }
            }
        });

        /**
         * 通过mSearchTxt上的关键字，mSearchBtn相应搜索的文件类型
         */
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = mPathBtn.getText().toString();
                inputStr = mSearchTxt.getText().toString();

                if (inputStr.equals("请输入要查找的文件后缀") || inputStr.equals("")) {
                    Toast.makeText(SearchFileActivity.this, "无效输入!", Toast.LENGTH_SHORT).show();
                } else {
                    mSearchTxt.setText("searching files for you...");
                    new SearchThread(filePath).start();

                }
            }

        });

        /**
         * 判断mFileLV 元素类型，如果item是文件夹，则点击打开文件夹，显示新的列表
         *    如果是文件，则调用相关方法打开文件或跳转到其他页面，或执行其他操作
         */
        mFileLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File theFile = files.get(position);
                if (theFile.isDirectory()) {
                    files.clear();
                    Collections.addAll(files, theFile.listFiles());
                    adapter.setData(files);
                    mPathBtn.setText(theFile.getAbsolutePath());
                } else if (theFile.getName().endsWith(".mp3")) {     //播放mp3
                    /**
                     * 得到当前所有音乐文件，发送到下一个播放音乐的Activity
                     *     思路：  只要输入了.mp3,则打包当前目录下的所有.mp3文件(路径，名称)
                     */
                    bundleFiles.clear();
                    for (File file : files) {
                        if (file.getName().endsWith(".mp3")) {
                            Collections.addAll(bundleFiles, file);
                        }
                    }

                    /**
                     * 这里是需要的路径和名称
                     */
                    String[] pathStr = new String[bundleFiles.size()];
                    String[] nameStr = new String[bundleFiles.size()];
                    for (int i = 0; i < bundleFiles.size(); i++) {
                        pathStr[i] = bundleFiles.get(i).getAbsolutePath();
                        nameStr[i] = bundleFiles.get(i).getName();
                    }
                    //-------------------------启动Activity播放音乐--------------
                    Intent intent = new Intent(SearchFileActivity.this, PagerPlayerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("path", pathStr);
                    bundle.putStringArray("name", nameStr);
                    bundle.putInt("theMusic",files.indexOf(theFile));
                    intent.putExtra("mediaFile", bundle);
                    startActivity(intent);


                    //-------------------------启动Service播放音乐------------
//                    Intent intent = new Intent(SearchFileActivity.this, MusicUI.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArray("path", pathStr);
//                    bundle.putStringArray("name", nameStr);
//                    intent.putExtra("mediaFile", bundle);
//                    startActivity(intent);

                    //------------------------AIDL 通讯-----------------------------
//                    Intent intent = new Intent(SearchFileActivity.this, AidlMusicActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putStringArray("path", pathStr);
//                    bundle.putStringArray("name", nameStr);
//                    intent.putExtra("mediaFile", bundle);
//                    startActivity(intent);

                }
            }
        });

    }

    /**
     * 这里shi适配器
     * 这里我直接使用了ListFilesActivity中适配器item的view布局
     */
    public class SearchAdapter extends BaseAdapter {
        List<File> mData = new ArrayList<>();
        LayoutInflater inflater;

        public SearchAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<File> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Mp3ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_file_list, null);
                viewHolder = new Mp3ViewHolder();
                viewHolder.mp3Img = (ImageView) convertView.findViewById(R.id.fileIcon);
                viewHolder.mp3Name = (TextView) convertView.findViewById(R.id.fileName);
                viewHolder.mp3Size = (TextView) convertView.findViewById(R.id.fileCount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Mp3ViewHolder) convertView.getTag();
            }
            if (mData.get(position).isFile()) {
                viewHolder.mp3Size.setText(sizeFile(mData.get(position)));
                if (mData.get(position).getName().endsWith(".mp3")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_music);
                } else if (mData.get(position).getName().endsWith(".pdf")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_pdf);
                } else if (mData.get(position).getName().endsWith(".txt")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_txt);
                } else if (mData.get(position).getName().endsWith(".ppt")) {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_ppt);
                } else if (mData.get(position).getName().endsWith(".jpg") || mData.get(position).getName().endsWith(".png")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position).getPath());
                    viewHolder.mp3Img.setImageBitmap(bitmap);
                } else {
                    viewHolder.mp3Img.setImageResource(R.drawable.icon_help);
                }

            } else {
                viewHolder.mp3Img.setImageResource(R.drawable.documents_folder);
                viewHolder.mp3Size.setText(countFiles(mData.get(position)) + "个文件");
            }

            viewHolder.mp3Name.setText(mData.get(position).getName());
            return convertView;
        }

        class Mp3ViewHolder {
            ImageView mp3Img;
            TextView mp3Name;
            TextView mp3Size;
        }
    }

    /**
     * 封装1个方法，计算文件大小
     */
    public String sizeFile(File file) {
        String str = null;
        if (file.isFile()) {
            Long byteLen = file.length();
            float kBLen = byteLen / 1024;
            float mBLen = kBLen / 1024;
            if (kBLen < 1) {
                str = String.valueOf(byteLen) + "B";
            } else if (mBLen < 1) {
                str = String.valueOf(kBLen) + "KB";
            } else {
                String strMath = new DecimalFormat("#.00").format(mBLen);
                str = strMath + "MB";
            }
        }
        return str;
    }

    /**
     * 封装1个方法，计算文件夹里的文件个数
     */
    public String countFiles(File file) {
        String str = "";
        int i;
        if (file.isDirectory()) {
            i = file.listFiles().length;
            return str + String.valueOf(i);
        }
        return str;
    }

    /**
     * 递归列出所有文件
     *
     * @param theFileDirPath
     */
    public void listAllFile(File theFileDirPath) {
        if (theFileDirPath.isDirectory()) {
            for (File file : theFileDirPath.listFiles()) {
                if (file.isFile()) {
                    myFiles.add(file);

                    //handler message
                    /**
                     * 重复产生Message对象，会造成手机卡死现象
                     *   尤其在文件很多的情况下，比如以真是的手机作为测试的设备时
                     *     当使用Message.obtain()也有同样故障。
                     *     【******有待进一步学习********】
                     */
//                    Message msg = Message.obtain();
//                    msg.what = SEARCH_START;
//                    msg.obj = file.getPath();
//                    mHandler.sendMessage(msg);

                } else {
                    listAllFile(file);
                }
            }
        }
    }


    /**
     * 封装1个方法，搜索文件， 得到list数据源
     * 要求没搜索得到一个适当的文件，就要传递对应的指导空间上显示
     * <p/>
     * 使用Handler  发送数据
     */
    public void searchFiles(String theFileDirPath) {

        listAllFile(new File(theFileDirPath));

        files.clear();
        for (File file : myFiles) {
            if (file.getName().endsWith(inputStr)) {
                files.add(file);

            }
        }
        //这里不再刷新数据，而是改为在mHandler中刷新

    }

    /**
     * 因为搜索耗时，创建一个搜索文件的线程类  ，用来操作文件搜索
     */
    class SearchThread extends Thread {
        //重写run方法    因为是搜索文件，u所以需要在构造中传入一个（文件夹）路径
        String filePath;

        public SearchThread(String filePath) {
            this.filePath = filePath;
        }

        /**
         * Handler Message Thread Looper
         * 这里是用Handler + Message
         * 子线程中发送消息，   主线程（UI）h线程中接收消息
         */
        @Override
        public void run() {
            searchFiles(filePath);
            mHandler.sendEmptyMessage(SEARCH_DONE);
        }

    }


}
