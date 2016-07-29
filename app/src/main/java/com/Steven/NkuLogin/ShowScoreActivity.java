package com.Steven.NkuLogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.webkit.WebView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ShowScoreActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_show_score);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setTintColor(Color.parseColor("#212121"));

		WebView tw=(WebView)findViewById(R.id.webView1);
		Intent intent=getIntent();
		String res_page=intent.getCharSequenceExtra("res_page").toString();
		tw.loadDataWithBaseURL("about:blank", res_page, "text/html", "utf-8", "");
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()+"/我开查分",
					"成绩单.html");
			//第二个参数意义是说是否以append方式添加内容
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
			bw.write(res_page);
			bw.flush();
			System.out.println("写入成功");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.show_score, menu);
//		return true;
//	}

}
