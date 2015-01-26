package com.example.ecompressor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CompresionLZW {
	public static final int TAMINICIALDICCIONARIO = 256;
	public static final int TAMINICIALPALABRA = 12;
	private static final int tampalabra = 12;

	// usamos un map para el acumulador de Bytes
	// el valor maximo del integer que escribe sera de 4095 palabras como viene
	// puesto en el compresor.
	private Map<ByteArray, Integer> tabCodificacion;
	// Lista de acumulador de Bytes para descomprimir
	private List<ByteArray> tabDescodificacion;

	public void carga() {
		this.tabCodificacion = new HashMap<ByteArray, Integer>();
		this.tabDescodificacion = new ArrayList<ByteArray>();
		// carga el ASCII para el diccionario
		for (int i = 0; i < TAMINICIALDICCIONARIO; i++) {
			this.tabCodificacion.put(new ByteArray((byte) i),
					Integer.valueOf(i));
			this.tabDescodificacion.add(new ByteArray((byte) i));
		}
	}

	public String comprimir(String documento) throws IOException {
		carga();
		int codigos = TAMINICIALDICCIONARIO;
		// 4095
		int max = (1 << tampalabra) - 1;
		// utilizamos la practica 3 para escribir
		BufferedReader bufEntrada = new BufferedReader(new FileReader(new File(
				documento)));
		// le pasamos la ruta desde donde se cargo el archivo, para que se
		// genere en la misma carpeta, con el nombre comprido.txt
		BinaryOut bufSalida = new BinaryOut(this.getRuta(documento)
				+ this.getNombre(documento) + "_comprimido.txt");
		int primerByte = bufEntrada.read();
		ByteArray b = new ByteArray((byte) primerByte);
		while (bufEntrada.ready()) {
			int segundoByte = bufEntrada.read();
			ByteArray k = new ByteArray((byte) segundoByte);
			ByteArray bk = new ByteArray(b).append(k);
			// busca si esta en el map, si lo esta concatena, sino los escribe
			// si cabe
			if (tabCodificacion.containsKey(bk)) {
				b = bk;
			} else {
				bufSalida.write((this.tabCodificacion.get(b)).intValue(),
						tampalabra);
				if (codigos < max) {
					tabCodificacion.put(bk, codigos++);
				}
				b = k;
			}
		}
		bufSalida.write((this.tabCodificacion.get(b)).intValue(), tampalabra);
		bufSalida.flush();
		bufEntrada.close();
		bufSalida.close();
		return this.getRuta(documento) + this.getNombre(documento)
				+ "_comprimido.txt";
	}

	public String descomprimir(String documento) throws IOException {
		carga();
		BinaryIn bufEntrada = new BinaryIn(documento);
		// como antes, permite guardar en la misma ruta desde donde se le da el
		// archivo.
		BinaryOut bufSalida = new BinaryOut(this.getRuta(documento)
				+ this.getNombre(documento) + "_descomprimido.txt");
		// seguimos haciendo uso de la practica 3
		int primera = bufEntrada.readInt(tampalabra);
		// evitamos que se escriban espacios en blanco
		bufSalida.write(primera, 8);
		int letra = primera;
		int segundo;
		while (!bufEntrada.isEmpty()) {
			segundo = bufEntrada.readInt(tampalabra);
			ByteArray cad = new ByteArray();
			// si no esta en la tabla
			if (segundo >= this.tabDescodificacion.size()) {
				cad = new ByteArray(this.tabDescodificacion.get(primera));
				cad.append((byte) letra);
			} else {
				cad = this.tabDescodificacion.get(segundo);
			}
			// se imprime la cadena
			for (int i = 0; i < cad.size(); i++) {
				if ((cad.get(i) <= 32 && bufEntrada.isEmpty()) == false) {
					// evita que se cree un espacio al final-
					bufSalida.write(cad.get(i));
				}
			}
			letra = cad.get(0);
			// 1º +char
			tabDescodificacion.add(new ByteArray(tabDescodificacion
					.get(primera)).append((byte) letra));
			primera = segundo;

		}

		bufEntrada.close();
		bufSalida.flush();
		bufSalida.close();
		return this.getRuta(documento) + this.getNombre(documento)
				+ "_descomprimido.txt";
	}

	public String getRuta(String ruta) {
		StringTokenizer st = new StringTokenizer(ruta, "/");
		StringBuilder sb = new StringBuilder();
		int num = st.countTokens();
		for (int i = 1; i < num; i++) {
			sb.append(st.nextToken() + "/");
		}
		return sb.toString();
	}

	// obtiene el nombre sin el .txt
	public String getNombre(String ruta) {
		StringTokenizer st = new StringTokenizer(ruta, "/");
		StringBuilder sb = new StringBuilder();
		int num = st.countTokens();
		for (int i = 1; i < num; i++) {
			st.nextToken();
		}
		String s = st.nextToken();
		sb.append(s.substring(0, s.length() - 4));
		return sb.toString();
	}
}
