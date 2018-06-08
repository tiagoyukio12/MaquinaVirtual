package com.company;

import java.io.*;
import java.util.HashMap;

class Montador {
    private int ic;
    private HashMap<String, String> TabSimb;
    private HashMap<String, Integer> TabMne;

    Montador() {
        ic = 0;
        TabSimb = new HashMap<>();
        TabMne = new HashMap<>();
    }

    public void gerarCodObj(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(filename);
        execPasso1(file);
        execPasso2(file);
    }

    private void execPasso1(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (line.charAt(0) != ' ') {  // Tem declaracao de simbolo ou label
                    String icString = Integer.toHexString(ic);
                    while (icString.length() < 4) {
                        icString = "0".concat(icString);
                    }

                    TabSimb.put(split[0], icString.substring(1));
                    if (split.length > 1)
                        if (split[1].equals("K")) {  // simbolo
                            ic++;
                        }
                } else {
                    switch (split[1]) {
                        case "@":
                            ic = Integer.parseInt(split[2], 16);
                            break;
                        case "#":
                            // termina passo
                            break;
                        default:   // Uma das 16 instrucoes de 2 bytes
                            ic += 2;
                            // TODO: usar tabMne e checar erros
                            break;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execPasso2(File file) throws FileNotFoundException, UnsupportedEncodingException {
        File output = new File("objAbs.txt");
        System.out.println(output.getAbsolutePath());
        PrintWriter writer = new PrintWriter(output);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line, icString;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (line.charAt(0) == ' ') {
                    switch (split[1]) {
                        case "@":
                            ic = Integer.parseInt(split[2], 16);
                            icString = Integer.toHexString(ic);
                            while (icString.length() < 4) {
                                icString = "0".concat(icString);
                            }
                            writer.println(icString);
                            break;
                        case "#":
                            icString = Integer.toHexString(ic);
                            while (icString.length() < 4) {
                                icString = "0".concat(icString);
                            }
                            String inicExec = icString.charAt(0) + TabSimb.get(split[2]);
                            writer.write("\n" + inicExec);
                            break;
                        case "JP":
                            writer.write("0" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "JZ":
                            writer.write("1" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "JN":
                            writer.write("2" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "LV":
                            writer.write("3" + split[2]);
                            ic += 2;
                            break;
                        case "+":
                            writer.write("4" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "-":
                            writer.write("5" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "*":
                            writer.write("6" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "/":
                            writer.write("7" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "LD":
                            writer.write("8" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "MM":
                            writer.write("9" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "SC":
                            writer.write("a" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "RS":
                            writer.write("b" + TabSimb.get(split[2]));
                            ic += 2;
                            break;
                        case "HM":
                            writer.write("c000");
                            ic += 2;
                            break;
                        case "GD":
                            writer.write("d" + split[2]);
                            ic += 2;
                            break;
                        case "PD":
                            writer.write("e" + split[2]);
                            ic += 2;
                            break;
                        case "SO":
                            writer.write("f000");
                            ic += 2;
                            break;
                    }
                } else if (split.length > 1) {
                    if (split[1].equals("K"))
                        writer.print(split[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
    }
}
