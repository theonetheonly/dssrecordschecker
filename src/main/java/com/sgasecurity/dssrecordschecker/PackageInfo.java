package com.sgasecurity.dssrecordschecker;

public class PackageInfo {
    private String vat;
    private String deposit;
    private String packageName;

    public PackageInfo() {}

    public PackageInfo(String vat, String deposit, String packageName) {
        this.vat = vat;
        this.deposit = deposit;
        this.packageName = packageName;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
