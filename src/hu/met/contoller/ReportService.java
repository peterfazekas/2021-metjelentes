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

}
