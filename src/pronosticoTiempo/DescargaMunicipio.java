
package pronosticoTiempo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Fran Bolivar
 */

public class DescargaMunicipio {

    private String db;
    private Connection connect;
    private String urlMunicipios;
    private String ficheroDestino;

    /* CONSTRUCTOR */
    /**
     * 
     * @param db ruta de la base de datos SQLite
     * @param urlJson URL del archivo json con los municipios
     * @param destino ruta del archivo json de salida
     */
    public DescargaMunicipio(String db,String urlJson, String destino) {
        this.db = db;
        this.urlMunicipios = urlJson;
        this.ficheroDestino = destino;
        connect = null;
    }
        
    
    /**
     * Extrae la informacion del archivo json dado en el constructor con los municipios
     * @return nº de registros insertados
     */
    public int guardarMunicipios() 
            throws SQLException, FileNotFoundException, IOException, ParseException {
        
        JSONParser parser = new JSONParser();
        int filas = 0;
        boolean exito=false;
        
        //Descarga y creacion del json
        DescargaJson descargar = new DescargaJson(urlMunicipios,ficheroDestino);
        exito=descargar.descarga();
   
        if(exito){
            Object obj = parser.parse(new FileReader(ficheroDestino));

            JSONObject jsonObject =  (JSONObject) obj;

            JSONArray municipios = (JSONArray) jsonObject.get("");
            Iterator<JSONObject> iterator = municipios.iterator();
            //Inicio la conexion con la base de datos
            connect = DriverManager.getConnection("jdbc:sqlite:"+db);
            if(connect!=null){
                while (iterator.hasNext()) {
                    JSONObject municipio = iterator.next();

                    String cod_mun = (String)municipio.get("COD_GEO");
                    String nombre = (String)municipio.get("NOMBRE");
                    String longitud = (String)municipio.get("LONGITUD_ETRS89_REGCAN95");               
                    String latitud = (String)municipio.get("LATITUD_ETRS89_REGCAN95");
                    String altitud = (String)municipio.get("ALTITUD");
                    String cod_prov = (String)municipio.get("CODPROV");

                    String sql = "INSERT OR REPLACE INTO municipios ('codgeo','nombre','longitud','latitud','altitud','codprov') "
                            + "VALUES (?, ?, ?, ?, ?,?)";
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setInt(1, Integer.valueOf(cod_mun));
                    statement.setString(2, nombre);
                    statement.setString(3, longitud);
                    statement.setString(4, latitud);
                    statement.setInt(5, Integer.valueOf(altitud));
                    statement.setInt(6, Integer.valueOf(cod_prov));

                    filas += statement.executeUpdate();
                }
                connect.close();
            }//if connect
        }//if exito
        //System.out.println("Filas insertadas: " + filas);
        return filas;
    }
    
    
}
