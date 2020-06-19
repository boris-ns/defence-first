export interface LogFilterDTO {
    id?: number;
    source?: string;
    message?: string;
    logType?: string;
    startDate?: number[];
    endDate?: number[];
}
