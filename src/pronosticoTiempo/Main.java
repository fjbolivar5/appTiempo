package pronosticoTiempo;

import java.io.FileNotFoundException;
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
        //String rutaDB = "./db/tiempo.db";
        String rutaDB = "..\\apirestpy\\tiempo.db";
        
        String urlTiempoProvincia = "https://www.el-tiempo.net/api/json/v2/provincias/"; //Añadir al final el cod provincia
        String urlProvincias = "https://www.el-tiempo.net/api/json/v2/provincias";
        String urlMunicipios = "https://www.el-tiempo.net/api/json/v2/municipios";
        String urlTiempoMunicipio = "https://www.el-tiempo.net/api/json/v2/provincias/"; //Añadir al final el cod provincia + "/municipios"
        
        String ficheroProvincias = "./json/provinciasAPI.json";
        String ficheroMunicipios = "./json/municipiosAPI.json";
        String ficheroTiempoProvincia = "./json/tiempoProvinciaAPI"; //Sin extension para añadir el cod_prov
        String ficheroTiempoMunicipios = "./json/tiempoMunicipioAPI"; //Sin extension para añadir el cod_municipio
        
        int filas = 0;

        try {
            System.out.println("Usando BD: " + rutaDB);
            
            //Provincias
            DescargaProvincia p = new DescargaProvincia(rutaDB, urlProvincias, ficheroProvincias);
            filas = p.guardarProvincias();
            System.out.println("PROVINCIAS: Se ha descargado de " + urlProvincias 
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            
            //Municipios
            DescargaMunicipio m = new DescargaMunicipio(rutaDB,urlMunicipios,ficheroMunicipios);
            filas = m.guardarMunicipios();
            System.out.println("MUNICIPIOS: Se ha descargado de " + urlMunicipios 
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            /*
            //Tiempo en Provincias
            DescargaTiempoProvincia tp = new DescargaTiempoProvincia(rutaDB,urlTiempoProvincia,ficheroTiempoProvincia);
            filas = tp.guardarTiempoProvincias();
            System.out.println("Tiempo Provincias: Se ha descargado de " + urlTiempoProvincia 
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            
            //Tiempo en Municipios
            DescargaTiempoMunicipio tm = new DescargaTiempoMunicipio(rutaDB,urlTiempoMunicipio,ficheroTiempoMunicipios);
            filas = tm.guardarTiempoMunicipio();
            System.out.println("Tiempo Municipios: Se ha descargado de " + urlTiempoMunicipio + "/[MUNICIPIO]"
                    + "\n e insertado o modificado " + filas + " filas en la BD " + rutaDB);
            */

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
