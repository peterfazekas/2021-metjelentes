package hu.met;

import hu.met.contoller.ReportService;
import hu.met.model.service.*;

import java.util.Scanner;

public class App {

    private final ReportService service;
    private final Console console;

    private App() {
        DataApi dataApi = new DataApi(new FileReader(), new DataParser());
        service = new ReportService(dataApi.getData("tavirathu13.txt"),
                new FileWriter());
        console = new Console(new Scanner(System.in));
    }

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        System.out.println("2. feladat");
        System.out.print("Adja meg egy település kódját! Település: ");
        String settlement = console.read();
    }
}
