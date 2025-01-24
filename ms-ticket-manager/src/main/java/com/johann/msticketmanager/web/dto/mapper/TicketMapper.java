package com.johann.msticketmanager.web.dto.mapper;

import com.johann.msticketmanager.entity.Ticket;
import com.johann.msticketmanager.web.dto.TicketCreateDto;
import com.johann.msticketmanager.web.dto.TicketResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketMapper {

    public static Ticket toTicket(TicketCreateDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<TicketCreateDto, Ticket>() {
            @Override
            protected void configure() {
                skip().setTicketId(null);
            }
        });
        return modelMapper.map(dto, Ticket.class);
    }

    public static TicketResponseDto toDto(Ticket ticket) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Ticket, TicketResponseDto>() {
            @Override
            protected void configure() {
                map().setBrlTotalAmount(source.getBrlAmount());
                map().setUsdTotalAmount(source.getUsdAmount());
            }
        });
        return modelMapper.map(ticket, TicketResponseDto.class);
    }
}
