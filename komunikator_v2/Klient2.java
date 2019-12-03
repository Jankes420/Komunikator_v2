
package komunikator_v2;

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class Klient2 {
    public static void main(String[] args) throws IOException  
    {
        try
        {
            Scanner scn = new Scanner(System.in);
            
            InetAddress ip = InetAddress.getByName("localhost");
            
            Socket s = new Socket(ip, 2222);
            
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            
            while(true)
            {
                System.out.println(dis.readUTF());
                String dowyslania = scn.nextLine();
                dos.writeUTF(dowyslania);
                
                if(dowyslania.equals("Exit"))
                {
                    System.out.println("Zamyanie połączenia" + s);
                    s.close();
                    System.out.println("Połączenie zamknięte");
                    break;
                }
                
                String otrzymane = dis.readUTF();
                System.out.println(otrzymane);
            }
            
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
