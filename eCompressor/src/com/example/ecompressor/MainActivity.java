package com.example.ecompressor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	// private EditText palabra, nMinimo;
	private Object[] cadena;
	private static final int PICKFILE_RESULT_CODE = 1;
	final static String ACT_INFO1 = "com.example.ecompressor.MainActivity";
	// final static String ACT_INFO2
	// ="com.example.anagramaker.AnagramaPalarasExisten";
	private ProgressDialog pd;
	private Context context = this;
	private String ruta = "";
	private Generador gn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gn = new Generador();
		cadena = new String[1];
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onDestroy() {
		if (pd != null) {
			pd.dismiss();
		}
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	public void Cargar(View v) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle("¿Tienes algun explorador de archivos?");
		alertDialogBuilder
				.setMessage(
						"Debes tener algun explorador de archivos para poder cargar el fichero de texto.")
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("file/*");
						startActivityForResult(intent, PICKFILE_RESULT_CODE);
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case PICKFILE_RESULT_CODE:
			if (resultCode == RESULT_OK) {
				String FilePath = data.getData().getPath();
				if (FilePath
						.substring(FilePath.length() - 4, FilePath.length())
						.contains(".txt")) {
					ruta = FilePath;
					Toast toast = Toast.makeText(this,
							"Archivo cargado con exito!", Toast.LENGTH_LONG);
					toast.show();

				} else {
					Toast toast = Toast
							.makeText(
									this,
									"Solo permite comprimir ficheros del tipo .txt, carga otro por favor.",
									Toast.LENGTH_LONG);
					toast.show();
					ruta = "";

				}
			}
			break;
		}
	}

	@SuppressLint("NewApi")
	public void Comprimir(View v) {
		if (ruta.isEmpty()) {
			Toast toast = Toast.makeText(this, "carga un archivo",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			cadena[0] = gn.comprimir(ruta);
			Intent act = new Intent(this, Resultado.class);
			act.putExtra(ACT_INFO1, cadena);
			startActivity(act);
		}
	}

	@SuppressLint("NewApi")
	public void Descomprimir(View v) {
		if (ruta.isEmpty()) {
			Toast toast = Toast.makeText(this, "carga un archivo",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			if (ruta.contains("comprimido")) {
				cadena[0] = gn.descomprimir(ruta);
				Intent act = new Intent(this, Resultado.class);
				act.putExtra(ACT_INFO1, cadena);
				startActivity(act);
			} else {
				Toast toast = Toast.makeText(this, "archivo no descomprimible",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		}
	}

}
