package virtualMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Memory {
    private String[] memory;
    private int ac;
    private int ic;

    Memory() {
        memory = new String[0x10000]; // enderecos de 0 a ffff
        String sLoader = "00000d000006004e0900500d00000400500e00000900500d000009005200002200000d00000e000108005205005409005201003900002200000300000e00000d00000e00010d00000e00010b00020100000000000001";
        int i = 0;
        while (!sLoader.isEmpty()) {
            memory[i++] = sLoader.substring(0, 2);
            sLoader = sLoader.substring(2);
        }
    }

    private void modify(int addr, String byteData) {
        addr = Util.signedToUnsigned(addr);
        if (addr > 0x10000) {
            System.out.println("Error: Address " + addr + " is outside memory");
            System.out.println("ac = " + ac);
            System.out.println("ic = " + ic);
        } else if (byteData.length() != 2) {
            System.out.println("Error: Data " + byteData + " is not a byte");
            System.out.println("ac = " + ac);
            System.out.println("ic = " + ic);
        }
        memory[addr] = byteData;
    }

    private String read(int addr) {
        addr = Util.signedToUnsigned(addr);
        if (addr >= 0x10000) {
            System.out.println("Error: Address " + addr + " is outside memory");
            return "00";
        }
        return memory[addr];
    }

    private String readWord(int addr) {
        addr = Util.signedToUnsigned(addr);
        if (addr >= 0x10000 - 1) {
            System.out.println("Error: Address " + addr + " is outside memory");
            return "0000";
        }
        return memory[addr].concat(memory[addr + 1]);
    }

    void run(int addr) throws IOException {
        ic = addr;
        ac = 0;
        int pc = 0;  // ponteiro utilizado por PD
        BufferedReader br = new BufferedReader(new FileReader("objAbs.txt"));
        while (true) {
            String co = read(ic);
            int op = Util.wordToInt(read(ic + 1).concat(read(ic + 2)));
            ic += 3;
            switch (co) {
                case "00":  // JP
                    ic = op;
                    break;
                case "01":  // JZ
                    if (ac == 0)
                        ic = op;
                    break;
                case "02":  // JN
                    if (ac < 0)
                        ic = op;
                    break;
                case "03":  // LV
                    ac = op;
                    break;
                case "04":  // +
                    ac += Util.wordToInt(readWord(op));
                    break;
                case "05":  // -
                    ac -= Util.wordToInt(readWord(op));
                    break;
                case "06":  // *
                    ac *= Util.wordToInt(readWord(op));
                    break;
                case "07":  // /
                    ac /= Util.wordToInt(readWord(op));
                    break;
                case "08":  // LD
                    ac = Util.wordToInt(readWord(op));
                    break;
                case "09":  // MM
                    modify(op, Util.intToWord(ac).substring(0, 2));
                    modify(op + 1, Util.intToWord(ac).substring(2));
                    break;
                case "0a":  // SC;
                    modify(op - 2, Util.intToWord(ic).substring(0, 2));
                    modify(op - 1, Util.intToWord(ic).substring(2));
                    ic = op;
                    break;
                case "0b":  // RS
                    ic = Util.wordToInt(readWord(op - 2));
                    break;
                case "0c":  // HM
                    // TODO: parar (?)
                    ic = op;
                    break;
                case "0d":  // GD
                    if (op == 0) {
                        String readByte = Character.toString((char) br.read());
                        readByte = readByte.concat(Character.toString((char) br.read()));
                        ac = Util.wordToInt(readByte);
                    } else if (op == 1) {
                        Scanner scanIn = new Scanner(System.in);
                        String input = scanIn.nextLine();
                        ac = Util.wordToInt(input);
                    }
                    break;
                case "0e":  // PD
                    if (op == 0) {
                        pc = ac;
                    } else if (op == 1) {
                        modify(pc++, Util.intToWord(ac).substring(2));
                    } else if (op == 2) {
                        System.out.println("ACC: " + Util.intToWord(ac));
                    }
                    break;
                case "0f":  // SO
                    br.close();
                    if (op == 1)
                        run(ac);
                    return;
            }
        }
    }

    void logOff() {
        System.out.println("Value of registers: ");
        System.out.println("ACC: " + Util.intToWord(ac));
        System.out.println("IC: " + Util.intToWord(ic));
        System.out.println("Turning off Virtual Machine...");
    }
}
