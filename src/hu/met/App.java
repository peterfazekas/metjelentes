package hu.met;

import hu.met.controller.ReportService;
import hu.met.domain.service.*;

import java.util.Scanner;

public class App {

    private final ReportService reportService;
    private final Console console;

    private App() {
        console = new Console(new Scanner(System.in));
        DataApi dataApi = new DataApi(new FileReader(), new DataParser());
        reportService = new ReportService(dataApi.getData("tavirathu13.txt"), new FileWriter());
    }

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        System.out.println("2. feladat:");
        System.out.print("Adja meg egy település kódját! Település: ");
        String settlement = console.read();
        System.out.println("Az utolsó mérési adat a megadott településről "
                + reportService.getLastReportFromSettlement(settlement) + "-kor érkezett.");
        System.out.println("3. feladat:");
        System.out.println("A legalacsonyabb hőmérséklet: " + reportService.getLowestTemperatureReport());
        System.out.println("A legmagasabb hőmérséklet: " + reportService.getHighestTemperatureReport());
        System.out.println("4. feladat");
        System.out.println(reportService.getCalmReportDetails());
        System.out.println("5. feladat");
        System.out.println(reportService.getTemperaturesBySettlement());
        System.out.println("6. feladat");
        System.out.println(reportService.writeReportsBySettlements());
    }

}
