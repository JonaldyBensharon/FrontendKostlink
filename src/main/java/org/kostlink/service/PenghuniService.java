package org.kostlink.service;

import org.kostlink.model.Penghuni;
import org.kostlink.repository.JPAUserRepository;
import org.kostlink.repository.UserRepository;

import java.time.LocalDate;

public class PenghuniService {

    private static final PenghuniService instance =
            new PenghuniService();

    private final UserRepository userRepository = new JPAUserRepository();

    private PenghuniService() {}

    public static PenghuniService getInstance() {
        return instance;
    }

    // FORMULIR untuk aktivasi akun
    public void lengkapiData(
            Penghuni penghuni,
            String namaLengkap,
            String nomorKamar
    ) {
        if (penghuni == null) return;

        if (namaLengkap == null || namaLengkap.trim().isEmpty()) return;
        if (nomorKamar == null || nomorKamar.trim().isEmpty()) return;

        // CEK APAKAH NOMOR KAMAR SUDAH DIPAKAI
        boolean kamarSudahDipakai = userRepository.findAll().stream()
                .filter(user -> user instanceof Penghuni)
                .map(user -> (Penghuni) user)
                .anyMatch(p ->
                        nomorKamar.trim().equals(p.getNomorKamar())
                                && !p.getUsername().equals(penghuni.getUsername())
                );

        if (kamarSudahDipakai) {
            throw new RuntimeException("Nomor kamar sudah digunakan!");
        }

        penghuni.setNamaLengkap(namaLengkap.trim());
        penghuni.setNomorKamar(nomorKamar.trim());
        penghuni.setStatusAktif(true);
        penghuni.setTanggalSiklusKost(
                Math.min(LocalDate.now().getDayOfMonth(), 28)
        );

        userRepository.save(penghuni);
    }

    // PEMBAYARAN
    public void kirimBukti(Penghuni penghuni, String buktiPath) {
        if (penghuni == null) return;

        if (buktiPath == null || buktiPath.trim().isEmpty()) return;

        penghuni.setStatusPembayaran("MENUNGGU_VERIFIKASI");
        penghuni.setTanggalKirimBukti(LocalDate.now());
        penghuni.setBuktiPembayaranPath(buktiPath.trim());
        userRepository.save(penghuni);
    }

    public void konfirmasiPembayaran(Penghuni penghuni) {
        if (penghuni == null) return;

        penghuni.setStatusPembayaran("LUNAS");
        penghuni.setTanggalKonfirmasiAdmin(LocalDate.now());
        userRepository.save(penghuni);
    }

    public void tolakPembayaran(Penghuni penghuni) {
        if (penghuni == null) return;

        penghuni.setStatusPembayaran("BELUM_BAYAR");
        penghuni.setTanggalKirimBukti(null);
        penghuni.setTanggalKonfirmasiAdmin(null);
        penghuni.setBuktiPembayaranPath(null);
        userRepository.save(penghuni);
    }

    public void resetPembayaran(Penghuni penghuni) {
        if (penghuni == null) return;

        penghuni.setStatusPembayaran("BELUM_BAYAR");
        penghuni.setTanggalKirimBukti(null);
        penghuni.setTanggalKonfirmasiAdmin(null);
        penghuni.setBuktiPembayaranPath(null);
        userRepository.save(penghuni);
    }

    // QUERY ke database
    public String getStatusPembayaran(Penghuni penghuni) {
        return penghuni != null
                ? penghuni.getStatusPembayaran()
                : "BELUM_BAYAR";
    }

    public LocalDate getTanggalKirimBukti(Penghuni penghuni) {
        return penghuni != null
                ? penghuni.getTanggalKirimBukti()
                : null;
    }

    public LocalDate getTanggalKonfirmasi(Penghuni penghuni) {
        return penghuni != null
                ? penghuni.getTanggalKonfirmasiAdmin()
                : null;
    }

    public String getBuktiPath(Penghuni penghuni) {
        return penghuni != null
                ? penghuni.getBuktiPembayaranPath()
                : null;
    }
}