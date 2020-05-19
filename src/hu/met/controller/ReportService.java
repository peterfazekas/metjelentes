package hu.met.controller;

import hu.met.domain.model.Report;
import hu.met.domain.model.ReportTime;
import hu.met.domain.service.FileWriter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {

    private static final String ENTER = "\r\n";

    private final List<Report> reports;
    private final FileWriter fileWriter;

    public ReportService(List<Report> reports, FileWriter fileWriter) {
        this.reports = reports;
        this.fileWriter = fileWriter;
    }

    /**
     * 2. feladat: Adja meg, hogy az adott városból mikor érkezett az utolsó mérési adat!
     * A kiírásban az időpontot óó:pp formátumban jelenítse meg!
     */
    public String getLastReportFromSettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.getSettlement().equals(settlement))
                .max(Comparator.comparing(Report::getReportTime))
                .map(Report::getReportTime)
                .map(ReportTime::toString)
                .get();
    }

    /**
     * 3. feladat: Határozza meg, hogy a nap során mikor mérték a legalacsonyabb és a legmagasabb hőmérsékletet!
     * Jelenítse meg a méréshez kapcsolódó település nevét, az időpontot és a hőmérsékletet!
     * Amennyiben több legnagyobb vagy legkisebb érték van, akkor elég az egyiket kiírnia.
     */
    public String getLowestTemperatureReport() {
        return reports.stream()
                .min(Comparator.comparing(Report::getTemperature))
                .map(Report::toString)
                .get();
    }

    public String getHighestTemperatureReport() {
        return reports.stream()
                .max(Comparator.comparing(Report::getTemperature))
                .map(Report::toString)
                .get();
    }

    /**
     * 4. feladat: Határozza meg, azokat a településeket és időpontokat, ahol és amikor a mérések idején szélcsend volt!
     * (A szélcsendet a táviratban 00000 kóddal jelölik.) Ha nem volt ilyen, akkor a „Nem volt szélcsend a mérések idején.”
     * szöveget írja ki! A kiírásnál a település kódját és az időpontot jelenítse meg.
     */
    public String getCalmReportDetails() {
        List<Report> calmReports = getCalmReports();
        return calmReports.size() > 0 ? printCalmReports(calmReports) : "Nem volt szélcsend a mérések idején.";
    }

    private String printCalmReports(List<Report> calmReports) {
        return calmReports.stream()
                .map(i -> i.getSettlement() + " " + i.getReportTime())
                .collect(Collectors.joining(ENTER));
    }

    private List<Report> getCalmReports() {
        return reports.stream()
                .filter(Report::isCalm)
                .collect(Collectors.toList());
    }

    /**
     * 5. feladat: Határozza meg a települések napi középhőmérsékleti adatát és a hőmérséklet-ingadozását!
     * A kiírásnál a település kódja szerepeljen a sor elején a minta szerint!
     * A kiírásnál csak a megoldott feladatrészre vonatkozó szöveget és értékeket írja ki!
     */

    public String getTemperaturesBySettlement() {
        return getSettlements().stream()
                .map(this::getTemperatureBySettlement)
                .collect(Collectors.joining(ENTER));
    }

    private String getTemperatureBySettlement(String settlement) {
        return String.format("%s %s; %s", settlement, getAverageTemperaturesBySettlement(settlement), getTemperatureFluctuationBySettlement(settlement));
    }

    /**
     * 5.a feladat: A középhőmérséklet azon hőmérsékleti adatok átlaga, amikor a méréshez tartozó óra értéke 1., 7., 13., 19.
     * Ha egy településen a felsorolt órák valamelyikén nem volt mérés, akkor a kiírásnál az „NA” szót jelenítse meg!
     * Az adott órákhoz tartozó összes adat átlagaként határozza meg a középhőmérsékletet, azaz minden értéket azonos
     * súllyal vegyen figyelembe! A középhőmérsékletet egészre kerekítve jelenítse meg!
     */

    private String getAverageTemperaturesBySettlement(String settlement) {
        return hasAllReportTemperaturesBySettlement(settlement)
                ? String.format("Középhőmérséklet: %d", countAverageTemperaturesBySettlement(settlement))
                : "NA";
    }


    private boolean hasAllReportTemperaturesBySettlement(String settlement) {
        List<Integer> hours = getReportBySettlement(settlement).stream()
                .map(Report::getReportTime)
                .map(ReportTime::getHour)
                .distinct()
                .collect(Collectors.toList());
        return hours.containsAll(ReportTime.REPORT_HOURS);
    }

    private int countAverageTemperaturesBySettlement(String settlement) {
        List<Integer> temperaturesBySettlement = getReportTemperaturesBySettlement(settlement);
        int sum = temperaturesBySettlement.stream().mapToInt(i -> i).sum();
        int count = temperaturesBySettlement.size();
        return Math.round((float) sum / count);
    }

    private List<Report> getReportBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.getSettlement().equals(settlement))
                .filter(Report::isReportHour)
                .collect(Collectors.toList());
    }

    private List<Integer> getReportTemperaturesBySettlement(String settlement) {
        return getReportBySettlement(settlement).stream()
                .map(Report::getTemperature)
                .collect(Collectors.toList());
    }

    /**
     * 5.b feladat: A hőmérséklet-ingadozás számításhoz az adott településen a napi
     * legmagasabb és legalacsonyabb hőmérséklet különbségét kell kiszámítania!
     * (Feltételezheti, hogy minden település esetén volt legalább két mérési adat.)
     */

    private String getTemperatureFluctuationBySettlement(String settlement) {
        int temperatureFluctuation = getHighestTemperatureBySettlement(settlement) - getLowestTemperatureBySettlement(settlement);
        return String.format("Hőmérséklet-ingadozás: %d", temperatureFluctuation);
    }

    private int getLowestTemperatureBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.getSettlement().equals(settlement))
                .map(Report::getTemperature)
                .min(Integer::compareTo)
                .get();
    }

    private int getHighestTemperatureBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.getSettlement().equals(settlement))
                .map(Report::getTemperature)
                .max(Integer::compareTo)
                .get();
    }

    /**
     * 6. feladat: Hozzon létre településenként egy szöveges állományt, amely első sorában a település kódját tartalmazza!
     * A további sorokban a mérési időpontok és a hozzá tartozó szélerősségek jelenjenek meg!
     * A szélerősséget a minta szerint a számértéknek megfelelő számú ettőskereszttel (#) adja meg!
     * A fájlban az időpontokat és a szélerősséget megjelenítő kettőskereszteket szóközzel válassza el egymástól!
     * A fájl neve X.txt legyen, ahol az X helyére a település kódja kerüljön!
     */
    public String writeReportsBySettlements() {
        getSettlements().forEach(settlement -> {
            List<String> content = getReportDetailsBySettlement(settlement);
            content.add(0, settlement);
            fileWriter.write(createFileName(settlement), content);
        });
        return "A fájlok elkészültek.";
    }

    private List<String> getReportDetailsBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.getSettlement().equals(settlement))
                .map(Report::getReportDetail)
                .collect(Collectors.toList());
    }

    private String createFileName(String settlement) {
        return settlement + ".txt";
    }

    private List<String> getSettlements() {
        return reports.stream()
                .map(Report::getSettlement)
                .distinct()
                .collect(Collectors.toList());
    }

}
