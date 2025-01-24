package com.johann.msticketmanager.events;

import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class TicketEntityListener extends AbstractMongoEventListener<Ticket> {

    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Ticket> event) {
        if (event.getSource().getTicketId() == null || event.getSource().getTicketId().intValue() < 1) {
            event.getSource().setTicketId(BigInteger.valueOf(sequenceGenerator.generateSequence(Ticket.SEQUENCE_NAME)));
        }
    }
}
