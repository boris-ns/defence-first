export interface ReportsDTO {
    reports: AgentReport[];
}

interface AgentReport {
    agent: string;
    numOfLogs: number;
    numOfAlarms: number;
    sources: SourceReport[];
}

export interface SourceReport {
    source: string;
    numOfLogs: number;
    numOfAlarms: number;
}
