package com.johann.msticketmanager.service;

import static com.johann.msticketmanager.common.TicketConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.johann.msticketmanager.clients.MsEventClient;
import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.events.TicketEntityListener;
import com.johann.msticketmanager.exception.EventNotFound;
import com.johann.msticketmanager.exception.TicketDeleteException;
import com.johann.msticketmanager.exception.TicketNotFound;
import com.johann.msticketmanager.mqueue.NotificationTicket;
import com.johann.msticketmanager.repository.TicketRepository;
import com.johann.msticketmanager.web.dto.EventDto;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import com.johann.msticketmanager.web.dto.mapper.TicketMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private MsEventClient msEventClient;

    @Mock
    private NotificationTicket notificationTicket;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @Mock
    private TicketEntityListener ticketEntityListener;

    @Test
    public void createTicket_WithValidData_ReturnsTicketResponseDto(){
        EventDto eventDto = EventDto.toEvent(EVENT_CLIENT_RESPONSE_DTO);

        when(ticketRepository.save(TICKET)).thenReturn(TICKET);
        when(msEventClient.findEventById(any())).thenReturn(EVENT_CLIENT_RESPONSE_DTO);


        TicketResponseDto sut = ticketService.createTicket(TICKET);

        verify(ticketRepository).save(TICKET);
        assertThat(sut).isEqualTo(TicketMapper.toDto(TICKET, eventDto));
    }

    @Test
    public void createTicket_WithNonExistentEventId_ThrowsFeignException() {
        when(msEventClient.findEventById(any())).thenThrow(FeignException.FeignClientException.NotFound.class);

        assertThatThrownBy( () -> ticketService.createTicket(TICKET) ).isInstanceOf(EventNotFound.class);
    }

    @Test
    public void findTicketById_WithValidTicketId_ReturnsTicketResponseDto(){
        EventDto eventDto = EventDto.toEvent(EVENT_CLIENT_RESPONSE_DTO);

        when(ticketRepository.findById(any())).thenReturn(Optional.of(TICKET));
        when(msEventClient.findEventById(any())).thenReturn(EVENT_CLIENT_RESPONSE_DTO);

        TicketResponseDto sut = ticketService.findTicketById(BigInteger.valueOf(1));

        verify(ticketRepository).findById(BigInteger.valueOf(1));
        assertThat(sut).isEqualTo(TicketMapper.toDto(TICKET, eventDto));
    }

    @Test
    public void findTicketById_WithNonExistentTicketId_ThrowsTicketNotFoundException() {
        assertThatThrownBy( () -> ticketService.findTicketById(BigInteger.valueOf(5))).isInstanceOf(TicketNotFound.class);
    }

    @Test
    public void updateTicket_ValidData_ReturnsTicketResponseDto() {
        EventDto eventDto = EventDto.toEvent(EVENT_CLIENT_RESPONSE_DTO);
        TicketCreateDto ticketCreateDto = new TicketCreateDto(
                "customer altered",
                "12345678900",
                "alteredcustomermail@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50));
        when(ticketRepository.findById(BigInteger.valueOf(1))).thenReturn(Optional.of(TICKET));
        when(msEventClient.findEventById(any())).thenReturn(EVENT_CLIENT_RESPONSE_DTO);

        TicketResponseDto sut = ticketService.updateTicket(BigInteger.valueOf(1), ticketCreateDto);

        verify(ticketRepository).findById(BigInteger.valueOf(1));
        assertThat(sut.getTicketId()).isEqualTo(BigInteger.valueOf(1));
        assertThat(sut.getCustomerName()).isEqualTo("customer altered");
        assertThat(sut.getCustomerMail()).isEqualTo("alteredcustomermail@mail.com");
        assertThat(sut.getBrlTotalAmount()).isEqualTo("100");
    }


    @Test
    public void updateTicket_WithNonExistentEventId_ThrowsFeignException() {
        TicketCreateDto ticketCreateDto = new TicketCreateDto(
                        "customer",
                        "12345678900",
                        "customermail@mail.com",
                        "abc10",
                        "event",
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(5));
        when(ticketRepository.findById(any())).thenReturn(Optional.of(TICKET));
        when(msEventClient.findEventById(any())).thenThrow(FeignException.FeignClientException.NotFound.class);

        assertThatThrownBy( () -> ticketService.updateTicket(BigInteger.valueOf(1), ticketCreateDto) ).isInstanceOf(EventNotFound.class);
    }

    @Test
    public void findTicketByCPF_withExistingCPF_ReturnsTicketResponseDtoList() {
        EventDto eventDto = EventDto.toEvent(EVENT_CLIENT_RESPONSE_DTO);

        when(ticketRepository.findAllByCpf(any())).thenReturn(List.of(TICKET));
        when(msEventClient.findEventById(any())).thenReturn(EVENT_CLIENT_RESPONSE_DTO);

        List<TicketResponseDto> sut = ticketService.findTicketByCpf(TICKET.getCpf());

        verify(ticketRepository).findAllByCpf(TICKET.getCpf());
        assertThat(sut).isNotNull();
        assertThat(sut).isNotEmpty();
        assertThat(sut).isEqualTo(List.of(TicketMapper.toDto(TICKET, eventDto)));
    }

    @Test
    public void findTicketByCPF_withExistingCPF_ReturnsTicketsNotFoundException() {
        when(ticketRepository.findAllByCpf(any())).thenReturn(List.of());

        assertThatThrownBy( () -> ticketService.findTicketByCpf(TICKET.getCpf())).isInstanceOf(TicketNotFound.class);
    }

    @Test
    public void deleteById_WithValidId_SetsStatusToCancelado() {
        Ticket ticketToCancel = new Ticket(
                BigInteger.valueOf(1),
                "customer",
                "12345678900",
                "customermail@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(1),
                "sucesso"
        );

        when(ticketRepository.existsById(any())).thenReturn(true);
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticketToCancel));

        ticketService.deleteById(BigInteger.valueOf(1));

        verify(ticketRepository).save(ticketToCancel);
        assertThat(ticketToCancel.getStatus()).isEqualTo("cancelado");
    }

    @Test
    public void deleteById_WithNonExistentId_ThrowsTicketNotFoundException() {
        when(ticketRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> ticketService.deleteById(BigInteger.valueOf(1))).isInstanceOf(TicketNotFound.class);
    }

    @Test
    public void deleteById_WhenTicketAlreadyCanceled_ThrowsTicketDeleteException() {
        Ticket canceledTicket = new Ticket(
                BigInteger.valueOf(1),
                "customer",
                "12345678900",
                "customermail@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(1),
                "cancelado"
        );

        when(ticketRepository.existsById(any())).thenReturn(true);
        when(ticketRepository.findById(any())).thenReturn(Optional.of(canceledTicket));

        assertThatThrownBy(() -> ticketService.deleteById(BigInteger.valueOf(1))).isInstanceOf(TicketDeleteException.class);
    }

    @Test
    public void deleteByCpf_WithExistingTickets_SetsStatusToCancelado() {
        Ticket ticket1 = new Ticket(
                BigInteger.valueOf(1),
                "customer1",
                "12345678900",
                "customer1@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(1),
                "sucesso"
        );
        Ticket ticket2 = new Ticket(
                BigInteger.TWO,
                "customer2",
                "12345678900",
                "customer2@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(1),
                "sucesso"
        );

        when(ticketRepository.findAllByCpf(any())).thenReturn(List.of(ticket1, ticket2));

        ticketService.deleteByCpf("12345678900");

        verify(ticketRepository, times(2)).save(any(Ticket.class));
        assertThat(ticket1.getStatus()).isEqualTo("cancelado");
        assertThat(ticket2.getStatus()).isEqualTo("cancelado");
    }

    @Test
    public void deleteByCpf_WithNoTickets_ThrowsTicketNotFoundException() {
        when(ticketRepository.findAllByCpf(any())).thenReturn(List.of());

        assertThatThrownBy(() -> ticketService.deleteByCpf("12345678900"))
                .isInstanceOf(TicketNotFound.class)
                .hasMessageContaining("No tickets were found");
    }

    @Test
    public void deleteByCpf_WhenAllTicketsAlreadyCanceled_ThrowsTicketDeleteException() {
        Ticket canceledTicket1 = new Ticket(
                BigInteger.valueOf(1),
                "customer1",
                "12345678900",
                "customer1@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(1),
                "cancelado"
        );
        Ticket canceledTicket2 = new Ticket(
                BigInteger.TWO,
                "customer2",
                "12345678900",
                "customer2@mail.com",
                "abc10",
                "event",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(1),
                "cancelado"
        );

        when(ticketRepository.findAllByCpf(any())).thenReturn(List.of(canceledTicket1, canceledTicket2));

        assertThatThrownBy(() -> ticketService.deleteByCpf("12345678900")).isInstanceOf(TicketDeleteException.class);
    }

    @Test
    public void findTicketsByEventId_WithExistingTickets_ReturnsTicketResponseDtoList() {
        EventDto eventDto = EventDto.toEvent(EVENT_CLIENT_RESPONSE_DTO);
        when(ticketRepository.findAllByEventId(any())).thenReturn(List.of(TICKET));
        when(msEventClient.findEventById(any())).thenReturn(EVENT_CLIENT_RESPONSE_DTO);

        List<TicketResponseDto> sut = ticketService.findTicketsByEventId("abc10");

        assertThat(sut).isNotEmpty();
        assertThat(sut).containsExactly(TicketMapper.toDto(TICKET, eventDto));
        assertThat(sut).hasSize(1);
    }

    @Test
    public void findTicketsByEventId_WithNoTickets_ThrowsTicketNotFoundException() {
        when(ticketRepository.findAllByEventId(any())).thenReturn(List.of());

        assertThatThrownBy(() -> ticketService.findTicketsByEventId("abc10"))
                .isInstanceOf(TicketNotFound.class)
                .hasMessageContaining("No tickets were found");
    }

}
