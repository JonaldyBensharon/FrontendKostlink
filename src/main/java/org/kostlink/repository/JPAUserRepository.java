package org.kostlink.repository;

import jakarta.persistence.EntityManager;
import org.kostlink.config.JPAUtil;
import org.kostlink.entity.UserEntity;
import org.kostlink.model.PemilikKos;
import org.kostlink.model.Penghuni;
import org.kostlink.model.User;
import org.kostlink.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JPAUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            UserEntity entity = toEntity(user);
            em.merge(entity);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            UserEntity entity = em.find(UserEntity.class, username);

            if (entity == null) {
                return Optional.empty();
            }

            return Optional.of(toDomain(entity));
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "FROM UserEntity",
                            UserEntity.class
                    )
                    .getResultList()
                    .stream()
                    .map(this::toDomain)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public void delete(String username) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            UserEntity entity =
                    em.find(UserEntity.class, username);

            if (entity != null) {
                em.remove(entity);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private UserEntity toEntity(User user) {

        if (user.getRole() == null) {
            throw new IllegalStateException("User role tidak boleh null: " + user.getUsername());
        }

        UserEntity entity = new UserEntity(
                user.getUsername(),
                user.getPassword(),
                user.getRole().name()
        );

        if (user instanceof Penghuni p) {
            entity.setNamaLengkap(p.getNamaLengkap());
            entity.setNomorKamar(p.getNomorKamar());
            entity.setStatusAktif(p.isStatusAktif());
            entity.setTanggalSiklusKost(
                    p.getTanggalSiklusKost()
            );
        }

        return entity;
    }

    private User toDomain(UserEntity entity) {

        Role role;
        try {
            role = Role.valueOf(entity.getRole());
        } catch (Exception e) {
            role = Role.PENGHUNI; // fallback aman
        }

        if (role == Role.PEMILIK_KOST) {
            return new PemilikKos(entity.getUsername(), entity.getPassword());
        }

        Penghuni p = new Penghuni(
                entity.getUsername(),
                entity.getPassword()
        );

        p.setNamaLengkap(entity.getNamaLengkap());
        p.setNomorKamar(entity.getNomorKamar());

        if (entity.getStatusAktif() != null) {
            p.setStatusAktif(entity.getStatusAktif());
        }

        if (entity.getTanggalSiklusKost() != null) {
            p.setTanggalSiklusKost(entity.getTanggalSiklusKost());
        }

        return p;
    }
}