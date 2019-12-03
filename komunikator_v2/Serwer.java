
package komunikator_v2;
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

public class Serwer {
    public static void main(String[] args) throws IOException  
    {
        ServerSocket ss = new ServerSocket(2222);
        
        //Włączenie nieskończonej pętli nasłuchiwania klietów
        while(true)
        {
            Socket s = null;
            
            try
            {
                s = ss.accept();
                
                System.out.println("Nowy klient: " + s);
                
                DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                
                System.out.println("Przydzielanie wątku klientowi");
                
                Thread t = new ClientHandler(s, dis, dos);
                
                t.start();
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }  
}

class ClientHandler extends Thread
{
    DateFormat fordate = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s =s;
        this.dis = dis;
        this.dos = dos;
    }
    
    @Override
    public void run()
    {
        String otrzymane;
        String dozwrotu;
        while(true)
        {
            try{
                dos.writeUTF("Co chcesz?[Data | Czas]..\n"+"Napisz Exit aby zakończyć połączenie.");
                
                otrzymane = dis.readUTF();
                if(otrzymane.equals("Exit"))
                {
                    System.out.println("Klient " + this.s +" wysłał exit");
                    System.out.println("Zamykanie połączenia");
                    this.s.close();
                    System.out.println("Połączenie zamknięte");
                    break;
                }
                
                Date date = new Date();
                
                switch (otrzymane){
                    case "Data":
                        dozwrotu = fordate.format(date);
                        dos.writeUTF(dozwrotu);
                        break;
                    case "Czas":
                        dozwrotu = fortime.format(date);
                        dos.writeUTF(dozwrotu);
                        break;
                    default:
                        dos.writeUTF("OcoChodzi?");
                        break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        try
        {
            this.dis.close();
            this.dos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}