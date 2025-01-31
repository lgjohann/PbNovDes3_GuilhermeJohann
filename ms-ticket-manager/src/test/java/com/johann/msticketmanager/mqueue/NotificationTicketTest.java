package com.johann.msticketmanager.mqueue;

import static com.johann.msticketmanager.common.TicketConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johann.msticketmanager.entity.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
public class NotificationTicketTest {

    @InjectMocks
    private NotificationTicket notificationTicket;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private Queue sendEmailQueue;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void publishTicket_WithValidData() {
        when(sendEmailQueue.getName()).thenReturn("test-queue");

        notificationTicket.publishTicketCreated(TICKET);

        verify(rabbitTemplate).convertAndSend(eq("test-queue"), contains("customer"));
    }

    @Test
    public void publishTicket_WithInvalidData() {

        try (MockedConstruction<ObjectMapper> mocked = mockConstruction(ObjectMapper.class,
                (mock, context) -> when(mock.writeValueAsString(any(Email.class))).thenThrow(JsonProcessingException.class))) {

            assertThatNoException().isThrownBy(() -> notificationTicket.publishTicketCreated(TICKET));
            verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
        }
    }


}
