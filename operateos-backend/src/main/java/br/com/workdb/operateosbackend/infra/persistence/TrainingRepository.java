package br.com.workdb.operateosbackend.infra.persistence;

import br.com.workdb.operateosbackend.core.domain.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingSession, String> {}
