package com.johann.msticketmanager.repository;

import com.johann.msticketmanager.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;


@Repository
public interface TicketRepository extends MongoRepository<Ticket, BigInteger> {
}
