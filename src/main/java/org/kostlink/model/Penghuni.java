package org.kostlink.model;

import org.kostlink.Main;
import java.time.LocalDate;

public class Penghuni extends User {
    private String namaLengkap = "";
    private String nomorKamar = "-";
    private boolean statusAktif = false;
    private int tanggalSiklusKost = 1;

    // =========================================================================
    // STATUS PEMBAYARAN 3 TAHAP:
    // "BELUM_BAYAR"          → User belum melakukan pembayaran
    // "MENUNGGU_VERIFIKASI"  → User sudah bayar, menunggu konfirmasi admin
    // "LUNAS"                → Admin sudah mengonfirmasi pembayaran valid
    // =========================================================================
    private String statusPembayaran = "BELUM_BAYAR";

    // Tanggal saat user mengirimkan bukti pembayaran
    private LocalDate tanggalKirimBukti = null;

    // Tanggal saat admin mengonfirmasi pembayaran (digunakan untuk hitung jatuh tempo +30 hari)
    private LocalDate tanggalKonfirmasiAdmin = null;

    // Path file bukti pembayaran yang di-upload oleh user
    private String buktiPembayaranPath = null;

    public Penghuni(String username, String password) {
        super(username, password, Role.PENGHUNI);
    }

    @Override
    public void bukaDashboard() {
        Main.jalankanDashboardPenghuni(this);
    }

    // Getter & Setter Enkapsulasi
    public String getNamaLengkap() { return namaLengkap; }

    public void setNamaLengkap(String namaLengkap) {
        if (namaLengkap != null) {
            this.namaLengkap = namaLengkap.trim();
        }
    }

    public String getNomorKamar() { return nomorKamar; }

    public void setNomorKamar(String nomorKamar) {
        if (nomorKamar != null) {
            this.nomorKamar = nomorKamar.trim();
        }
    }

    public boolean isStatusAktif() { return statusAktif; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }

    public int getTanggalSiklusKost() { return tanggalSiklusKost; }

    public void setTanggalSiklusKost(int tanggalSiklusKost) {
        if (tanggalSiklusKost >= 1 && tanggalSiklusKost <= 31) {
            this.tanggalSiklusKost = tanggalSiklusKost;
        }
    }
    // --- STATUS PEMBAYARAN ---
    public String getStatusPembayaran() { return statusPembayaran; }

    public void setStatusPembayaran(String statusPembayaran) {
        if (statusPembayaran == null) return;

        switch (statusPembayaran) {
            case "BELUM_BAYAR":
            case "MENUNGGU_VERIFIKASI":
            case "LUNAS":
                this.statusPembayaran = statusPembayaran;
                break;
        }
    }

    public LocalDate getTanggalKirimBukti() { return tanggalKirimBukti; }
    public void setTanggalKirimBukti(LocalDate tanggalKirimBukti) { this.tanggalKirimBukti = tanggalKirimBukti; }

    public LocalDate getTanggalKonfirmasiAdmin() { return tanggalKonfirmasiAdmin; }
    public void setTanggalKonfirmasiAdmin(LocalDate tanggalKonfirmasiAdmin) { this.tanggalKonfirmasiAdmin = tanggalKonfirmasiAdmin; }

    public String getBuktiPembayaranPath() { return buktiPembayaranPath; }
    public void setBuktiPembayaranPath(String buktiPembayaranPath) { this.buktiPembayaranPath = buktiPembayaranPath; }

    // Backward compatibility helper methods
    public boolean isSudahBayar() { return "LUNAS".equals(statusPembayaran); }
    public void setSudahBayar(boolean sudahBayar) {
        if (sudahBayar) {
            this.statusPembayaran = "LUNAS";
        } else {
            this.statusPembayaran = "BELUM_BAYAR";
        }
    }
}