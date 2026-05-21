package org.kostlink.model;

import org.kostlink.Main;

public class Penghuni extends User {
    private String namaLengkap = "";
    private String nomorKamar = "-";
    private boolean statusAktif = false;
    private boolean isSudahBayar = false;
    private int tanggalSiklusKost = 1;

    public Penghuni(String username, String password) {
        super(username, password, "PENGHUNI");
    }

    @Override
    public void bukaDashboard() {
        Main.jalankanDashboardPenghuni(this);
    }

    // Getter & Setter Enkapsulasi
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }

    public boolean isStatusAktif() { return statusAktif; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }

    public boolean isSudahBayar() { return isSudahBayar; }
    public void setSudahBayar(boolean sudahBayar) { this.isSudahBayar = sudahBayar; }

    public int getTanggalSiklusKost() { return tanggalSiklusKost; }
    public void setTanggalSiklusKost(int tanggalSiklusKost) { this.tanggalSiklusKost = tanggalSiklusKost; }
}