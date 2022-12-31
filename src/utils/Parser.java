package utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Parser {
    public static String[] parameters = new String[] {"TableDelimeter", "AttributeTypeDelimeter", "ConnectionDenomination",
        "0.1Denomination", "0.NDenomination", "1.1Denomination", "1.NDenomination"};
    private static final List<String> parametersList = Arrays.asList(parameters);
    public static String[] defaultDenominators = new String[] {",", ":", "CONNECTION", "0.1", "0.N", "1.1", "1.N"};
    private static final List<String> defaultDenominatorsList = Arrays.asList(defaultDenominators);
    public static HashMap<String, String> ParseParameterizedTokens(String syntaxStr) {
        HashMap<String, String> ParameterizedTokens = new HashMap<>();
        if (syntaxStr.replaceAll("\\s", "").isEmpty()) {
            System.out.println("Syntax input file is empty; using default values");
            for (String para : parametersList) {
                ParameterizedTokens.put(defaultDenominatorsList.get(parametersList.indexOf(para)), para);
            }
        } else if (syntaxStr.equals("""
                TableDelimeter = , ~
                AttributeTypeDelimeter = , ~
                ConnectionDenomination = CONNECTION ~
                0.1Denomination = 0.1 ~
                0.NDenomination = 0.N ~
                1.1Denomination = 1.1 ~
                1.NDenomination = 1.N""")) {
            for (String para : parametersList) {
                ParameterizedTokens.put(defaultDenominatorsList.get(parametersList.indexOf(para)), para);
            }
        } else {
            String[] syntax = syntaxStr.replaceAll("\\s", "").split("~");
            for (String s : syntax) {
                String[] str = s.split("=");
                if (str.length != 2) {
                    System.out.println("Incorrect syntax definition: amount of = in one expression is not 1 in expression: " + s);
                    System.exit(4);
                } else {
                    String parameterName = str[0];
                    String parameterDenomination = str[1];
                    if (parametersList.contains(str[0]) && !parameterDenomination.isEmpty())
                        ParameterizedTokens.put(parameterDenomination, parameterName);
                }
            }
            // добиваем не вошедшие в пользовательский синтаксис параметры дефолтными значениями
            for (String para : parametersList) {
                if (!ParameterizedTokens.containsValue(para))
                    ParameterizedTokens.put(defaultDenominatorsList.get(parametersList.indexOf(para)), para);
            }
        }
        System.out.println("Resulting Map of Parameterized tokens:");
        for (String paraDen : ParameterizedTokens.keySet()) {
            System.out.println(paraDen + " -- " + ParameterizedTokens.get(paraDen));
        }
        return ParameterizedTokens;
    }

    public static ArrayList<Table> ParseTables(String testInput, HashMap<String, String> parameterMap) {
        String text = testInput.replaceAll("\\s", "");
        if (text.isEmpty()) {
            System.out.println("Table data is empty");
            System.exit(5);
        }
        System.out.println("Parsing table data:\n" + text);
        ArrayList<Table> tables = new ArrayList<>();
        ArrayList<String> tokens = Tokenize(text, parameterMap);
        //while (!tokens.isEmpty()) {
        //    tables.add(ParseTable(tokens, parameterMap));
        //}
        return tables;
    }

    private static ArrayList<String> Tokenize(String text, HashMap<String, String> parameterMap) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        String[] nonParametherizedConstantStuff = new String[] {"{", "}", "ID", ",",
                "int", "float", "money", "datetime", "char", "String"};
        String[] parameterNames = parameterMap.keySet().toArray(new String[0]);
        String[] parasAndNonParas = Stream.concat(Arrays.stream(nonParametherizedConstantStuff),
                Arrays.stream(parameterNames)).toArray(String[]::new);
        boolean checkParasAgain;
        while (!text.isEmpty()) {
            checkParasAgain = false;
            for (String para : parasAndNonParas) {
                if (text.startsWith(para)) {
                    checkParasAgain = true;
                    if (!buf.isEmpty()) {
                        tokens.add(buf.toString());
                        buf = new StringBuilder();
                    }
                    tokens.add(para);
                    text = text.substring(para.length());
                }
            }
            if (!checkParasAgain) {
                if (!text.isEmpty()) {
                    buf.append(text.charAt(0));
                    text = text.substring(1);
                } else {
                    if (!buf.isEmpty()) {
                        tokens.add(buf.toString());
                    }
                }
            }
        }
        System.out.println("Tokens:");
        for (String token : tokens) {
            System.out.println(token);
        }
        return tokens;
    }

    //private static Table ParseTable(ArrayList<String> tokens, HashMap<String, String> parameterMap) {
//
  //  }
}
