package hu.met.domain.model;

public class Report {

    private final String settlement;
    private final ReportTime reportTime;
    private final Wind wind;
    private final int temperature;

    public Report(String settlement, ReportTime reportTime, Wind wind, int temperature) {
        this.settlement = settlement;
        this.reportTime = reportTime;
        this.wind = wind;
        this.temperature = temperature;
    }

    public String getSettlement() {
        return settlement;
    }

    public boolean isSettlement(String settlement) {
        return this.settlement.equals(settlement);
    }

    public ReportTime getReportTime() {
        return reportTime;
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean isCalm() {
        return wind.isCalm();
    }

    public boolean isReportHour() {
        return reportTime.isReportHour();
    }

    public String getReportDetail() {
        return reportTime + " " + wind.getWindForce();
    }

    public String printReportTime() {
        return settlement + " " + reportTime;
    }

        @Override
    public String toString() {
        return settlement + " " + reportTime + " " + temperature + " fok";
    }
}
