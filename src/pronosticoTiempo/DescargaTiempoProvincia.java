
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
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Fran Bolivar
 */

public class DescargaTiempoProvincia {
    private String titulo;
    private String db;
    private Connection connect;
    private String urlProvincias;
    private String ficheroDestino;

    /* CONSTRUCTOR */
    /**
     * 
     * @param db ruta de la base de datos SQLite
     * @param urlJson URL del archivo json con las provincias
     * @param destino ruta del archivo json de salida
     */
    public DescargaTiempoProvincia(String db,String urlJson, String destino) {
        this.db = db;
        this.urlProvincias = urlJson;
        this.ficheroDestino = destino;
        this.titulo = "";
        connect = null;
    }
        
    
    /**
     * Extrae la informacion del archivo json dado en el constructor con las provincias
     * @return nº de registros insertados
     */
    public int guardarTiempoProvincias() 
            throws SQLException, FileNotFoundException, IOException, ParseException {
        
        JSONParser parser = new JSONParser();
        int filas = 0;
        boolean exito=false;
        List<Integer> codProv = new ArrayList<Integer>();
        
        //Es necesario conocer el código de la provincia a descargar
        //Lo extraemos de la BD, de la tabla provincias
        //Descargamos todos los datos de todas las provincias y generamos
        //un solo json con todo
        connect = DriverManager.getConnection("jdbc:sqlite:"+db); //Conectamos a la bd
        String sql_prov = "SELECT codprov FROM provincias";
        Statement consulta = connect.createStatement();
        boolean ejecuta = consulta.execute(sql_prov);
        if(ejecuta){
            ResultSet rs = consulta.getResultSet();
            while(rs.next()){
                codProv.add(rs.getInt(1));
            }           
        }
        connect.close();
        
        for(Integer provincia: codProv){
            //Creo la url para descargar el Json
            String url = urlProvincias.concat(String.valueOf(provincia));
            //Descarga y creacion del json
            String ficheroProv = ficheroDestino.concat(String.valueOf(provincia)+".json");
            DescargaJson descargar = new DescargaJson(url,ficheroProv);
            exito=descargar.descarga();

            if(exito){
                Object obj = parser.parse(new FileReader(ficheroProv));

                JSONObject jsonObject =  (JSONObject) obj;

                titulo = (String) jsonObject.get("title");
                String hoy="";
                String manana="";
                
                JSONArray tiempoHoy = (JSONArray) jsonObject.get("today");
                Iterator<JSONObject> iterator = tiempoHoy.iterator();
                while (iterator.hasNext()) {
                    JSONObject p = iterator.next();
                    hoy = (String)p.get("p");
                }
                JSONArray tiempoManana = (JSONArray) jsonObject.get("tomorrow");
                iterator = tiempoManana.iterator();
                while (iterator.hasNext()) {
                    JSONObject p = iterator.next();
                    manana = (String)p.get("p");
                }
                
                
                //Inicio la conexion con la base de datos
                connect = DriverManager.getConnection("jdbc:sqlite:"+db);
                if(connect!=null){                      

                    String sql = "INSERT OR REPLACE INTO tiempoProvincia ('codprov','hoy','manana','fecha') "
                            + "VALUES (?, ?, ?, GETDATE())";
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setInt(1, provincia);
                    statement.setString(2, hoy);
                    statement.setString(3, manana);

                    filas += statement.executeUpdate();
                }//if connect
                
                connect.close();
            }//if exito 
        }
          
        //System.out.println("Filas insertadas: " + filas);
        return filas;
    }
    
    
}
