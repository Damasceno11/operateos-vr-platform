package br.com.workdb.operateosbackend.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainingSessionTest {

    @Test
    void shouldInitializeSessionCorrectly() {
        TrainingSession session = TrainingSession.start("OPERADOR-TESTE");

        assertNotNull(session.getId());
        assertEquals("OPERADOR-TESTE", session.getOperatorId()); // Supondo que você criou este getter
        assertEquals(100.0, session.getCurrentScore());
        assertTrue(session.isActive());
    }

    @Test
    void shouldDecreaseScoreOnSpeeding() {
        TrainingSession session = TrainingSession.start("OP-01");
        double initialScore = session.getCurrentScore();

        // Simula velocidade 25km/h (acima de 20)
        session.processTelemetry(25.0, false);

        assertEquals(99.5, session.getCurrentScore());
        // Verifica se o log foi adicionado (acesso indireto ou via getter de logs)
        assertFalse(session.getLogs().isEmpty());
    }

    @Test
    void shouldFailSessionOnCollision() {
        TrainingSession session = TrainingSession.start("OP-01");

        // Simula colisão
        session.processTelemetry(10.0, true);

        assertEquals(0.0, session.getCurrentScore());
        assertFalse(session.isActive());
    }
}
