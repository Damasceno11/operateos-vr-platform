import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, of, retry, shareReplay, switchMap, timer } from 'rxjs';
import { TrainingSession } from '../models/training-session';

@Injectable({
  providedIn: 'root',
})
export class TelemetryService {
  private http = inject(HttpClient);
  // URL do Backend Java (Profile Local)
  private readonly API_URL = 'http://localhost:8080/api/v1/training/latest';

  // Polling reativo: Cria um stream que busca dados a cada 1000ms
  // shareReplay(1) garante que múltiplos componentes ouçam a mesma requisição
  getLatestSession(): Observable<TrainingSession | null> {
    return timer(0, 1000).pipe(
      switchMap(() =>
        this.http.get<TrainingSession>(this.API_URL).pipe(
          catchError((err) => {
            console.error('Erro de conexão com Telemetria:', err);
            return of(null); // Retorna null se cair a conexão para não quebrar a UI
          })
        )
      ),
      retry(3), // Tenta reconectar 3 vezes antes de falhar
      shareReplay(1)
    );
  }
}
