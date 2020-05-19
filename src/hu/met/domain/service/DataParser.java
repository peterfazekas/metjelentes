package hu.met.domain.service;

import hu.met.domain.model.Report;
import hu.met.domain.model.ReportTime;
import hu.met.domain.model.Wind;

import java.util.List;
import java.util.stream.Collectors;

public class DataParser {

    public List<Report> parse(List<String> lines) {
        return lines.stream()
                .map(this::createReport)
                .collect(Collectors.toList());
    }

    private Report createReport(String line) {
        String[] items = line.split(" ");
        String settlement = items[0];
        String time = items[1];
        ReportTime reportTime = new ReportTime(getValue(time.substring(0, 2)), getValue(time.substring(2)));
        String windDetails = items[2];
        Wind wind = new Wind(windDetails.substring(0, 3), getValue(windDetails.substring(3)));
        int temperature = getValue(items[3]);
        return new Report(settlement, reportTime, wind, temperature);
    }

    private int getValue(String text) {
        return Integer.parseInt(text);
    }
}
