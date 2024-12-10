package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

public class FinsetPressureCDRequest {
    private Double finArea;
    private Double mach;
    private String crossSection;
    private Double cosGammaLead;
    private Double span;
    private Double thickness;
    private Long timestamp;
    public static ArrayList client_cn = new ArrayList();
    public static ArrayList server_cn = new ArrayList();


    private Double refArea;

    public Double getFinArea() {
        return finArea;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static ArrayList<Double> getClient_cn() {
        return client_cn;
    }

    public static void setClient_cn(ArrayList<Double> client_cn) {
        FinsetPressureCDRequest.client_cn = client_cn;
    }

    public static ArrayList<Double> getServer_cn() {
        return server_cn;
    }

    public static void setServer_cn(ArrayList<Double> server_cn) {
        FinsetPressureCDRequest.server_cn = server_cn;
    }

    public void setFinArea(Double finArea) {
        this.finArea = finArea;
    }

    public Double getMach() {
        return mach;
    }

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public String getCrossSection() {
        return crossSection;
    }

    public void setCrossSection(String crossSection) {
        this.crossSection = crossSection;
    }

    public Double getCosGammaLead() {
        return cosGammaLead;
    }

    public void setCosGammaLead(Double cosGammaLead) {
        this.cosGammaLead = cosGammaLead;
    }

    public Double getSpan() {
        return span;
    }

    public void setSpan(Double span) {
        this.span = span;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    public Double getRefArea() {
        return refArea;
    }

    public void setRefArea(Double refArea) {
        this.refArea = refArea;
    }
}
