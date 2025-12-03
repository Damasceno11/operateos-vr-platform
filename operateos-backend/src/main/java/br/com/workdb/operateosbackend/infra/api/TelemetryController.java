package br.com.workdb.operateosbackend.infra.api;

import br.com.workdb.operateosbackend.core.domain.TrainingSession;
import br.com.workdb.operateosbackend.core.usecase.ProcessTelemetryUseCase;
import br.com.workdb.operateosbackend.core.usecase.dto.TelemetryInput;
import br.com.workdb.operateosbackend.infra.persistence.TrainingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training")
public class TelemetryController {

    private final ProcessTelemetryUseCase useCase;
    private final TrainingRepository repository;

    private static final TrainingSession WAITING_SESSION = TrainingSession.start("AGUARDANDO_CONEXAO");

    public TelemetryController(ProcessTelemetryUseCase useCase, TrainingRepository repository) {
        this.useCase = useCase;
        this.repository = repository;
    }

    @PostMapping("/start/{operatorId}")
    public ResponseEntity<TrainingSession> startSession(@PathVariable String operatorId) {
        return ResponseEntity.ok(useCase.startNewSession(operatorId));
    }

    @PostMapping("/telemetry")
    public ResponseEntity<TrainingSession> receiveTelemetry(@RequestBody TelemetryInput input) {
        TrainingSession updatedSession = useCase.execute(input);
        return ResponseEntity.ok(updatedSession);
    }

    @GetMapping("/latest")
    public ResponseEntity<TrainingSession> getLatestSession() {
        List<TrainingSession> all = repository.findAll();

        return repository.findFirstByOrderByCreatedAtDesc()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(WAITING_SESSION));
    }
}
