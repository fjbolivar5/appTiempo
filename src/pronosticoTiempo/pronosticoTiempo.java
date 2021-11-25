
package pronosticoTiempo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Fran Bolivar
 */

public class pronosticoTiempo {

    /**
     * App para obtener información de la API "https://www.el-tiempo.net/api".
     * Extrae la información de los archivos json y los guarda en la base de
     * datos de SQLite dada en los parámetros.
     * 
     * @param args[0] ruta del archivo de la base de datos SQLite,
     * args[1] url del json de informacion nacional,
     * args[2] url del json de lista de provincias,
     * args[3] url del json de lista de municipios
     */
    public static void main(String[] args) {    
        /* ARGUMENTOS: "./db/tiempo.db" "https://www.el-tiempo.net/api/json/v2/home" "https://www.el-tiempo.net/api/json/v2/provincias" "https://www.el-tiempo.net/api/json/v2/municipios"
        String rutaDB = args[0];
        String jsonNacional = args[1];
        String jsonProvincias = args[2];
        String jsonMunicipios = args[3];
        */

        // PARA PRUEBAS
        String rutaDB = "./db/tiempo.db";
        String jsonNacional = "./json/nacional.json";
        String jsonProvincias = "./json/provincias.json"; 
        String jsonMunicipios = "./json/municipios.json";

        try{
            System.out.println("Usando BD: " + rutaDB);
            System.out.println("Usando Json: " + jsonProvincias);
            
            Provincia p = new Provincia(rutaDB,jsonProvincias);
            int filas = p.guardarProvincias();
            
            System.out.println("Se han insertado o modificado " + filas + " filas en la BD " + rutaDB);
            
        }catch(SQLException e){
            System.out.println("Error SQL: " + e.getMessage());
        }catch(FileNotFoundException e){
            System.out.println("Error FileNotFound: " + e.getMessage());
        }catch(IOException e){
            System.out.println("Error IOException: " + e.getMessage());
        }catch(ParseException e){
            System.out.println("Error Json: " + e.getMessage());
        }
    }
    
}
