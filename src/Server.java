import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Server implements Runnable {
    String ip;
    int port;

    List<Neighbour> priority_1 = new ArrayList<Neighbour>();
    List<Neighbour> priority_2 = new ArrayList<Neighbour>();

    @Override
    public void run() {
        DatagramSocket sock = null;
        try {
            // creating a server socket, parameter is local port number
            sock = new DatagramSocket();
            port = sock.getLocalPort();
            ip = "127.0.0.1";

            // buffer to receive incoming data
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            // wait for an incoming data
            echo("Server socket created at " + port + ". Waiting for incoming data...");

            // bootstrap server details
            String bs_host = "localhost";
            int bs_port = 55555;

            String regString = "0114 REG " + ip + " " + port + " user" + port;
            DatagramPacket dpReg = new DatagramPacket(regString.getBytes(), regString.getBytes().length, InetAddress.getByName(bs_host), bs_port);
            sock.send(dpReg);

            // communication loop
            while (true) {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());

                // echo the details of incoming data - client ip : client port - client message
                echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);

                StringTokenizer st = new StringTokenizer(s, " ");

                String length = st.nextToken();
                String command = st.nextToken();

                if (command.equals("REGOK")) {
                    int no_nodes = Integer.parseInt(st.nextToken());

                    if (no_nodes == 9996) {

                    } else if (no_nodes == 9997) {

                    } else if (no_nodes == 9998) {
                        String unRegString = "0114 UNREG " + ip + " " + port + " user" + port;
                        DatagramPacket dpUnReg = new DatagramPacket(unRegString.getBytes(), unRegString.getBytes().length, InetAddress.getByName(bs_host), bs_port);
                        sock.send(dpUnReg);

                        sock.receive(incoming);
                        data = incoming.getData();
                        s = new String(data, 0, incoming.getLength());

                        // echo the details of incoming data - client ip : client port - client message
                        echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);

                        st = new StringTokenizer(s, " ");

                        length = st.nextToken();
                        command = st.nextToken();
                        String value = st.nextToken();

                        if (value.equals("0")) {
                            sock.send(dpReg);
                        }

                    } else if (no_nodes == 9999) {

                    } else if (no_nodes == 1) {
                        String ip_address = st.nextToken();
                        int port_no = Integer.parseInt(st.nextToken());

                        priority_1.add(new Neighbour(ip_address, port_no));

                        String newJoin = "0114 JOIN 1 " + ip + " " + port;

                        DatagramPacket dpJoin = new DatagramPacket(newJoin.getBytes(), newJoin.getBytes().length, InetAddress.getByName(ip_address), port_no);
                        sock.send(dpJoin);
                    } else if (no_nodes == 2) {
                        String ip_address1 = st.nextToken();
                        int port_no1 = Integer.parseInt(st.nextToken());

                        priority_1.add(new Neighbour(ip_address1, port_no1));

                        String newJoin1 = "0114 JOIN 1 " + ip + " " + port;
                        DatagramPacket dpJoin1 = new DatagramPacket(newJoin1.getBytes(), newJoin1.getBytes().length, InetAddress.getByName(ip_address1), port_no1);
                        sock.send(dpJoin1);

                        String ip_address2 = st.nextToken();
                        int port_no2 = Integer.parseInt(st.nextToken());

                        priority_2.add(new Neighbour(ip_address2, port_no2));

                        String newJoin2 = "0114 JOIN 2 " + ip + " " + port;
                        DatagramPacket dpJoin2 = new DatagramPacket(newJoin2.getBytes(), newJoin2.getBytes().length, InetAddress.getByName(ip_address2), port_no2);
                        sock.send(dpJoin2);
                    }


                } else if (command.equals("UNREG")) {

                } else if (command.equals("UNROK")) {

                } else if (command.equals("JOIN")) {
                    String priority = st.nextToken();
                    String ip_address = st.nextToken();
                    int port_no = Integer.parseInt(st.nextToken());
                    if (priority.equals("1")) {
                        priority_1.add(new Neighbour(ip_address, port_no));
                    } else if (priority.equals("2")) {
                        priority_2.add(new Neighbour(ip_address, port_no));
                    }
                    String reply = "0014 JOINOK 0";
                    DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                    sock.send(dpReply);
                } else if (command.equals("JOINOK")) {
                    String value = st.nextToken();
                    if (value.equals("0")) {
                    }
                } else if (command.equals("LEAVE")) {
                    String ip_address = st.nextToken();
                    int port_no = Integer.parseInt(st.nextToken());
                    for (int i = 1; i < priority_1.size(); i++) {
                        for (int j = 0; j < priority_1.size(); j++) {
                            if (priority_1.get(j).getPort() == port_no) {
                                priority_1.remove(j);

                                String reply = "0114 LEAVEOK 0";
                                DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                                sock.send(dpReply);
                            }
                        }
                    }
                } else if (command.equals("LEAVEOK")) {
                    String value = st.nextToken();
                    if (value.equals("0")) {
                    }
                } else if (command.equals("DISCON")) {
                    if (priority_1.size() > 1) {
                        for (int i = 1; i < priority_1.size(); i++) {
                            String newJoin1 = "0114 JOIN 1 " + priority_1.get(0).getIp() + " " + priority_1.get(0).getPort();
                            DatagramPacket dpJoin1 = new DatagramPacket(newJoin1.getBytes(), newJoin1.getBytes().length, InetAddress.getByName(priority_1.get(i).getIp()), priority_1.get(i).getPort());
                            sock.send(dpJoin1);

                            String newJoin2 = "0114 JOIN 1 " + priority_1.get(i).getIp() + " " + priority_1.get(i).getPort();
                            DatagramPacket dpJoin2 = new DatagramPacket(newJoin2.getBytes(), newJoin2.getBytes().length, InetAddress.getByName(priority_1.get(0).getIp()), priority_1.get(0).getPort());
                            sock.send(dpJoin2);

                            String leave = "0114 LEAVE " + ip + " " + port;
                            DatagramPacket dpLeave = new DatagramPacket(leave.getBytes(), leave.getBytes().length, InetAddress.getByName(priority_1.get(i).getIp()), priority_1.get(i).getPort());
                            sock.send(dpLeave);
                        }
                    }
                    String leave = "0114 LEAVE " + ip + " " + port;
                    DatagramPacket dpLeave = new DatagramPacket(leave.getBytes(), leave.getBytes().length, InetAddress.getByName(priority_1.get(0).getIp()), priority_1.get(0).getPort());
                    sock.send(dpLeave);

                    String reply = "0114 DISOK 0";
                    DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                    sock.send(dpReply);
                } else if (command.equals("SER")) {
                    String reply = "0114 SEROK 3 129.82.128.1 2301 baby_go_home.mp3 baby_come_back.mp3 baby.mpeg";
                    DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                    sock.send(dpReply);

                    for (int i = 0; i < priority_1.size(); i++) {
                        if (priority_1.get(i).getPort() != incoming.getPort()) {
                            DatagramPacket dp2 = new DatagramPacket(s.getBytes(), s.getBytes().length, InetAddress.getByName(priority_1.get(i).getIp()), priority_1.get(i).getPort());
                            sock.send(dp2);
                        }
                    }
                } else if (command.equals("SEROK")) {

                } else if (command.equals("ERROR")) {

                } else {
                    String reply = "0010 ERROR";
                    DatagramPacket dpReply = new DatagramPacket(reply.getBytes(), reply.getBytes().length, incoming.getAddress(), incoming.getPort());
                    sock.send(dpReply);
                }
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }

    // simple function to echo data to terminal
    public static void echo(String msg) {
        System.out.println(msg);
    }
}