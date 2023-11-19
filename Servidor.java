import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Scanner;

public class Servidor implements Runnable {
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private ServerSocket mi_Socket;
   Servidor_auxiliar contolador;
    /*constructor */
    public Servidor(){
        Thread hilo = new Thread(this);
        hilo.start();//inicia el proceso para ejcutarse
    }
    public void aceptar_conexion(){

        try{
            mi_Socket = new ServerSocket(5050);//establezco el puerto por defecto
            System.out.println("Esperando conexion entrante en el puerto " + String.valueOf(5050) + "...");
            while(true){
                socket = mi_Socket.accept();//acepto la conexion 
                System.out.println("Conexion establecida con: " + socket.getInetAddress().getHostName() + "\n\n\n");
                entrada = new DataInputStream(socket.getInputStream());
                salida = new DataOutputStream(socket.getOutputStream());
                contolador = new Servidor_auxiliar(socket,entrada,salida);
                Thread hilo2 = new Thread(contolador);
                hilo2.start();
            }
        }catch(Exception e){
            System.out.println("No se pudo establecer conexion desde el servidor con el cliente..!" + e);
        }
    }


    public void run(){
        try{
            aceptar_conexion();
            
        }finally{
            System.out.println("adios..!");
        }
    } 

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
    }

}
