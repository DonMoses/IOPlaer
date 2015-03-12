package com.moses.io.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moses.io.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 丹 on 2014/12/17.
 */
public class CopyOfPagerPlayerActivity extends Activity {
    ViewPager mViewPager, mSingerPager;
    View mList, mInfo, mPlayer;
    View mSingerPhoto, mSingerInfo;
    LayoutInflater mInflater;
    List<View> mData = new ArrayList<>();
    List<View> mSingerData = new ArrayList<>();
    List<MediaFile> mMedias = new ArrayList<>();
    ImageButton mPrevBtn, mPlayBtn, mNextBtn, mSingleModeBtn, mAllModeBtn, mRandomBtn;
    TextView mCurTxt, mTotTxt;
    SeekBar mSeekBar;
    TextView mWhereTxt;
    MyPagerAdapter mAdapter;
    MediaListAdapter mediaListAdapter;
    SingerPagerAdapter mSingerAdapter;
    String[] paths;
    String[] names;
    ListView mMediaList;
    MediaPlayer mMP = new MediaPlayer();
    int position = 0;
    SharedPreferences mSharedPreferences;
    String totTimeStr;
    String curTimeStr;
    Handler mHandler = new Handler();
    boolean isSingleMode = false;
    boolean isAllMode = true;
    boolean isRandomMode = false;
    private static final int SINGLE_MODE = 0;
    private static final int ALL_MODE = 1;
    private static final int RANDOM_MODE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_player);
        mSharedPreferences = getSharedPreferences("position", MODE_PRIVATE);
        position = mSharedPreferences.getInt("position", 0);

        playMode(ALL_MODE);

        intiViews();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("position", position);
        editor.apply();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMP != null) {

            mMP.release();
            mMP = null;
        }

    }

    public void intiViews() {
        mInflater = LayoutInflater.from(this);

        mMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Mode     need to be finished   效果等同于向下按钮的效果，参考向下按钮的事件监听
                positionUp(position);
                mMP.reset();
                if (isSingleMode) {
                    positionDown(position);
                }
                playWithMode();

            }
        });

        //.....................mViewPager...............................//
        mViewPager = (ViewPager) findViewById(R.id.view_pager_player);
        mList = mInflater.inflate(R.layout.view_pager_list, null);
        mInfo = mInflater.inflate(R.layout.view_pager_info, null);
        mPlayer = mInflater.inflate(R.layout.view_pager_play, null);
        mData.add(mList);
        mData.add(mPlayer);
        mData.add(mInfo);
        /**
         * adapter for mainViewPager
         */
        mAdapter = new MyPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mAdapter.setData(mData);
        //默认选中播放界面
        mViewPager.setCurrentItem(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        /**
         * intent from here to mList.             //mList is a pager
         *   we take something with it for display on the mList
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mediaFile");
        paths = bundle.getStringArray("path");
        names = bundle.getStringArray("name");

        mMediaList = (ListView) mList.findViewById(R.id.media_list);
        mediaListAdapter = new MediaListAdapter(this);
        mMediaList.setAdapter(mediaListAdapter);
        for (int i = 0; i < paths.length; i++) {
            MediaFile mediaFile = new MediaFile();
            mediaFile.setfPath(paths[i]);
            mediaFile.setfName(names[i]);
            /**
             * 这里暂时忽略了文件时长
             */
            mMedias.add(mediaFile);
        }
        /**
         * add action for mediaList's item
         */
        mediaListAdapter.setList(mMedias);
        mMediaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionID, long id) {
                /**
                 * 跳转到播放界面
                 */
                mViewPager.setCurrentItem(1);

                position = positionID;   //将position置位当前位置
                setWhereTxt();
                if (mMP != null) {
                    mMP.reset();
                    try {
                        mMP.setDataSource(mMedias.get(position).getfPath());
                        mMP.prepare();
                        mMP.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mMP = new MediaPlayer();
                    try {
                        mMP.setDataSource(mMedias.get(position).getfPath());
                        mMP.prepare();
                        mMP.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /**
         * add actions for each media play buttons in pager_play
         */
        View.OnClickListener onClickListenerView = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 不同模式下，上下曲按钮对应的坐标不同。
                 *  主要判断单曲模式下：  向上按钮后，向下移回到原来坐标
                 *                      向下按钮后，向上移回到原来坐标
                 */
                switch (v.getId()) {
                    case R.id.prev_button:
                        positionDown(position);
                        mMP.reset();
                        if (isSingleMode) {
                            positionUp(position);
                        }
                        playWithMode();
                        break;
                    case R.id.play_pause_button:
                        if (mMP.isPlaying()) {
                            mPlayBtn.setImageResource(R.drawable.ic_player_pause_pressed);
                            mMP.pause();
                        } else {
                            mPlayBtn.setImageResource(R.drawable.ic_player_play_pressed);
                            mMP.start();
                        }
                        break;
                    case R.id.next_button:
                        positionUp(position);
                        mMP.reset();
                        if (isSingleMode) {
                            positionDown(position);
                        }
                        playWithMode();
                        break;
                    case R.id.loop_single_button:
                        mSingleModeBtn.setVisibility(View.INVISIBLE);
                        mAllModeBtn.setVisibility(View.VISIBLE);
                        mRandomBtn.setVisibility(View.INVISIBLE);
                        playMode(ALL_MODE);
                        break;
                    case R.id.loop_all_button:
                        mSingleModeBtn.setVisibility(View.INVISIBLE);
                        mAllModeBtn.setVisibility(View.INVISIBLE);
                        mRandomBtn.setVisibility(View.VISIBLE);
                        playMode(RANDOM_MODE);
                        break;
                    case R.id.loop_random_button:
                        mSingleModeBtn.setVisibility(View.VISIBLE);
                        mAllModeBtn.setVisibility(View.INVISIBLE);
                        mRandomBtn.setVisibility(View.INVISIBLE);
                        playMode(SINGLE_MODE);
                        break;

                    default:
                        break;
                }
            }

        };

        mPrevBtn = (ImageButton) mPlayer.findViewById(R.id.prev_button);
        mPlayBtn = (ImageButton) mPlayer.findViewById(R.id.play_pause_button);
        mNextBtn = (ImageButton) mPlayer.findViewById(R.id.next_button);
        mPrevBtn.setOnClickListener(onClickListenerView);
        mPlayBtn.setOnClickListener(onClickListenerView);
        mNextBtn.setOnClickListener(onClickListenerView);

        mWhereTxt = (TextView) mPlayer.findViewById(R.id.whereTxt);
        setWhereTxt();

        //.....................singerPager...............................//
        mSingerPager = (ViewPager) mPlayer.findViewById(R.id.view_pager_singer);
        mSingerPhoto = mInflater.inflate(R.layout.view_pager_singer_photo, null);
        mSingerInfo = mInflater.inflate(R.layout.view_pager_singer_info, null);
        mSingerData.add(mSingerInfo);
        mSingerData.add(mSingerPhoto);

        mSingerAdapter = new SingerPagerAdapter(this);
        mSingerPager.setAdapter(mSingerAdapter);
        mSingerAdapter.setmData(mSingerData);

        //...............mSeekBar.....................................//

        mSeekBar = (SeekBar) mPlayer.findViewById(R.id.play_seek_bar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pro = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = progress;
                seekBar.setProgress(pro);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mMP != null) {
                    mMP.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMP != null) {
                    mMP.seekTo(pro);
                    mMP.start();
                }
            }
        });
        mCurTxt = (TextView) mPlayer.findViewById(R.id.current_time_txt);
        mTotTxt = (TextView) mPlayer.findViewById(R.id.total_time_txt);

        new SeekThread().start();
        mTotTxt.setText(totTimeStr);
        mCurTxt.setText(curTimeStr);

        //.............loop Mode......................................//
        mSingleModeBtn = (ImageButton) mPlayer.findViewById(R.id.loop_single_button);
        mAllModeBtn = (ImageButton) mPlayer.findViewById(R.id.loop_all_button);
        mRandomBtn = (ImageButton) mPlayer.findViewById(R.id.loop_random_button);
        mSingleModeBtn.setVisibility(View.INVISIBLE);
        mAllModeBtn.setVisibility(View.VISIBLE);
        mRandomBtn.setVisibility(View.INVISIBLE);
        mSingleModeBtn.setOnClickListener(onClickListenerView);
        mAllModeBtn.setOnClickListener(onClickListenerView);
        mRandomBtn.setOnClickListener(onClickListenerView);

    }

    class MyPagerAdapter extends PagerAdapter {
        List<View> data = new ArrayList<>();
        Context context;

        public MyPagerAdapter(Context context) {
            this.context = context;
        }

        public void setData(List<View> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //super.destroyItem(container, position, object);
            View view = data.get(position);
            container.removeView(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // return super.instantiateItem(container, position);
            View view = data.get(position);
            container.addView(view);
            return view;
			
        }
    }

    class MediaListAdapter extends BaseAdapter {
        List<MediaFile> list = new ArrayList<>();
        Context context;

        public MediaListAdapter(Context context) {
            this.context = context;
        }

        public void setList(List<MediaFile> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.view_file_list, null);
                viewHolder = new ViewHolder();
                viewHolder.img = (ImageView) convertView.findViewById(R.id.fileIcon);
                viewHolder.name = (TextView) convertView.findViewById(R.id.fileName);
                viewHolder.duration = (TextView) convertView.findViewById(R.id.fileCount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.img.setImageResource(R.drawable.icon_music);
            viewHolder.name.setText(mMedias.get(position).getfName());
            /**
             * 这里暂且没有设置路径和音乐文件时长
             */
            return convertView;
        }

        class ViewHolder {
            ImageView img;
            TextView name;
            TextView duration;
        }

    }

    class MediaFile {
        private String fName;
        private String fPath;

        public void setfName(String fName) {
            this.fName = fName;
        }

        public String getfName() {
            return fName;
        }

        public void setfPath(String fPath) {
            this.fPath = fPath;
        }

        public String getfPath() {
            return fPath;
        }


    }

    class SingerPagerAdapter extends PagerAdapter {
        List<View> mData = new ArrayList<>();
        Context context;

        public SingerPagerAdapter(Context context) {
            this.context = context;
        }

        public void setmData(List<View> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mData.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mData.get(position));
        }

    }

    public int positionUp(int i) {
        if (i < mMedias.size() - 1) {
            position = position + 1;
        } else {
            position = 0;
        }
        return position;
    }

    public int positionDown(int i) {
        if (i > 0) {
            position = position - 1;
        } else {
            position = mMedias.size() - 1;
        }
        return position;
    }

    public void setWhereTxt() {
        mWhereTxt.setText("第" + String.valueOf(position + 1) + "首" + "    " + "共" + mMedias.size() + "首");
    }

    /**
     * 处理seekBar事务
     */
    class SeekThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if (mMP != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSeekBar.setMax(mMP.getDuration());
                            mSeekBar.setProgress(mMP.getCurrentPosition());

                            int max = mMP.getDuration();
                            int cur = mMP.getCurrentPosition();

                            String totMin = String.valueOf((max / 1000) / 60);
                            String totSec = String.valueOf((max / 1000) % 60);

                            String curMin = String.valueOf((cur / 1000) / 60);
                            String curSec = String.valueOf((cur / 1000) % 60);

                            if (Integer.parseInt(totSec) < 10) {
                                totTimeStr = totMin + ":" + "0" + totSec;
                            } else {
                                totTimeStr = totMin + ":" + totSec;
                            }
                            if (Integer.parseInt(curSec) < 10) {
                                curTimeStr = curMin + ":" + "0" + curSec;
                            } else {
                                curTimeStr = curMin + ":" + curSec;
                            }
                            mTotTxt.setText(totTimeStr);
                            mCurTxt.setText(curTimeStr);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * 封装一个方法，用来判断曲目循环的方式
     */
    public void playMode(int modeInt) {
        switch (modeInt) {
            case RANDOM_MODE:
                isAllMode = false;
                isSingleMode = false;
                isRandomMode = true;
                break;
            case SINGLE_MODE:
                isAllMode = false;
                isSingleMode = true;
                isRandomMode = false;
                break;
            case ALL_MODE:
                isAllMode = true;
                isSingleMode = false;
                isRandomMode = false;
                break;
            default:
                break;
        }
    }

    /**
     * h这个封装的方法中，u最重要的是在单曲模式下上下曲按钮a操作时坐标必须保持不变。
     */
    public void playWithMode() {
        if (isAllMode) {
            try {
                mMP.setDataSource(mMedias.get(position).getfPath());
                setWhereTxt();
                mPlayBtn.setImageResource(R.drawable.ic_player_play_pressed);
                mMP.prepare();
                mMP.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (isSingleMode) {
            try {
                mMP.setDataSource(mMedias.get(position).getfPath());
                setWhereTxt();
                mPlayBtn.setImageResource(R.drawable.ic_player_play_pressed);
                mMP.prepare();
                mMP.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (isRandomMode) {
            try {
                int randomPosition = new Random().nextInt(mMedias.size());
                mMP.setDataSource(mMedias.get(randomPosition).getfPath());
                position = randomPosition;
                setWhereTxt();
                mPlayBtn.setImageResource(R.drawable.ic_player_play_pressed);
                mMP.prepare();
                mMP.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
