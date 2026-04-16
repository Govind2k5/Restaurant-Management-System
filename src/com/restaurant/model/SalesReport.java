package com.restaurant.model;

import java.util.Date;

/**
 * SALES REPORT MODEL - Matches class diagram: SalesReport class
 */
public class SalesReport {
    private int reportId;
    private double totalSales;
    private Date date;

    public SalesReport() { this.date = new Date(); }

    public SalesReport(int reportId, double totalSales, Date date) {
        this.reportId = reportId;
        this.totalSales = totalSales;
        this.date = date;
    }

    public SalesReport generateReport() {
        System.out.println("[SalesReport] Generating report for date: " + date);
        return this;
    }

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public double getTotalSales() { return totalSales; }
    public void setTotalSales(double totalSales) { this.totalSales = totalSales; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
