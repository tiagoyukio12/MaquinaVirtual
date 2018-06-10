package com.company;

class Memoria {
    private String[] memory;
    private int pc;

    Memoria() {
        memory = new String[0x10000]; // enderecos de 0 a ffff
        pc = 0;
        String sLoader = "0d000006003f0900410d00000400410e00000900410d00000900430d00000e000108004305004501002d00001b0d000006003f0900410d00000400410f00000100000000000001";
        int i = 0;
        while (!sLoader.isEmpty()) {
            memory[i++] = sLoader.substring(0, 2);
            sLoader = sLoader.substring(2);
        }
    }

    private void modify(int addr, String byteData) {
        memory[addr] = byteData;
    }

    private String read(int addr) {
        return memory[addr];
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
