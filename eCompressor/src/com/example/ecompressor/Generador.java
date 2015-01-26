package com.example.ecompressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class Generador {

	// permite comprimir un archivo y devolver el tiempo de compresion y su
	// ratio
	public String comprimir(String ruta_archivo) {
		String comp;
		try {
			File inicial = new File(ruta_archivo);
			CompresionLZW lzw = new CompresionLZW();
			long ini = System.nanoTime();
			File nuevo;
			nuevo = new File(lzw.comprimir(ruta_archivo));

			long fin = System.nanoTime();
			String resultado = tiempoAlgoritmo(ini, fin, inicial.length(),
					nuevo.length(), true);
			comp = ("Exito en la compresión: " + resultado);
		} catch (IOException e) {
			comp = ("Fallo en la compresión: \n" + e);
			return comp;
		}
		return comp;
	}

	// descomprime y muestra el tiempo de descompresion
	public String descomprimir(String ruta_archivo) {
		String desc;
		try {
			File inicial = new File(ruta_archivo);
			CompresionLZW lzw = new CompresionLZW();
			long ini = System.nanoTime();
			File nuevo = new File(lzw.descomprimir(ruta_archivo));
			long fin = System.nanoTime();
			String resultado = tiempoAlgoritmo(ini, fin, inicial.length(),
					nuevo.length(), false);
			desc = "Exito en la descompresión: \n" + resultado;
		} catch (IOException e) {
			desc = ("Fallo en la descompresión: " + e);
			return desc;
		}
		return desc;

	}

	// calcula el tiempo de compresion, descompresion y ratio, tanto en
	// nanosegundos como en segundos
	public String tiempoAlgoritmo(long ini, long fin, long tamEntrada,
			long tamSalida, boolean compresion) {
		String sol = "";
		if (compresion) {
			sol = "tiempo de compresión "
					+ (((double) fin - ini))
					+ " nanosegundos ("
					+ (((double) fin - ini) / 1000000000.0)
					+ " segundos) "
					+ " \n con ratio de compresión de "
					+ (((float) tamSalida / (float) tamEntrada))
					+ " %."
					+ "\n"
					+ "El fichero se guardara en la misma carpeta del fichero original con nombre_comprimido.txt \n para ver el fichero puedes usar cualquier aplicación de lectura de ficheros para android!";
		} else {
			sol = "tiempo de descompresión "
					+ (((double) fin - ini))
					+ " nanosegundos ("
					+ (((double) fin - ini) / 1000000000.0)
					+ " segundos )"
					+ "\n"
					+ "El fichero se guardara en la misma carpeta del fichero comprimido con nombre_descomprimido.txt \n para ver el fichero puedes usar cualquier aplicación de lectura de ficheros para android!";
		}
		return sol;
	}

	// TODO arreglar para que no falle
	// utilizando diffutil evalua dos ficheros, si los ficheros son iguales
	// devuelve un mensaje de correcto, sino los cambios.
	public String getDiferencias(String ruta_viejo, String ruta_nuevo) {

		List<String> viejoList = fileToLines(ruta_viejo);
		List<String> nuevoList = fileToLines(ruta_nuevo);
		Patch patch = DiffUtils.diff(viejoList, nuevoList);
		List<Delta> diff = patch.getDeltas();
		if (diff.isEmpty()) {
			return "(DiffUtils msg): No hay cambios";
		} else {
			return diff.toString();
		}
	}

	private static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		BufferedReader in;

		try {
			in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

}
