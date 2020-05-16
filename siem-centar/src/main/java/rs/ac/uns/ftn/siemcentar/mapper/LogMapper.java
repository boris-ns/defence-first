package rs.ac.uns.ftn.siemcentar.mapper;

import rs.ac.uns.ftn.siemcentar.dto.response.LogDTO;
import rs.ac.uns.ftn.siemcentar.model.Log;

import java.util.List;
import java.util.stream.Collectors;

public class LogMapper {

    public static List<LogDTO> toListDto(List<Log> logs) {
        return logs.stream()
                .map(LogDTO::new).collect(Collectors.toList());
    }

    public static LogDTO toDto(Log log) {
        return new LogDTO(log);
    }

    private LogMapper() {
    }
}
