package rs.ac.uns.ftn.siemcentar.mapper;

import rs.ac.uns.ftn.siemcentar.dto.response.AlarmDTO;
import rs.ac.uns.ftn.siemcentar.model.Alarm;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmMapper {

    public static AlarmDTO toDto(Alarm alarm) {
        return new AlarmDTO(alarm);
    }

    public static List<AlarmDTO> toListDto(List<Alarm> alarms) {
        return alarms.stream()
                .map(AlarmDTO::new).collect(Collectors.toList());
    }

    private AlarmMapper() {
    }
}
