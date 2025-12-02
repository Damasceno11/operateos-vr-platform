package br.com.workdb.operateosbackend.infra.api;

import br.com.workdb.operateosbackend.core.domain.TrainingSession;
import br.com.workdb.operateosbackend.core.usecase.ProcessTelemetryUseCase;
import br.com.workdb.operateosbackend.core.usecase.dto.TelemetryInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/training")
@CrossOrigin(origins = "*") // Permite acesso do Unity/Angular
public class TelemetryController {

    private final ProcessTelemetryUseCase useCase;

    public TelemetryController(ProcessTelemetryUseCase useCase) {
        this.useCase = useCase;
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
}
