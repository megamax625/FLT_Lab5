package utils;
import java.util.ArrayList;

public class RelationalTable {
    String name;
    Table.Attribute PK;
    ArrayList<Table.Attribute> Attributes;
    ArrayList<Table.Connection> Connections;

    public RelationalTable(Table table) {
        this.name = table.name;
        this.Attributes = new ArrayList<>();
        this.Connections = new ArrayList<>();
        Connections = table.Connections;
        if ((table.ID.size() == 1) && (table.ID.get(0).type.equals("int"))) {
            System.out.println("No need for surrogate key in table " + table.name);
            Attributes = table.Attributes;
            PK = table.ID.get(0);
        } else {
            PK = new Table.Attribute(table.name + "_surrogate_ID", "int");
            Attributes = table.ID;
            Attributes.addAll(table.Attributes);
        }
    }

    public static ArrayList<RelationalTable> RelationalTableFromArrayList(ArrayList<Table> tables) {
        ArrayList<RelationalTable> RTs = new ArrayList<>();
        for (Table table : tables){
            RelationalTable RT = new RelationalTable(table);
            RTs.add(RT);
        }
        return RTs;
    }

    public static ArrayList<AssociationTable> GetForeignKeys(ArrayList<RelationalTable> RTs) {
        ArrayList<AssociationTable> ATs = new ArrayList<>();
        for (RelationalTable RT1 : RTs) {
            for (Table.Connection conn1 : RT1.Connections) {
                for (RelationalTable RT2 : RTs) {
                    if ((RT1 != RT2) && (conn1.destination.equals(RT2.name))) {
                        Table.Connection back_conn = new Table.Connection();
                        for (Table.Connection conn2 : RT2.Connections) {
                            if (conn2.destination.equals(RT1.name)) {
                                back_conn = conn2;
                                System.out.println("Matched connections from " + conn2.destination + " and " + conn1.destination);
                                break;
                            }
                        }
                        if (!back_conn.destination.equals(RT1.name)) {
                            System.out.println("Table " + RT2.name + " misses a backing connection to table " +
                                    RT1.name + ", who has one to them");
                            System.exit(11);
                        }
                        if (conn1.connType.equals("0.1Denomination") || conn1.connType.equals("1.1Denomination")) {
                            if (back_conn.connType.equals("0.1Denomination") || back_conn.connType.equals("1.1Denomination")) {
                                boolean hasFK = false;
                                System.out.println("Checking 1-to-1 connection for 1 FK or inserting a new one");
                                for (Table.Attribute attr : RT2.Attributes) {
                                    if ((attr.KeyStatus == -1) && (attr.name.equals(RT1.PK.name))) {
                                        hasFK = true;
                                        break;
                                    }
                                }
                                if (!hasFK) {
                                    RT1.Attributes.add(new Table.Attribute(RT2.PK.name, RT2.PK.type, -1));
                                    System.out.println("Adding FK " + RT2.PK.name + " to Table " + RT1.name);
                                }
                            } else {
                                System.out.println("Adding PK " + RT2.PK.name + " to child table " + RT1.name);
                                RT1.Attributes.add(new Table.Attribute(RT2.PK.name, RT2.PK.type, -1));
                            }
                        } else {
                            if (back_conn.connType.equals("0.NDenomination") || back_conn.connType.equals("1.NDenomination")) {
                                boolean hasAT = false;
                                for (AssociationTable AT : ATs) {
                                    if (AT.name.equals(RT1.name + "_" + RT2.name) || AT.name.equals(RT2.name + "_" + RT1.name)) {
                                        hasAT = true;
                                        break;
                                    }
                                }
                                if (!hasAT) {
                                    System.out.println("Making association table from tables " + RT1.name + " and " + RT2.name + " since they are connected N-to-M");
                                    AssociationTable AT = new AssociationTable(RT1, RT2);
                                    ATs.add(AT);
                                }
                            }
                        }
                    }
                }
            }
        }
        return ATs;
    }

    public void print() {
        System.out.println("Outputting table " + this.name);
        System.out.format("Primary Key: %s -- %s\n", PK.name, PK.type);
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
