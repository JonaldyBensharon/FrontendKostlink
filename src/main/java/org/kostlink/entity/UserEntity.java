package org.kostlink.entity;

import jakarta.persistence.*;

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
}