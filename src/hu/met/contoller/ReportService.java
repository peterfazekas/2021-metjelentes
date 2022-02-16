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
}
