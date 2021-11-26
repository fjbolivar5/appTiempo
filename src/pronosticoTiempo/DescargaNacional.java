package pronosticoTiempo;

import java.io.*;
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


public class DescargaNacional {
    private String titulo;
    private String db;
    private Connection connect;
    private String urlNacional;
    private String ficheroDestino;
    
    
    public DescargaNacional(String db,String urlJson, String destino) {
        this.db = db;
        this.urlNacional = urlJson;
        this.ficheroDestino = destino;
        this.titulo = "";
        connect = null;
    }
    
    
    public int guardarProvincias() 
            throws SQLException, FileNotFoundException, IOException, ParseException {
        
        JSONParser parser = new JSONParser();
        int filas = 0;
        boolean exito=false;
        
        //Descarga y creacion del json
        DescargaJson descargar = new DescargaJson(urlNacional,ficheroDestino);
        exito=descargar.descarga();
        
        if(exito){
            
            Object obj = parser.parse(new FileReader(ficheroDestino));

            JSONObject jsonObject =  (JSONObject) obj;

            titulo = (String) jsonObject.get("title");

            JSONArray nacionalidades = (JSONArray) jsonObject.get("ciudades");
            Iterator<JSONObject> iterator = nacionalidades.iterator();
            //Inicio la conexion con la base de datos
            connect = DriverManager.getConnection("jdbc:sqlite:"+db);
            
            if(connect!=null){
                while (iterator.hasNext()) {
                    JSONObject nacion = iterator.next();
                    
                    String id = (String)nacion.get("id");
                    String id_provincia = (String)nacion.get("idProvince");
                    String nombre = (String)nacion.get("name");               
                    String nombre_provincia = (String)nacion.get("nameProvince");
                    
                    //Al contener 2 apartados dentro de "stateSky", vamos a tener que hacer una tabla para estos apartados
                    JSONArray stateSky = (JSONArray)nacion.get("stateSky");
                    Iterator<JSONObject> iteratorstateSky = stateSky.iterator();
                    
                    while (iteratorstateSky.hasNext()) {
                        JSONObject nacion2 = iteratorstateSky.next();
                        
                        //Variables recogidas
                        String descripcion = (String)nacion2.get("description");
                        String id_stateSky = (String)nacion2.get("id");
                        
                        String sql = "INSERT OR REPLACE INTO provincias ('id_stateSky', 'temperatura') "
                            + "VALUES (?, ?)";
                        
                        //Variables insertadas
                        PreparedStatement statementstateSky = connect.prepareStatement(sql);
                        statementstateSky.setString(1, descripcion);
                        statementstateSky.setInt(2, Integer.valueOf(id_stateSky));
                    
                    }
                    
                    //Lo mismo pasa con "temperatures"
                    JSONArray temperatura = (JSONArray)nacion.get("temperatures");
                    Iterator<JSONObject> iteratorTemperaturas = temperatura.iterator();
                    
                    while (iteratorTemperaturas.hasNext()) {
                        JSONObject nacion3 = iteratorTemperaturas.next();
                        
                        //Variables recogidas
                        String max = (String)nacion3.get("max");
                        String min = (String)nacion3.get("min");
                        
                        String sql = "INSERT OR REPLACE INTO provincias ('max', 'min') "
                            + "VALUES (?, ?)";
                        
                        //Variables insertadas
                        PreparedStatement statementTemperaturas = connect.prepareStatement(sql);
                        statementTemperaturas.setInt(1, Integer.valueOf(max));
                        statementTemperaturas.setInt(2, Integer.valueOf(min));
                    }
                    
                    String sql = "INSERT OR REPLACE INTO provincias ('id','id_provincia','nombre','nombre_provincia',"
                            + "'stateSky','descripcion','id_stateSky', 'temperatura', 'max', 'min') "
                            + "VALUES (?, ?, ?, ?, ?, ?)";
                    
                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.setInt(1, Integer.valueOf(id));
                    statement.setInt(2, Integer.valueOf(id_provincia));
                    statement.setString(3, nombre);
                    statement.setString(4, nombre_provincia);
                    statement.setObject(5, stateSky);
                    statement.setObject(6, temperatura);
                    filas += statement.executeUpdate();
                    
                }
                connect.close();
            }
        }
        return filas;
    }
    
}
