export interface TrainingSession {
  id: string;
  operatorId: string;
  currentScore: number;
  active: boolean;
  logs: string[];
}
