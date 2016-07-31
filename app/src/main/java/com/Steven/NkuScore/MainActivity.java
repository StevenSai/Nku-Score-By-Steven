package com.Steven.NkuScore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;

public class MainActivity extends BaseActivity {

	Bitmap valicodeBitmap;
	ImageView valicodeView;
	WebLogger myLogger;
	EditText EdText_user;
	EditText EdText_password;
	EditText EdText_valicode;
	String PutPassWord;
	String SetPassWord;
	WebView webView1;
	ProgressDialog prgDlg;
	CheckBox remenberpass;
	CreatFiles Scf;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
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
				if(myLogger.toast_str.equals("用户不存在或密码错误！")){
					myLogger.toast_str = "再检查下用户和密码，有错误哦！";
					EdText_password.setText("");
					EdText_password.requestFocus();
					//Toast.makeText(MainActivity.this,EdText_password.getText().toString(),Toast.LENGTH_SHORT);
					EdText_valicode.setText("");
				}else if(myLogger.toast_str.equals("请输入正确的验证码！")){
					myLogger.toast_str = "验证码输错啦→_→手别抖，别方。";
					EdText_valicode.setText("");
					EdText_valicode.requestFocus();
				}
				Toast toast=Toast.makeText(MainActivity.this,myLogger.toast_str, Toast.LENGTH_SHORT);
				toast.show();
			}
			//处理网络连接不成功的情况
			if(msg.what==0x127){
				prgDlg.dismiss();
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setTitle("网络连接超时！");
				dialog.setMessage("检查下手机网络吧！\n如果手机网络木有问题，那就是教务系统在维护咯，过一阵子再来，不要方。");
				dialog.setCancelable(false);
				dialog.setPositiveButton("重连一下", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
					}
				});
				dialog.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCollector.finishAll();
					}
				});
				dialog.show();
			}
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_main);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setTintColor(Color.parseColor("#4281C9"));
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		valicodeView=(ImageView)findViewById(R.id.imageView1);
		EdText_user=(EditText)findViewById(R.id.editText_user);
		EdText_password=(EditText)findViewById(R.id.editText_password);
		EdText_valicode=(EditText)findViewById(R.id.editText_valicode);
		webView1 = (WebView) findViewById(R.id.webView1);
		remenberpass = (CheckBox) findViewById(R.id.Cbox_rmbpass);
		Scf = new CreatFiles();
		WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView1.addJavascriptInterface(new STJ(), "Steven");
		webView1.loadUrl("file:///android_asset/StevenSecurity.html");
		pref = PreferenceManager.getDefaultSharedPreferences(this);
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

		boolean isrmb = pref.getBoolean("remenber_password",false);
		boolean user_first = pref.getBoolean("first1",true);
		if (user_first){
			pref.edit().putBoolean("first1", false).apply();
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle("资瓷不资瓷？ ");
			dialog.setMessage("\n本次更新，没做什么别的，大概三件事：\n\n1、加入了记住学号密码功能，终于不用每次都输入了。\n\n2、加入了详细学分绩统计，在表格后。\n\n3、改善联网稳定性，长时间“联网”请检查网络，若有网但连不上则说明是教务系统在维护。\n\n还有就是Fix了一些使用上的小Bug，对了还换了新图标，感谢“吃掉南开”的设计大神。另外还优化了UI细节，这些都不是主要的，主要就那三件事。\n\n很惭愧，做了一点微小的工作，谢谢大家！\n\n");
			dialog.setCancelable(true);
			dialog.setPositiveButton("吼啊", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			dialog.show();
		}

		if (isrmb){
			String pass = pref.getString("password","");
			String uname = pref.getString("username","");
			EdText_password.setText(pass);
			EdText_user.setText(uname);
			EdText_valicode.requestFocus();
			remenberpass.setChecked(true);
			Toast.makeText(MainActivity.this,"已载入保存的学号密码",Toast.LENGTH_LONG).show();
		}

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
		Button bn_clearall = (Button) findViewById(R.id.button_clear);
		Button bn_getScore=(Button)findViewById(R.id.button_getScore);
		Button bn_evaluate=(Button)findViewById(R.id.button_evaluateTeacher);
		bn_clearall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setTitle("Sure?");
				dialog.setMessage("\n清空所有输入吗？\n");
				dialog.setCancelable(true);
				dialog.setPositiveButton("只清空输入", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EdText_user.setText("");
						EdText_password.setText("");
						EdText_valicode.setText("");
						EdText_user.requestFocus();
					}
				});
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				dialog.setNeutralButton("删除保存", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EdText_user.setText("");
						EdText_password.setText("");
						EdText_valicode.setText("");
						EdText_user.requestFocus();
						editor = pref.edit();
						editor.putString("username", null);
						editor.putBoolean("remenber_password", false);
						editor.putString("password", null);
						editor.apply();
						remenberpass.setChecked(false);
						Toast.makeText(MainActivity.this,"已删除保存的学号密码",Toast.LENGTH_LONG).show();
					}
				});
				dialog.show();
			}
		});
		bn_getScore.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (!EdText_valicode.getText().toString().equals("")) {
					if (!EdText_password.getText().toString().equals("")) {
						PutPassWord = EdText_password.getText().toString();
						if (PutPassWord.length() < 100) {
							SetPassWord = "";
							webView1.loadUrl("javascript:StevenSecurity('" + PutPassWord + "')");
						}
						new Thread() {
							public void run() {
								try {
									updateLoginInfos();
									if (myLogger.getScore()) {
										editor = pref.edit();
										if (remenberpass.isChecked() && PutPassWord.length() < 100) {
											editor.putString("username", EdText_user.getText().toString());
											editor.putBoolean("remenber_password", true);
											editor.putString("password", PutPassWord);
										} else if (remenberpass.isChecked() && PutPassWord.length() >= 100) {
											editor.putBoolean("remenber_password", true);
										} else {
											editor.putString("username", null);
											editor.putBoolean("remenber_password", false);
											editor.putString("password", null);
										}
										editor.apply();
										Bundle data = new Bundle();
										data.putCharSequence("res_page", myLogger.res_page);
										data.putString("student_name",myLogger.studentName);
										Intent intent = new Intent(MainActivity.this, ShowScoreActivity.class);
										intent.putExtras(data);
										startActivity(intent);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();
					} else {
						Toast.makeText(MainActivity.this, "请输入正确的用户名和密码！", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(MainActivity.this, "验证码还是要输入的...", Toast.LENGTH_SHORT).show();
				}
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

		FileHandler();
		//Toast.makeText(MainActivity.this,Scf.getSdCardPath(),Toast.LENGTH_LONG).show();
	}


	public void FileHandler(){
		try {
			Scf.CreatPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private class STJ{
		@JavascriptInterface
		public void jsMethod(String mi){
			//EdText_password.setText(mi);
			SetPassWord = mi;
		}
	}

	public void updateLoginInfos() {
		int i=0;
		while(SetPassWord.equals("")){
		    i++;
	    }
		myLogger.setUser(EdText_user.getText().toString());
		//myLogger.setPassword(EdText_password.getText().toString());
		myLogger.setPassword(SetPassWord);
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