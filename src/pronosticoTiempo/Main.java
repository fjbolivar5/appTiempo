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
public class Main {

    /**
     * App para obtener información de la API "https://www.el-tiempo.net/api".
     * Extrae la información de los archivos json y los guarda en la base de
     * datos de SQLite dada en los parámetros.
     *
     * @param args[0] ruta del archivo de la base de datos SQLite, args[1] url
     * del json de informacion nacional, args[2] url del json de lista de
     * provincias, args[3] url del json de lista de municipios
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
        
        String jsonTiempoProvincia = "https://www.el-tiempo.net/api/json/v2/provincias/"; //Añadir al final el cod provincia
        String jsonProvincias = "https://www.el-tiempo.net/api/json/v2/provincias";
        String jsonMunicipios = "https://www.el-tiempo.net/api/json/v2/municipios";
        String jsonTiempoMunicipio = "https://www.el-tiempo.net/api/json/v2/provincias/";
        
        String ficheroProvincias = "./json/provinciasAPI.json";
        String ficheroMunicipios = "./json/municipiosAPI.json";
        String ficheroTiempoProvincia = "./json/tiempoProvinciaAPI"; //Sin extension para añadir el cod_prov
        String ficheroTiempoMunicipio = "./json/tiempoMunicipioAPI";
        
        int filas = 0;

        try {
            System.out.println("Usando BD: " + rutaDB);
            /*
            //Provincias
            DescargaProvincia p = new DescargaProvincia(rutaDB, jsonProvincias, ficheroProvincias);
            filas = p.guardarProvincias();
            System.out.println("PROVINCIAS: Se ha descargado de " + jsonProvincias 
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            
            //Municipios
            DescargaMunicipio m = new DescargaMunicipio(rutaDB,jsonMunicipios,ficheroMunicipios);
            filas = m.guardarMunicipios();
            System.out.println("MUNICIPIOS: Se ha descargado de " + jsonMunicipios 
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            */
            //Tiempo en Provincias
            DescargaTiempoProvincia tp = new DescargaTiempoProvincia(rutaDB,jsonTiempoProvincia,ficheroTiempoProvincia);
            filas = tp.guardarTiempoProvincias();
            System.out.println("Tiempo Provincias: Se ha descargado de " + jsonTiempoProvincia 
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            
            //Tiempo en Municipios
            //DescargaTiempoMunicipio tm = new DescargaTiempoMunicipio(rutaDB,jsonTiempoMunicipio,ficheroTiempoMunicipio);
            //filas = tm.guardarTiempoMunicipio();
            

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Error FileNotFound: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error IOException: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Error Json: " + e.getMessage());
        }
    }

}
