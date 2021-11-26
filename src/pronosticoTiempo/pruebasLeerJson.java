/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pronosticoTiempo;

import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author user
 */
public class pruebasLeerJson  {
    public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();

        String ficheroDestino = "./json/municipiosAPI.json";

        Object obj = parser.parse(new FileReader(ficheroDestino));

        JSONObject jsonObject =  (JSONObject) obj;

        JSONArray municipios = (JSONArray) jsonObject.get("municipios");
        Iterator<JSONObject> iterator = municipios.iterator();

        while (iterator.hasNext()) {
            JSONObject municipio = iterator.next();

            String cod_mun = (String)municipio.get("COD_GEO");
            String nombre = (String)municipio.get("NOMBRE");
            /*
            String longitud = (String)municipio.get("LONGITUD_ETRS89_REGCAN95");               
            String latitud = (String)municipio.get("LATITUD_ETRS89_REGCAN95");
            String altitud = (String)municipio.get("ALTITUD");
            String cod_prov = (String)municipio.get("CODPROV");
            */

            System.out.printf("Cod: %s - Nombre: %s\n",cod_mun,nombre);

        }
    }
}
