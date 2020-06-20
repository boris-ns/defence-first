export interface ReportsDTO {
    reports: AgentReport[];
}

interface AgentReport {
    agent: String;
    numOfLogs: number;
    numOfAlarms: number;
    sources: SourceReport[];
}

interface SourceReport {
    source: String;
    numOfLogs: number;
    numOfAlarms: number;
}
