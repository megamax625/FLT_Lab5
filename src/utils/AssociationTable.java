package utils;
import java.util.ArrayList;

// по сути шаблон сопряжение
public class AssociationTable {
    String name;
    Table.Attribute PK1, PK2;
    ArrayList<Table.Attribute> Attributes;
    ArrayList<Table.Connection> Connections;

    public AssociationTable(RelationalTable RT1, RelationalTable RT2) {
        this.name = RT1.name + "_" + RT2.name;
        PK1 = RT1.PK;
        PK1.KeyStatus = -1;
        PK2 = RT2.PK;
        PK2.KeyStatus = -1;
        Attributes = new ArrayList<>();
        Connections = new ArrayList<>();
        Connections.add(new Table.Connection(RT1.name, "1.1Denomination"));
        Connections.add(new Table.Connection(RT2.name, "1.1Denomination"));
        RT1.Connections.remove(new Table.Connection(RT2.name, "0.1Denomination"));
        RT1.Connections.remove(new Table.Connection(RT2.name, "0.NDenomination"));
        RT1.Connections.remove(new Table.Connection(RT2.name, "1.1Denomination"));
        RT1.Connections.remove(new Table.Connection(RT2.name, "0.NDenomination"));
        RT2.Connections.remove(new Table.Connection(RT1.name, "0.1Denomination"));
        RT2.Connections.remove(new Table.Connection(RT1.name, "0.NDenomination"));
        RT2.Connections.remove(new Table.Connection(RT1.name, "1.1Denomination"));
        RT2.Connections.remove(new Table.Connection(RT1.name, "0.NDenomination"));
    }

    public void print() {
        System.out.println("Outputting table " + this.name);
        System.out.format("Primary Key 1: %s -- %s", PK1.name, PK1.type);
        if (PK1.KeyStatus == -1) {
            System.out.println("(FK)");
        } else {
            System.out.println();
        }
        System.out.format("Primary Key 2: %s -- %s", PK2.name, PK2.type);
        if (PK2.KeyStatus == -1) {
            System.out.println("(FK)");
        } else {
            System.out.println();
        }
        System.out.println("Attributes:");
        if (Attributes.isEmpty()) {
            System.out.println("None");
        } else {
            int inc = 1;
            for (Table.Attribute attr : Attributes) {
                System.out.format("%s -- %s", attr.name, attr.type);
                if (attr.KeyStatus == -1) {
                    System.out.println("(FK)");
                } else if (attr.KeyStatus == -2) {
                    System.out.format("(FK, AK%d.1)\n", inc);
                    inc += 1;
                } else if (attr.KeyStatus > 0) {
                    System.out.format("(AK%d.%d)\n", inc, attr.KeyStatus);
                } else {
                    System.out.println();
                }
            }
        }
        System.out.println("Connections:");
        if (Connections.isEmpty()) {
            System.out.println("None");
        } else {
            for (Table.Connection conn : Connections) {
                System.out.format("to %s with cardinality of %s\n", conn.destination, conn.connType);
            }
        }
        System.out.println();
    }
}
