package pronosticoTiempo;

import java.io.*;
import java.net.*;

public class DescargaJson {
    
    private String urlAPI;
    private String rutaDestino; //Archivo donde guardaremos el contenido descargado
    
    //Esta propiedad sirve para establecer conexion con la URL que le pasaremos mas adelante
    private static HttpURLConnection conexion;
    

    public DescargaJson(String url, String rutaDestino) {
        this.urlAPI = url;
        this.rutaDestino = rutaDestino;
    }
    
    public boolean descarga() throws MalformedURLException, IOException{
        
        //Para empezar, creamos unas variables que nos permitan obtener la informacion 
        //que hay dentro del archivo Json que esta en internet
        BufferedReader leer;                            //Leemos
        String linea;                                   //Escribimos lineas
        File destino = new File(rutaDestino);
        boolean conseguido=false;
                
        try{
            //Con la clase URL podemos hacer referencia a la URL deonde se encuentra el archivo Json
            URL url = new URL(urlAPI);
            
            //Ahora abrimos una conexion con el archivo Json de internet
            conexion = (HttpURLConnection) url.openConnection();
            
            //Con este linea, llamando al metodo GET, obtenemos los datos del archivo Json
            conexion.setRequestMethod("GET");
            
            //Esperaremos minimo 5 segundos para que se conecte correctamente
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);
            
            //Ahora vamos a ver si la conexion a sido exitosa
            int exito = conexion.getResponseCode();
            
            //Si el valor de la variable "exito" es igual a 200 hay una buena conexion
            if(exito == 200){
                
                //En el caso en el que si haya conexion.
                leer = new BufferedReader(new InputStreamReader(conexion.getInputStream()));    //leermos las linas
                //Y lo metemos dentro del archivo de destino
                if(!destino.exists()){
                    destino.createNewFile();
                }
                FileWriter jsonDestino = new FileWriter(destino);
                while((linea = leer.readLine()) != null){
                    jsonDestino.write(linea);
                }
                //Cerramos todo una vez que haya terminado
                leer.close();
                jsonDestino.close();
                
                conseguido=true;
                
            }
            
        }finally{
            //Finalmente, tenemos que desconectarnos de la URL en cualquier caso
            conexion.disconnect();
        }
        
        return conseguido;
    }
}
