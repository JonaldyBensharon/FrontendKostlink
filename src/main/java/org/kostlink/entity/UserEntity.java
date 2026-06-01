package org.kostlink.entity;

import jakarta.persistence.*;
import org.kostlink.model.Role;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private String namaLengkap;
    private String nomorKamar;
    private Boolean statusAktif;
    private Integer tanggalSiklusKost;

    // ===== FIELD PEMBAYARAN =====
    private String statusPembayaran;
    private LocalDate tanggalKirimBukti;
    private LocalDate tanggalKonfirmasiAdmin;
    private String buktiPembayaranPath;

    public UserEntity() {
    }

    public UserEntity(
            String username,
            String password,
            String role
    ) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    // Penambahan konversi setelah adanya Role
    public Role getRoleAsEnum() {
        return Role.valueOf(role);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNomorKamar() {
        return nomorKamar;
    }

    public void setNomorKamar(String nomorKamar) {
        this.nomorKamar = nomorKamar;
    }

    public Boolean getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(Boolean statusAktif) {
        this.statusAktif = statusAktif;
    }

    public Integer getTanggalSiklusKost() {
        return tanggalSiklusKost;
    }

    public void setTanggalSiklusKost(Integer tanggalSiklusKost) {
        this.tanggalSiklusKost = tanggalSiklusKost;
    }

    // ===== GETTER & SETTER PEMBAYARAN =====
    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public LocalDate getTanggalKirimBukti() {
        return tanggalKirimBukti;
    }

    public void setTanggalKirimBukti(LocalDate tanggalKirimBukti) {
        this.tanggalKirimBukti = tanggalKirimBukti;
    }

    public LocalDate getTanggalKonfirmasiAdmin() {
        return tanggalKonfirmasiAdmin;
    }

    public void setTanggalKonfirmasiAdmin(LocalDate tanggalKonfirmasiAdmin) {
        this.tanggalKonfirmasiAdmin = tanggalKonfirmasiAdmin;
    }

    public String getBuktiPembayaranPath() {
        return buktiPembayaranPath;
    }

    public void setBuktiPembayaranPath(String buktiPembayaranPath) {
        this.buktiPembayaranPath = buktiPembayaranPath;
    }
}