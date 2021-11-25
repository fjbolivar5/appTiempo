package pronosticoTiempo;

import java.io.*;
import java.net.*;

public class DescargaJson {
    
    //Esta propiedad sirve para establecer conexion con la URL que le pasaremos mas adelante
    private static HttpURLConnection conexion;
    
    public static void main(String[] args){
        
        //Para empezar, creamos unas variables que nos permitan obtener la informacion 
        //que hay dentro del archivo Json que esta en internet
        BufferedReader leer;                            //Leemos
        String linea;                                   //Escribimos lineas
        StringBuffer sBuffer = new StringBuffer();      //Escribiremos todas la informacion que saquemos de las lineas
        File destino = new File("./json/provinciasAPI.json"); //Archivo donde guardaremos el contenido descargado
        
        //Esta es la URL de donde vamos a sacar los datos
        String rutaURL = "https://www.el-tiempo.net/api/json/v2/provincias";
        
        try{
            //Con la clase URL podemos hacer referencia a la URL deonde se encuentra el archivo Json
            URL url = new URL(rutaURL);
            
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
                
                //En el caso en el que si haya conexion. Meteremos todos los datos dentro de la cariable sBuffer
                System.out.println("Se ha conectado correctamente");
                leer = new BufferedReader(new InputStreamReader(conexion.getInputStream()));    //leermos las linas
                //Y lo metemos dentro de la variable sBuffer mediante un bucle y
                //del archivo de destino
                if(!destino.exists()){
                    destino.createNewFile();
                }
                FileWriter jsonDestino = new FileWriter(destino);
                while((linea = leer.readLine()) != null){
                    jsonDestino.write(linea);
                    sBuffer.append(linea);
                }
                //Cerramos todo una vez que haya terminado
                leer.close();
                jsonDestino.close();
                
            }else{
                
                //En caso contrario, se informará de que ha surgido un error.
                //Y hacemos el mismo paso, donde no se leeran las lineas
                leer = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
                while((linea = leer.readLine()) != null){
                    sBuffer.append(linea);
                }
                //Lo cerramos una vez que haya terminado
                leer.close();
                
            }
            
            //Mostramos por pantalla la informacion que hemos metido dentro de la variable "sBuffer"
            System.out.println(sBuffer.toString());
            
        }catch(MalformedURLException e){
            System.out.println("Error en MalformedURLException");
        }catch(IOException e){
            System.out.println("Error en IOException: " + e.getMessage());
        }finally{
            //Finalmente, tenemos que desconectarnos de la URL
            conexion.disconnect();
        }
        
    }
}
