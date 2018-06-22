package virtualMachine;

import java.io.*;
import java.util.HashMap;

class Assembler {
    private int ic;
    private int byteCounter;
    private HashMap<String, String> tabSimb;
    private HashMap<String, Integer> tabMne;

    Assembler() {
        ic = 0;
        byteCounter = 0;
        tabSimb = new HashMap<>();
        tabMne = new HashMap<>();
        tabMne.put("JP", 3);
        tabMne.put("JZ", 3);
        tabMne.put("JN", 3);
        tabMne.put("LV", 3);
        tabMne.put("+", 3);
        tabMne.put("-", 3);
        tabMne.put("*", 3);
        tabMne.put("/", 3);
        tabMne.put("LD", 3);
        tabMne.put("MM", 3);
        tabMne.put("SC", 3);
        tabMne.put("RS", 3);
        tabMne.put("HM", 3);
        tabMne.put("GD", 3);
        tabMne.put("PD", 3);
        tabMne.put("SO", 3);
    }

    void gerarCodObj(String filename) throws FileNotFoundException {
        File file = new File(filename);
        execPasso1(file);
        execPasso2(file);
    }

    private void execPasso1(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (line.charAt(0) != ' ') {
                    // Declaracao de simbolo
                    if (split.length > 1) {
                        tabSimb.put(split[0], Util.intToWord(ic));
                        ic += 2;
                    }
                    // Declaracao de label
                    else {
                        ic += 2;
                        tabSimb.put(split[0], Util.intToWord(ic));
                    }
                } else {
                    switch (split[1]) {
                        case "@":
                            ic = Util.wordToInt(split[2]);
                            byteCounter = ic;
                            break;
                        case "#":
                            // termina passo
                            break;
                        default:   // Uma das 16 instrucoes de 2 bytes
                            if (tabMne.get(split[1]) != null)
                                ic += tabMne.get(split[1]);
                            else
                                System.out.println("Error: " + split[2] + " is not a valid instruction.");
                            break;
                    }
                }
            }
            byteCounter = ic - byteCounter;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execPasso2(File file) throws FileNotFoundException {
        File output = new File("objAbs.txt");
        PrintWriter writer = new PrintWriter(output);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line, icString;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (line.charAt(0) == ' ') {
                    switch (split[1]) {
                        case "@":
                            ic = Util.wordToInt(split[2]);
                            icString = Integer.toHexString(ic);
                            while (icString.length() < 4) {
                                icString = "0".concat(icString);
                            }
                            String byteCounterString = Integer.toHexString(byteCounter + 3);
                            while (byteCounterString.length() < 2) {
                                byteCounterString = "0".concat(byteCounterString);
                            }
                            writer.write(Util.intToWord(ic) + Util.intToWord(byteCounter).substring(2));
                            break;
                        case "#":
                            String inicExec = tabSimb.get(split[2]);
                            writer.write(inicExec);
                            break;
                        case "JP":
                            writer.write("00" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "JZ":
                            writer.write("01" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "JN":
                            writer.write("02" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "LV":
                            writer.write("03" + split[2]);
                            ic += 3;
                            break;
                        case "+":
                            writer.write("04" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "-":
                            writer.write("05" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "*":
                            writer.write("06" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "/":
                            writer.write("07" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "LD":
                            writer.write("08" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "MM":
                            writer.write("09" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "SC":
                            writer.write("0a" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "RS":
                            writer.write("0b" + tabSimb.get(split[2]));
                            ic += 3;
                            break;
                        case "HM":
                            writer.write("0c0000");
                            ic += 3;
                            break;
                        case "GD":
                            writer.write("0d" + split[2]);
                            ic += 3;
                            break;
                        case "PD":
                            writer.write("0e" + split[2]);
                            ic += 3;
                            break;
                        case "SO":
                            writer.write("0f" + split[2]);
                            ic += 3;
                            break;
                    }
                } else if (split.length > 1) {
                    if (split[1].equals("K")) {
                        while (split[2].length() < 4)
                            split[2] = "0".concat(split[2]);
                        writer.print(split[2]);
                        ic += 2;
                    }
                } else {
                    writer.print("0000");  // Reserva para endereco de retorno
                    ic += 2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
    }
}
