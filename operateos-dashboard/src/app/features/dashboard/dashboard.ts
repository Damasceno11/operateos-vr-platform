import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { TelemetryService } from '../../core/services/telemetry.service';
import { Observable } from 'rxjs';
import { TrainingSession } from '../../core/models/training-session';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatListModule,
    MatChipsModule,
    MatProgressBarModule,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  private telemetryService = inject(TelemetryService);

  // Usando Signals (Angular 17+) para performance máxima
  session = toSignal(this.telemetryService.getLatestSession());

  getScoreColor(score: number): string {
    if (score < 50) return 'warn';
    if (score < 80) return 'accent';
    return 'primary';
  }

  getLogIcon(log: string): string {
    if (log.includes('COLISÃO') || log.includes('encerrada')) return 'warning';
    if (log.includes('velocidade')) return 'speed';
    return 'info';
  }
}
