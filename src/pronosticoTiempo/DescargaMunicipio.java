
package pronosticoTiempo;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
            //Doy el formato correcto al archivo descargardo
            BufferedReader fr = new BufferedReader(new FileReader(ficheroDestino));
            StringBuffer sBuffer = new StringBuffer("{\"municipios\":");
            //Inicio la variable 'linea' con el contenido que hay que añadir al principio al archivo municipios
            String linea="";
            //sBuffer.append("{\"municipios\":[");
            while((linea=fr.readLine())!=null){
                sBuffer.append(linea);
            }
            //Añado a 'sBuffer' lo necesario en el final del archivo
            sBuffer.append("}");
            fr.close();
            
            //Sobreescribo el fichero con los datos modificados.
            BufferedWriter fw = new BufferedWriter (new FileWriter(ficheroDestino));
            fw.write(sBuffer.toString());
            fw.close();
                        
            //Comienza la lectura del Json para su inserccion en la BD
            Object obj = parser.parse(new FileReader(ficheroDestino));

            JSONObject jsonObject =  (JSONObject) obj;

            JSONArray municipios = (JSONArray) jsonObject.get("municipios");
            Iterator<JSONObject> iterator = municipios.iterator();
            //Inicio la conexion con la base de datos
            connect = DriverManager.getConnection("jdbc:sqlite:"+db);
            if(connect!=null){
                while (iterator.hasNext()) {
                    JSONObject municipio = iterator.next();

                    String cod_mun = (String)municipio.get("COD_GEO");
                    String nombre = (String)municipio.get("NOMBRE");
                    String cod_prov = (String)municipio.get("CODPROV");

                    String sql = "INSERT OR REPLACE INTO municipios ('codmuni','nombre','codprov') "
                            + "VALUES (?, ?, ?)";
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setInt(1, Integer.valueOf(cod_mun));
                    statement.setString(2, nombre);
                    statement.setInt(3, Integer.valueOf(cod_prov));

                    filas += statement.executeUpdate();
                }
                connect.close();
            }//if connect
        }//if exito
        //System.out.println("Filas insertadas: " + filas);
        return filas;
    }
    
    
}
