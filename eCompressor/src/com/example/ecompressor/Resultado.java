package com.example.ecompressor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class Resultado extends Activity {
	
	private String[] info;
	private TextView res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado);
		
		res = (TextView) findViewById(R.id.textViewRes);
		Intent men = getIntent();
		info = men.getStringArrayExtra(MainActivity.ACT_INFO1);
		res.setText(info[0]);
		//info[0];

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado, menu);
		return true;
	}

}
