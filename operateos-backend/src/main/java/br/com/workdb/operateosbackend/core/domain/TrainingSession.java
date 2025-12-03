package br.com.workdb.operateosbackend.core.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {

    @Id
    private String id;
    private String operatorId;
    private double currentScore;
    private boolean isActive;
    private LocalDateTime createdAt;

    @ElementCollection
    private List<String> logs = new ArrayList<>();

    @Deprecated // JPA only
    public TrainingSession() {}

    // Factory method para iniciar uma sessão
    public static TrainingSession start(String operatorId) {
        TrainingSession session = new TrainingSession();
        session.id = UUID.randomUUID().toString();
        session.operatorId = operatorId;
        session.currentScore = 100.0;
        session.isActive = true;
        session.createdAt = LocalDateTime.now();
        session.addLog("Sessão iniciada em " + session.createdAt);
        return session;
    }

    // Lógica de Negócio Rica (Rich Domain Model)
    public void processTelemetry(double speed, boolean collision) {
        if (!isActive) return;

        if (collision) {
            this.currentScore = 0.0;
            this.isActive = false;
            addLog("🚨 COLISÃO DETECTADA! Sessão encerrada.");
            return;
        }

        if (speed > 20.0) {
            this.currentScore = Math.max(0, this.currentScore - 0.5); // Não deixa negativar
            addLog(String.format("⚠️ Excesso de velocidade: %.1f km/h", speed));
        }
    }

    private void addLog(String message) {
        this.logs.add(message);
    }

    // Getters
    public String getId() { return id; }
    public String getOperatorId() { return operatorId; }
    public double getCurrentScore() { return currentScore; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() {return createdAt;}
    public List<String> getLogs() { return Collections.unmodifiableList(logs); }
}
