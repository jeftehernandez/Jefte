
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner; 
public class Servidor_auxiliar implements Runnable{
    private DataInputStream entrada;
    private DataOutputStream salida;
    private Socket socket;
    private Scanner scanner;
    /*constructor vacio */
    public Servidor_auxiliar(){

    }
    public Servidor_auxiliar(Socket sock , DataInputStream in, DataOutputStream out){
        this.entrada = in;
        this.salida = out;
        this.socket=sock;
        this.scanner = new Scanner(System.in);
    }

    /*public void gestionar_flujos(DataInputStream in, DataOutputStream out){

    }*/

    
    public void recibir_mensaje(){
        String mensaje;
        try{
            while(true){
                mensaje = entrada.readUTF();
                System.out.println("el mensaje del cliente para servidor es:" + mensaje);
            }
        }catch(IOException e){
            System.out.println("error al recibir mensaje del cliente...!" + e);
        }
    }
    public void enviar_mensaje(){
        String mensaje;
        try{
            while(true){
                System.out.print("Mensaje servidor:");
                mensaje = scanner.nextLine();
                salida.writeUTF(mensaje);
                salida.flush();
            }
        }catch(IOException e){
            System.out.println("error al enviar mensaje al cliente desde server auxiliar" + e);
        }
    }

    public void cerrar_conexiones(){
        try{    
            entrada.close();
            salida.close();
            socket.close();
            System.out.println("conexion terminada..!");
        }catch(IOException e){
            System.out.println("error al cerrar los flujos de entrada y salida y conexiones" + e);
        }
    }


    public String esperarConfirmacion(){
        String mensajeconfirmacion = null;
        try{
            System.out.println("Esperando mensaje de confirmación del cliente...");
            mensajeconfirmacion=entrada.readUTF();
            //return mensajeconfirmacion;
        }catch(IOException e){
            System.out.println("Error al esperar el mensaje de confirmación del cliente: " + e);
        }
        return mensajeconfirmacion;
        
    }
    @Override
    public void run(){
        Thread hiloRecepcion = new Thread(this::recibir_mensaje);
        Thread hiloEnvio = new Thread(this::enviar_mensaje);
        String confirmacion = esperarConfirmacion();
        if(confirmacion.equals("confirmacion")){
            hiloRecepcion.start();
            hiloEnvio.start();
        }
        try{
           // while (true) {
            //recibir_mensaje();
            //enviar_mensaje();
            hiloRecepcion.join();
            hiloEnvio.join();
        //}
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            cerrar_conexiones();
        }
    }

    /*catch(Exception e){
        System.out.println("error al enviar o recibir mensaje en este hilo del servidor auxiliar" + e);
    }*/
}
