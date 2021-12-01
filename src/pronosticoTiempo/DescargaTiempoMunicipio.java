
package pronosticoTiempo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Fran Bolivar
 */

public class DescargaTiempoMunicipio {
    private String titulo;
    private String db;
    private Connection connect;
    private String urlMunicipio;
    private String ficheroDestino;

    /* CONSTRUCTOR */
    /**
     * 
     * @param db ruta de la base de datos SQLite
     * @param urlJson URL del archivo json con el tiempo de los municipios
     * @param destino ruta del archivo json de salida
     */
    public DescargaTiempoMunicipio(String db,String urlJson, String destino) {
        this.db = db;
        this.urlMunicipio = urlJson;
        this.ficheroDestino = destino;
        this.titulo = "";
        connect = null;
    }

    
    /**
     * Extrae la informacion del archivo json dado en el constructor con las provincias
     * @return nº de registros insertados
     */
    public int guardarTiempoMunicipio() 
            throws SQLException, FileNotFoundException, IOException, ParseException {
        
        JSONParser parser = new JSONParser();
        int filas = 0;
        List<String> codProv = new ArrayList<String>();

        //Es necesario conocer el código de la provincia a descargar
        //Lo extraemos de la BD, de la tabla provincias
        connect = DriverManager.getConnection("jdbc:sqlite:" + db); //Conectamos a la bd
        String sql_prov = "SELECT codprov FROM provincias";
        Statement consulta = connect.createStatement();
        boolean ejecuta = consulta.execute(sql_prov);
        if(ejecuta){
            ResultSet rs = consulta.getResultSet();
            while(rs.next()){
                codProv.add(rs.getString(1));
            }           
        }
        
        //Cerramos la conexion con la BD
        connect.close();

        boolean exito=false;
        for(String provincia: codProv){
            
            List<String> codMuni = new ArrayList<String>(); //ArrayList para los municipios de cada provincia
            
            //Para descargar la info de los municipios de cada provincia,
            //necesitamos tener los codigos de municipios de diche provincia.
            //Los descargamos de la tabla municipios
            connect = DriverManager.getConnection("jdbc:sqlite:" + db); //Conectamos a la bd
            ejecuta = false;
            String sql_muni = "SELECT codmuni FROM municipios WHERE codprov ='" + provincia + "'";
            ejecuta = consulta.execute(sql_muni);
            if(ejecuta){
                ResultSet rs = consulta.getResultSet();
                while(rs.next()){
                    codMuni.add(rs.getString(1));
                }           
            }
            //Cerramos la conexion con la BD
            connect.close();
            
            for(String municipio: codMuni){

                //Creo la url para descargar el Json
                String url = urlMunicipio.concat(provincia + "/municipios/" + municipio);
                //Descarga y creacion del json
                String ficheroMunicipios = ficheroDestino.concat("_" + municipio + ".json");
                DescargaJson descargar = new DescargaJson(url,ficheroMunicipios);
                exito=descargar.descarga();

                if(exito){
                    Object obj = parser.parse(new FileReader(ficheroMunicipios));

                    JSONObject jsonObject =  (JSONObject) obj;
                    titulo = (String) jsonObject.get("title");
                    JSONObject jsonTemp = (JSONObject) jsonObject.get("temperaturas");
                    String minima = (String) jsonTemp.get("min");
                    String maxima = (String) jsonTemp.get("max");
                    String lluvia=(String) jsonObject.get("lluvia");

                    //Inicio la conexion con la base de datos
                    connect = DriverManager.getConnection("jdbc:sqlite:"+db);
                    if(connect!=null){                      

                        String sql = "INSERT OR REPLACE INTO tiempoMunicipio ('codmuni','fecha','minima','maxima', 'lluvia') "
                                + "VALUES (?, DATE(),?, ?, ?)";
                        PreparedStatement statement = connect.prepareStatement(sql);
                        statement.setString(1, municipio);
                        statement.setString(2, minima);
                        statement.setString(3, maxima);
                        statement.setString(4, lluvia);

                        filas += statement.executeUpdate();
                    }//if connect

                    connect.close();
                }//if exito 

            }//for municipio
            
            //Vacío el array para volver a rellenarlo con los municipios de la siguiente provincia
            codMuni.clear(); 
            
            System.out.println("Provincia: " + provincia + " terminada.");
            
        }//for provincia

        //System.out.println("Filas insertadas: " + filas);
        return filas;
    }
    
    
}
