package com.Steven.NkuLogin;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ShowScoreActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_score);
		WebView tw=(WebView)findViewById(R.id.webView1);
		Intent intent=getIntent();
		String res_page=intent.getCharSequenceExtra("res_page").toString();
		tw.loadDataWithBaseURL("about:blank",res_page, "text/html", "utf-8","");
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.show_score, menu);
//		return true;
//	}

}
