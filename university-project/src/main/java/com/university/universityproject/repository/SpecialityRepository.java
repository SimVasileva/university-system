package com.university.universityproject.repository;

import com.university.universityproject.model.SpecialityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<SpecialityEntity,Long> {
    @Override
    Optional<SpecialityEntity> findById(Long id);
    Optional<SpecialityEntity> findByName(String name);
}
