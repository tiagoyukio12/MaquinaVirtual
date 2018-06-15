package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Memory memory = new Memory();

        // Login
        System.out.println("Available users: ");
        File file = new File(".\\accounts");
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        System.out.println(Arrays.toString(directories));
        System.out.println("Choose an username: ");
        Scanner scanIn = new Scanner(System.in);
        String userName = scanIn.nextLine();
        System.out.println(userName + " logged in successfully");

        // Terminal
        label:
        while (true) {
            System.out.println("Type a command: ");
            System.out.print("$");
            String input = scanIn.nextLine();
            String[] cmd = input.split(" ");
            switch (cmd[0]) {
                case "DIR":
                    System.out.println("Available programs: ");
                    file = new File(".\\accounts\\" + userName);
                    String[] programs = file.list((current, name) -> new File(current, name).isFile() && !name.startsWith("DEL_"));
                    System.out.println(Arrays.toString(programs));
                    break;
                case "DEL":
                    file = new File(".\\accounts\\" + userName + "\\" + cmd[1]);
                    boolean success = file.renameTo(new File(".\\accounts\\" + userName + "\\DEL_" + cmd[1]));
                    if (success)
                        System.out.println(cmd[1] + " deleted successfully");
                    break;
                case "RUN":
                    Assembler assembler = new Assembler();
                    assembler.gerarCodObj(".\\accounts\\" + userName + "\\" + cmd[1]);
                    memory.run(2);
                    break;
                case "END":
                    memory.logOff();
                    break label;
            }
        }
        scanIn.close();
    }
}
