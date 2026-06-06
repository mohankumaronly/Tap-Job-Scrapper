package com.job.jobalerts.preferences.repository;

import com.job.jobalerts.auth.entity.User;
import com.job.jobalerts.preferences.entity.JobPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPreferenceRepository extends JpaRepository<JobPreference, Long> {
    Optional<JobPreference> findByUser(User user);
    List<JobPreference> findByActiveTrue();
    List<JobPreference> findByLocationContainingIgnoreCaseAndMinimumPackageLessThanEqual(String location, Double packageLpa);
}