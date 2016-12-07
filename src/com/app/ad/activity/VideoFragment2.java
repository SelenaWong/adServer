package com.app.ad.activity;

import java.io.File;
import java.util.TimerTask;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.ad.R;
import com.app.ad.utils.ConnectionLog;
import com.app.ad.utils.FileUtil;
import com.app.ad.utils.ToastUtil;
import com.app.ad.widget.MyPlayer;

public class VideoFragment2 extends Fragment  implements OnPreparedListener
{

	private final String TAG = "AdActivity";
	private SurfaceView sv;
	private MyPlayer myPlayer;
	RelativeLayout layout_video=null;
	int which_file=1;
	private String 	adPath=null;//Environment.getExternalStorageDirectory().getPath()+dictory+"海底左边投影.wmv";
	private String winPricePath=null;// Environment.getExternalStorageDirectory().getPath()+dictory+"0424探测海底石油海水.mp4";
	private boolean blChange = true;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);  
        FileUtil fileutil = new FileUtil(); 
	    sv=(SurfaceView)rootView.findViewById(R.id.sv);
        myPlayer = new MyPlayer(sv,getAdFile());
        return rootView;
    }
    
    public String getAdFile(){
    	return this.adPath;
    }
    public void setAdFile(String filePath){
    	this.adPath = filePath;
    }
    public void setPriceFile(String filePath){
    	this.winPricePath = filePath;
    }
    public String getPriceFile(){
    	return this.winPricePath;
    }
    

	public void log(String message){
		log(message,null);
	}
	
	public void log(String message,Throwable e){

		try{
			if(e!=null){
				Log.e(TAG, e.getMessage());
			}else{
				Log.i(TAG, message);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}
    
	public void start(){
		if( myPlayer!=null  && myPlayer.IsPlay()){
			myPlayer.pause();
		}else if(myPlayer!=null && !myPlayer.IsPlay()){
			myPlayer.play();
			return;
		}else {
			play(which_file);
		}
	}

	/*
	 * 停止播放
	 */
	public void stop() {
		if (myPlayer != null && myPlayer.IsPlay()) {
			myPlayer.stop();
			myPlayer.release();
		}
		myPlayer = null;
	}
	
	public void playAd( ){
		which_file=1;
		blChange = true;
		play(which_file);
	}
  
	public void playWinPrice( ){
		which_file=2;
		blChange =true;
		play(which_file);
	}
	
	/**
	 * 开始播放
	 * 
	 * @param msec 播放初始位置    
	 */
	protected void play( int which) {
		
		try {
			which_file =which;
			String path="";// 获取视频文件地址
			if(which==1){
				path=adPath;
			}else {
				path=winPricePath;
			}
			Log.i(TAG,"path= "+path);
			File file = new File(path);
			if (!file.exists()) {
				ToastUtil.ShowText(getActivity().getApplicationContext(), "视频文件路径错误");
				Log.i(TAG,"视频文件路径错误");
				return;
			}
			if(myPlayer==null){
				myPlayer = new MyPlayer(sv,getAdFile());
			}
			myPlayer.playUrl(path);
			blChange =false;

		} catch (Exception e) {
			Log.e(TAG, "Exception:"+e.toString());
			Log.i(TAG,e.toString());
			e.printStackTrace();
		}
	}
	
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 5001:
				Bundle bd = msg.getData();
				int index = bd.getInt("whichfile");
				play(index);
				break;
			}	
		}
	};

	boolean isPlaying(){
		if(myPlayer==null){
			return false;
		}
		else if(myPlayer.IsPlay()){
			return true;
		}
		return false;
	}
	
    
	@Override
	public void onDestroy() {
		try{
			if(myPlayer!=null){
				myPlayer.stop();
				myPlayer.release();
				myPlayer=null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		super.onDestroy();
	}


	/**
	 * Description 
	 * @param mp 
	 * @see android.media.MediaPlayer.OnPreparedListener#onPrepared(android.media.MediaPlayer) 
	 */ 
		
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
	}

}
