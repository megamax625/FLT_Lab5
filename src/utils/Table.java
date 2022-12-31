package utils;
import java.util.ArrayList;

public class Table {
    String name;
    ArrayList<Attribute> ID;
    ArrayList<Attribute> Attributes;
    ArrayList<Connection> Connections;

    public Table(String name) {
        this.name = name;
        this.ID = new ArrayList<>();
        this.Attributes = new ArrayList<>();
        this.Connections = new ArrayList<>();
    }

    public class Attribute {
        String name;
        String type;
        int KeyStatus; // n = -1 => FK, n = 0 => NO, n >= 1 => AK1.n
    }
    public class Connection {
        Table destination;
        int connType; // 1 = 0.1 | 2 = 0.N | 3 = 1.1 | 4 = 1.N
    }
}
