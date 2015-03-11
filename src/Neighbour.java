public class Neighbour {
    private String ip;
    private int port;
    private String username;

    public Neighbour(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Neighbour(String ip, int port, String username) {
        this.ip = ip;
        this.port = port;
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
