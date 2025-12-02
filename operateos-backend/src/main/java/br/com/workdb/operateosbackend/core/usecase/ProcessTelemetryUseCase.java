package br.com.workdb.operateosbackend.core.usecase;

import br.com.workdb.operateosbackend.core.domain.TrainingSession;
import br.com.workdb.operateosbackend.core.usecase.dto.TelemetryInput;
import br.com.workdb.operateosbackend.infra.persistence.TrainingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProcessTelemetryUseCase {

    private final TrainingRepository repository;

    public ProcessTelemetryUseCase(TrainingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TrainingSession execute(TelemetryInput input) {
        // 1. Recupera o estado atual (Stateless Service handling)
        TrainingSession session = repository.findById(input.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada: " + input.sessionId()));

        // 2. Delega a regra de negócio para o Domínio
        session.processTelemetry(input.speed(), input.collision());

        // 3. Persiste o novo estado
        return repository.save(session);
    }

    @Transactional
    public TrainingSession startNewSession(String operatorId) {
        TrainingSession session = TrainingSession.start(operatorId);
        return repository.save(session);
    }
}
