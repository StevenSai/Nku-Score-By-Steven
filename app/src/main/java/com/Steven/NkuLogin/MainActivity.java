package com.Steven.NkuLogin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	Bitmap valicodeBitmap;
	ImageView valicodeView;
	WebLogger myLogger;
	EditText EdText_user;
	EditText EdText_password;
	EditText EdText_valicode;
	String PutPassWord;
	WebView webView1;
	ProgressDialog prgDlg;
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==0x123){
				valicodeView.setImageBitmap(valicodeBitmap);
			}
			if(msg.what==0x124){
				prgDlg.setMax(100);
				prgDlg.setTitle("让我飞一会儿~~");
				prgDlg.setMessage(myLogger.progress_str);
				prgDlg.setIndeterminate(false);
				prgDlg.setCancelable(false);
				prgDlg.setProgress(myLogger.progress_int);
				prgDlg.show();
			}
			if(msg.what==0x125){
				prgDlg.dismiss();
			}
			if(msg.what==0x126){
				Toast toast=Toast.makeText(MainActivity.this,myLogger.toast_str, Toast.LENGTH_SHORT);
				if(myLogger.toast_str.equals("用户不存在或密码错误！")){
					EdText_password.setText("");
					EdText_valicode.setText("");
				}
				toast.show();
			}
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		valicodeView=(ImageView)findViewById(R.id.imageView1);
		EdText_user=(EditText)findViewById(R.id.editText_user);
		EdText_password=(EditText)findViewById(R.id.editText_password);
		EdText_valicode=(EditText)findViewById(R.id.editText_valicode);
		webView1 = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView1.addJavascriptInterface(new STJ(), "Steven");
		webView1.loadUrl("file:///android_asset/StevenSecurity.html");
		prgDlg=new ProgressDialog(MainActivity.this);
		myLogger=new WebLogger();
		myLogger.myActivity=this;
		new Thread(){
			public void run(){
				try{
					myLogger.init();

				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();

		EdText_user.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void afterTextChanged(Editable s){
				if (myLogger.logined){
					myLogger.logined=false;
					EdText_password.setText("");
					EdText_valicode.setText("");
					new Thread(){
						public void run(){
							myLogger.init();
						}
					}.start();
				}
			}

		} );
		Button bn_getScore=(Button)findViewById(R.id.button_getScore);
		Button bn_evaluate=(Button)findViewById(R.id.button_evaluateTeacher);
		bn_getScore.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				PutPassWord = EdText_password.getText().toString();
				if(PutPassWord.length()<100) {
					webView1.loadUrl("javascript:StevenSecurity('" + PutPassWord + "')");
				}
				new Thread(){
					public void run(){
						try{
							sleep(500);
							updateLoginInfos();
							if(myLogger.getScore()){
								Bundle data=new Bundle();
								data.putCharSequence("res_page", myLogger.res_page);
								Intent intent=new Intent(MainActivity.this, ShowScoreActivity.class);
								intent.putExtras(data);
								startActivity(intent);
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}.start();
			}

		});


		bn_evaluate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				PutPassWord = EdText_password.getText().toString();
				if(PutPassWord.length()<100) {
					webView1.loadUrl("javascript:StevenSecurity('" + PutPassWord + "')");
				}
				new Thread(){
					public void run(){
						try{
							sleep(500);
							updateLoginInfos();
							myLogger.evaluateTeacher();
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}.start();
			}

		});
	}



	private class STJ{
		@JavascriptInterface
		public void jsMethod(String mi){
			EdText_password.setText(mi);
		}
	}

	public void updateLoginInfos() {
		myLogger.setUser(EdText_user.getText().toString());
		myLogger.setPassword(EdText_password.getText().toString());
		myLogger.setValicode(EdText_valicode.getText().toString());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("( ⊙ o ⊙ )！");
			dialog.setMessage("\n嘿！你确定要走了吗？ \n");
			dialog.setCancelable(true);
			dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCollector.finishAll();
				}
			});
			dialog.setPositiveButton("继续查分", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			dialog.setNeutralButton("本App的故事", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(MainActivity.this,AboutActivity.class);
					startActivity(i);
				}
			});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}


	//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}