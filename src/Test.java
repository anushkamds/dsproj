import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String args[])
    {
        List<Neighbour> priority_1 = new ArrayList<Neighbour>();
        priority_1.add(new Neighbour("localhoht", 0));
        priority_1.add(new Neighbour("localhoht", 1));
        priority_1.add(new Neighbour("localhoht", 2));
        priority_1.add(new Neighbour("localhoht", 3));
        priority_1.add(new Neighbour("localhoht", 4));
        priority_1.add(new Neighbour("localhoht", 5));

        echo(priority_1.size()+"");
        priority_1.remove(2);
        echo(priority_1.size()+"");
        for (int i=0; i<priority_1.size(); i++){
            /*if(priority_1.get(i).getPort() == 1){

            }*/
        }

    }

    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
}
