import java.io.*;
import java.net.*;

public class Client implements Runnable
{
    /*public static void main(String args[])
    {

    }*/
    int port;

    Client (int port) {
        this.port = port;
    }

    @Override
    public void run() {

        DatagramSocket sock = null;
        String s;

        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            sock = new DatagramSocket(port);

            InetAddress host = InetAddress.getByName("localhost");

            while(true)
            {
                //take input and send the packet
                echo("Enter message to send : ");
                s = (String)cin.readLine();

                int portMes = Integer.parseInt( s.substring(0,5) );
                byte[] b = s.getBytes();

                DatagramPacket  dp = new DatagramPacket(b , b.length , host , portMes);
                sock.send(dp);

                //now receive reply
                //buffer to receive incoming data
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                sock.receive(reply);

                byte[] data = reply.getData();
                s = new String(data, 0, reply.getLength());

                //echo the details of incoming data - client ip : client port - client message
                echo(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
            }
        }

        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }

    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
}