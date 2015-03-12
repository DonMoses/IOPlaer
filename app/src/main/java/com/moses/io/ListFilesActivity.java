package com.moses.io;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListFilesActivity extends Activity {
    Button mButton;
    ListView mListView;
    List<File> myFiles = new ArrayList<>();
    List<FileIconHelper> theData;
    LayoutInflater inflater;
    ListFileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);
        inflater = LayoutInflater.from(this);

        intiViews();

    }

    /**
     * i初始化控件
     */
    public void intiViews() {
        mButton = (Button) findViewById(R.id.file_list_btn);
        mListView = (ListView) findViewById(R.id.list_view_file);
        /**
         * 点击Button， 列出文件目录
         */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFiles = new ArrayList<>();
                String str = mButton.getText().toString();
                /**
                 * 通过button上指示的当前位置，判断button应有的点击事件
                 *   这里，我规定了除了第一次进入，其它点击按钮时回到上一目录
                 */
                if (isExternalAvailable()) {
                    if (str.equals("获取文件夹/文件目录")||str.equals("/storage")) {
                        listMyFiles(new File("/storage").getAbsolutePath());
                        mButton.setText("/storage");
                    }else {
                        String strPa = new File(str).getParent();
                        listMyFiles(strPa);
                        mButton.setText(strPa);
                    }

                } else {
                    Toast.makeText(ListFilesActivity.this, "外部空间不可用", Toast.LENGTH_SHORT).show();
                }
                adapter = new ListFileAdapter(myFiles);
                mListView.setAdapter(adapter);

                theData = setFiles(myFiles);

            }
        });
        /**
         * listView点击事件，  进入文件夹/（Intent的action调用）打开文件
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File i_file = myFiles.get(position);
                /**
                 * 如果i_file是文件夹，则继续递归编列，  并将结果重新设置到adapter
                 */
                if (i_file.isDirectory()) {
                    myFiles.clear();
                    listMyFiles(i_file.getAbsolutePath());
                    mButton.setText(i_file.getAbsolutePath());
                    adapter.setData(setFiles(myFiles));
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                }
            }
        });

    }

    public boolean isExternalAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 封装一个方法，用来list 一个目录
     */

    public boolean listMyFiles(String filePath) {

        File file = new File(filePath);
        if (file.isFile()) {
            Log.i("TAG", "打开文件");
        } else {
            File[] files = file.listFiles();
            Collections.addAll(myFiles, files);
        }
        return true;
    }

    /**
     * 从获得的普通File对象创建新的自定义File类对象
     */
    public List<FileIconHelper> setFiles(List<File> listFiles) {
        List<FileIconHelper> theFiles = new ArrayList<>();

        for (int i = 0; i < listFiles.size(); i++) {
            FileIconHelper theFile = new FileIconHelper(listFiles.get(i).getPath());
            if (theFile.isDirectory()) {
                theFile.setFileIcon(R.drawable.documents_folder);
            } else if (theFile.getName().endsWith(".txt")) {
                theFile.setFileIcon(R.drawable.icon_txt);
            } else if (theFile.getName().endsWith(".pdf")) {
                theFile.setFileIcon(R.drawable.icon_pdf);
            } else if (theFile.getName().endsWith(".ogg")
                    || theFile.getName().endsWith(".mp3")
                    || theFile.getName().endsWith(".ape")
                    || theFile.getName().endsWith(".flac")
                    || theFile.getName().endsWith(".wv")) {
                theFile.setFileIcon(R.drawable.icon_music);
            } else theFile.setFileIcon(R.drawable.icon_help);

            theFiles.add(theFile);
        }

        return theFiles;
    }

    /**
     * 自定义Adapter 作为ListView的数据适配器
     */

    public class ListFileAdapter extends BaseAdapter {
        List<FileIconHelper> mData = new ArrayList<>();

        public ListFileAdapter(List<File> listFiles) {
            mData = setFiles(listFiles);
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

        /**
         * 刷新数据
         * 理解为属性 de设置
         */
        public void setData(List<FileIconHelper> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_file_list, null);
                viewHolder = new ViewHolder();
                viewHolder.fileIcon = (ImageView) convertView.findViewById(R.id.fileIcon);
                viewHolder.fileCount = (TextView) convertView.findViewById(R.id.fileCount);
                viewHolder.fileName = (TextView) convertView.findViewById(R.id.fileName);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            /**
             * 判断文件类型为图片，则icon设置为该图片
             * 如果不是图片，则icon设置为通过getFileIcon()获取的数据源中对应的icon
             */
            if (mData.get(position).getName().endsWith(".jpg") || mData.get(position).getName().endsWith(".png")) {
                Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position).getPath());
                viewHolder.fileIcon.setImageBitmap(bitmap);
            } else {
                viewHolder.fileIcon.setImageResource(mData.get(position).getFileIcon());
            }
            /**
             * 判断大小或子目录  如果是文件夹则显示子目录数量， 如果是文件则显示文件大小
             */
            if (mData.get(position).isDirectory()) {
                viewHolder.fileCount.setText(countFiles(mData.get(position)) + "个文件");
            } else {
                viewHolder.fileCount.setText(sizeFile(mData.get(position)));
            }

            viewHolder.fileName.setText(mData.get(position).getName());
            return convertView;
        }

        class ViewHolder {
            ImageView fileIcon;
            TextView fileCount;
            TextView fileName;
        }
    }


    /**
     * 封装一个文件类， 用来设置文件夹/文件的icon
     */

    class FileIconHelper extends File {
        private int fileIcon;

        public FileIconHelper(String path) {
            super(path);
        }

        public void setFileIcon(int fileIcon) {
            this.fileIcon = fileIcon;
        }

        public int getFileIcon() {
            return fileIcon;
        }

    }

    /**
     * 封装1个方法，计算文件夹里的文件个数
     */
    public String countFiles(FileIconHelper file) {
        String str = "";
        int i;
        if (file.isDirectory()) {
            i = file.listFiles().length;
            return str + String.valueOf(i);
        }
        return str;
    }

    /**
     * 封装1个方法，计算文件大小
     */
    public String sizeFile(FileIconHelper file) {
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

}

