
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

public class Provincia {
    String titulo;
    String db;
    Connection connect;
    String jsonProvincias;

    /* CONSTRUCTOR */
    /**
     * 
     * @param db ruta de la base de datos SQLite
     * @param json ruta del archivo json con las provincias
     */
    public Provincia(String db,String json) {
        this.db = db;
        this.jsonProvincias = json;
        this.titulo = "";
        connect = null;
    }
        
    
    /**
     * Extrae la informacion del archivo json dado en el constructor con las provincias
     * @return nº de registros insertados
     */
    public int guardarProvincias() 
            throws SQLException, FileNotFoundException, IOException, ParseException {
        
        JSONParser parser = new JSONParser();
        int filas = 0;
   
        Object obj = parser.parse(new FileReader(jsonProvincias));

        JSONObject jsonObject =  (JSONObject) obj;

        titulo = (String) jsonObject.get("title");

        JSONArray provincias = (JSONArray) jsonObject.get("provincias");
        Iterator<JSONObject> iterator = provincias.iterator();
        //Inicio la conexion con la base de datos
        connect = DriverManager.getConnection("jdbc:sqlite:"+db);
        if(connect!=null){
            while (iterator.hasNext()) {
                JSONObject provincia = iterator.next();
                
                String cod_prov = (String)provincia.get("CODPROV");
                String nombre = (String)provincia.get("NOMBRE_PROVINCIA");
                String cod_auton = (String)provincia.get("CODAUTON");               
                String comunidad = (String)provincia.get("COMUNIDAD_CIUDAD_AUTONOMA");
                String capital = (String)provincia.get("CAPITAL_PROVINCIA");
                
                String sql = "INSERT OR REPLACE INTO provincias ('codprov','nombre','codauton','comunidad','capital') "
                        + "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connect.prepareStatement(sql);
                statement.setInt(1, Integer.valueOf(cod_prov));
                statement.setString(2, nombre);
                statement.setString(3, cod_auton);
                statement.setString(4,comunidad);
                statement.setString(5, capital);
                
                /* PARA DEPURAR
                System.out.printf("Prov: %s - Nombre: %s - Capital: %s - Comunidad: %s - Cod. Com: %s\n", 
                                    cod_prov,nombre,capital,comunidad,cod_auton);
                */
                
                filas += statement.executeUpdate();
            }
            connect.close();
        }
        //System.out.println("Filas insertadas: " + filas);
        return filas;
    }
    
    
}
