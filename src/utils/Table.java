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

    public void print() {
        System.out.println("Outputting table " + this.name);
        System.out.println("ID: {");
        for (Attribute IDAttr : ID) {
            System.out.format("%s -- %s\n", IDAttr.name, IDAttr.type);
        }
        System.out.println("}\nAttributes:");
        for (Attribute attr : Attributes) {
            System.out.format("%s -- %s\n", attr.name, attr.type);
        }
        System.out.println("Connections:");
        for (Connection conn : Connections) {
            System.out.format("to %s with cardinality of %s\n", conn.destination, conn.connType);
        }
        System.out.println();
    }

    public static class Attribute {
        String name;
        String type;
        int KeyStatus; // n = -1 => FK, n = 0 => NO, n >= 1 => AK1.n
    }
    public static class Connection {
        String destination;
        String connType; // 1 = 0.1 | 2 = 0.N | 3 = 1.1 | 4 = 1.N
    }
}
