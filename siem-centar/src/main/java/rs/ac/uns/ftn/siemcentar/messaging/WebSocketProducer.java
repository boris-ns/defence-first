package rs.ac.uns.ftn.siemcentar.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import rs.ac.uns.ftn.siemcentar.mapper.LogMapper;
import rs.ac.uns.ftn.siemcentar.model.Log;

@Controller
public class WebSocketProducer {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    WebSocketProducer(SimpMessagingTemplate template){
        this.messagingTemplate = template;
    }
    
    public void sendLog(Log log) {
        this.messagingTemplate.convertAndSend("/topic", LogMapper.toDto(log));
    }
}
