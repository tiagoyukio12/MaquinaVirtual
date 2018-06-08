package com.company;

class Memoria {
    private char[] memory;
    private int pc;

    Memoria() {
        memory = new char[0x10000]; // enderecos de 0 a ffff
        pc = 0;
    }

    private void modify(int addr, String byteData) {
        memory[addr] = byteData.charAt(0);
        memory[addr + 1] = byteData.charAt(1);
    }

    private String read(int addr) {
        return Character.toString(memory[addr]).concat(Character.toString(memory[addr + 1]));
    }
    void runTest() {
        modify(0, "03");
        modify(2, "ff");
        modify(4, "0e");
        modify(6, "0f");
    }

    void run(int addr) {
        int ic = addr;
        int ac = 0;
        while (true) {
            String co = read(ic);
            ic += 2;
            switch (co) {
                case "03":
                    String op = read(ic);
                    ic += 2;
                    ac = Integer.parseInt(op, 16);
                    break;
                case "0e":
                    System.out.println(ac);
                    break;
                case "0f":
                    return;
            }
        }
    }
}
