package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        Memory memory = new Memory();

        // Login
        System.out.println("Usuarios disponiveis: ");
        // TODO: Mostrar pastas de usuarios disponiveis
        try (Stream<Path> paths = Files.walk(Paths.get(".\\accounts"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Digite o nome do usuario: ");
        Scanner scanIn = new Scanner(System.in);
        String userName = scanIn.nextLine();
        System.out.println(userName);

        // Terminal
        label:
        while (true) {
            System.out.println("Digite um comando: ");
            System.out.print("$");
            String input = scanIn.nextLine();
            String[] cmd = input.split(" ");
            switch (cmd[0]) {
                case "DIR":
                    break label;
                case "DEL":
                    break label;
                case "RUN":
                    Assembler assembler = new Assembler();
                    assembler.gerarCodObj(".\\accounts\\" + userName + "\\" + cmd[1]);
                    memory.run(2);
                    break;
                case "END":
                    break label;
            }
        }
        scanIn.close();
    }
}
