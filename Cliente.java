/*aqui hare todo lo necesario para atender al cliente*/
import java.util.Scanner; 
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
public class Cliente  implements Runnable{
    /*atributos */
    private Socket socket;
    private DataOutputStream enviar_datos;
    private DataInputStream recibir_datos;
    Scanner scanner =new Scanner (System.in);
    
   public Cliente(){
      /*Thread hilo = new Thread(this);
      hilo.start();//inicia el proceso para ejcutarse
      */
      Thread hilo = new Thread(this::run);
      hilo.start();
   }

   public void levantar_conexion(){//metodo para levantar la conexion
    try{
        socket = new Socket("localhost", 5050);//defino inmediatamente el puerto
        System.out.println("Conectado a :" + socket.getInetAddress().getHostName());//muestro mensaje al puerto que me he conectado
    }catch(Exception e){
        System.out.println("Error al levantar conexión...!");
    }

   }
   public void abrir_flujos(){
     try{
        enviar_datos = new DataOutputStream(socket.getOutputStream());//abre fujo de salida
        recibir_datos = new DataInputStream(socket.getInputStream());//abre flujo de entrada

     }catch(Exception e){
        System.out.println("No se pudieron abrir los flujos de entrada y salida para el cliente..!" + e);
     }
   }

   public void enviar_mensaje_inicial() {
      String confirmacion;
      try {
          // Envía un mensaje especial indicando que está listo para recibir mensajes del servidor
          System.out.print("escriba el mensaje de confirmacion (confirmacion):");
          confirmacion=scanner.nextLine();
          enviar_datos.writeUTF(confirmacion);
          enviar_datos.flush();
      } catch (Exception e) {
          System.out.println("Error al enviar mensaje inicial del cliente...!" + e);
      }
  }


   public void enviar_mensaje(){
     String mensaje;
     try{
        while(true){
            System.out.print("Mensaje cliente:");
            System.out.println("\n");
            mensaje = scanner.nextLine();
            enviar_datos.writeUTF(mensaje);
            enviar_datos.flush();//limpia el buffer de salida
        }
    }catch(Exception e){
        System.out.println("No se pudo enviar mensaje del cliente...!" + e);
     }
   }

   public void recibir_mensaje(){
     String mensaje;
     try{
        do{
            mensaje = recibir_datos.readUTF();
            System.out.println("mensaje servidor: " + mensaje);
            System.out.println("\n");
        }while(!mensaje.equals("salir"));
     }catch(Exception e){
        System.out.println("No se ha podido recibir mensaje del servidor...!" + e);
     }
   }

   public void cerrar_conexiones(){
     try{
        enviar_datos.close();
        recibir_datos.close();
        socket.close();
        System.out.println("conexiones cerradas");
     }catch(Exception e){
        System.out.println("no se pudo cerrar conexiones del cliente correctamente" + e);
     }
   }
   
   @Override
   public void run(){
      Thread envio = new Thread(this::enviar_mensaje);
      Thread recepcion = new Thread(this::recibir_mensaje);
     try{
         levantar_conexion();
         abrir_flujos();
         enviar_mensaje_inicial(); // Envía el mensaje especial inicial
         envio.start();
         recepcion.start();

         envio.join();
         recepcion.join();
         //enviar_mensaje();
        // recibir_mensaje();
     } catch (InterruptedException e) {
      e.printStackTrace();
   }finally{
         cerrar_conexiones();
     }
   }
   public static void main(String[] args) {
      Cliente cliente = new Cliente();
      //cliente.enviar_mensaje();
   }
}//fin de la clase
