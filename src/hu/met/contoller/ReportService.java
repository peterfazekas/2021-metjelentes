package hu.met.contoller;

import hu.met.model.domain.Report;
import hu.met.model.domain.ReportTime;
import hu.met.model.service.FileWriter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportService {

    private final List<Report> reports;
    private final FileWriter fileWriter;

    public ReportService(List<Report> reports, FileWriter fileWriter) {
        this.reports = reports;
        this.fileWriter = fileWriter;
    }

    /**
     * 2. feladat
     */
    public String getLastReportTimeFromSettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.isSettlement(settlement))
                .max(Comparator.comparing(Report::getReportTime))
                .map(Report::getReportTime)
                .map(ReportTime::toString)
                .get();
    }

    /**
     * 3. feladat
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
     * 4. feladat
     */
    public String getCalmReportDetails() {
        List<Report> calmReports = getCalmReports();
        return calmReports.isEmpty()
                ? "Nem volt szélcsend a mérések idején."
                : printCalmReports(calmReports);
    }

    private String printCalmReports(List<Report> calmReports) {
        return calmReports.stream()
                .map(Report::getSettlementWithReportTime)
                .collect(Collectors.joining("\n"));
    }

    private List<Report> getCalmReports() {
        return reports.stream()
                .filter(Report::isCalm)
                .collect(Collectors.toList());
    }

    /**
     * 5. feladat
     */
    public String getTemperaturesBySettlement() {
        return getSettlements()
                .map(this::getTemperatureBySettlement)
                .collect(Collectors.joining("\n"));
    }

    private String getTemperatureBySettlement(String settlement) {
        return String.format("%s %s; %s",
                settlement,
                getAverageTemperatureBySettlement(settlement),
                getTemperatureFluctuationBySettlement(settlement));
    }

    /**
     * 5.a
     */
    private String getAverageTemperatureBySettlement(String settlement) {
        return hasAllReportsTemperatureBySettlement(settlement)
                ? String.format("Középhőmérséklet: %d", countAverageTemperatureBySettlement(settlement))
                : "NA";
    }

    private boolean hasAllReportsTemperatureBySettlement(String settlement) {
        return ReportTime.REPORT_HOURS.size() == getReportsBySettlement(settlement)
                .map(Report::getReportTime)
                .map(ReportTime::getHour)
                .distinct()
                .count();
    }

    private long countAverageTemperatureBySettlement(String settlement) {
        return Math.round(getReportsBySettlement(settlement)
                .mapToInt(Report::getTemperature)
                .average()
                .getAsDouble());
    }

    private Stream<Report> getReportsBySettlement(String settlement) {
        return reports.stream()
                .filter(report -> report.isSettlement(settlement))
                .filter(Report::isReportHour);
    }

    /**
     * 5.b
     */
    private String getTemperatureFluctuationBySettlement(String settlement) {
        int temperatureFluctuation =
                getHighestTemperatureBySettlement(settlement) - getLowestTemperatureBySettlement(settlement);
        return String.format("Hőmérséklet-ingadozás: %d", temperatureFluctuation);
    }

    private int getLowestTemperatureBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.isSettlement(settlement))
                .mapToInt(Report::getTemperature)
                .min()
                .getAsInt();
    }

    private int getHighestTemperatureBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.isSettlement(settlement))
                .mapToInt(Report::getTemperature)
                .max()
                .getAsInt();
    }

    /**
     * 6. feladat
     */
    public String writeWindReportsBySettlements() {
        getSettlements().forEach(settlement -> {
            List<String> report = getWindReportDetailsBySettlement(settlement);
            report.add(0, settlement);
            fileWriter.write(createFilename(settlement), report);
        });
        return "A fájlok elkészültek.";
    }

    private List<String> getWindReportDetailsBySettlement(String settlement) {
        return reports.stream()
                .filter(i -> i.isSettlement(settlement))
                .map(Report::getWindForceByTime)
                .collect(Collectors.toList());
    }

    private String createFilename(String settlement) {
        return settlement + ".txt";
    }

    private Stream<String> getSettlements() {
        return reports.stream()
                .map(Report::getSettlement)
                .distinct();
    }
}
