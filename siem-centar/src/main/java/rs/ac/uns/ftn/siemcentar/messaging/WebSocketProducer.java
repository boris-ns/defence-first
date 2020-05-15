package rs.ac.uns.ftn.siemcentar.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.siemcentar.mapper.LogMapper;
import rs.ac.uns.ftn.siemcentar.model.Log;

@Component
public class WebSocketProducer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendLog(Log log) {
        this.messagingTemplate.convertAndSend("/topic", LogMapper.toDto(log));
    }
}
