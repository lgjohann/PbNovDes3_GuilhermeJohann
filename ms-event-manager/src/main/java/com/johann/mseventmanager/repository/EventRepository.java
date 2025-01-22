package com.johann.mseventmanager.repository;

import com.johann.mseventmanager.entity.Event;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    @Aggregation(pipeline = {
            "{ $addFields: { tempEventName: { $toLower: '$eventName' } } }",
            "{ $sort: { tempEventName: 1 } }",
            "{ $project: { tempEventName: 0 } }"
    })
    List<Event> findAllByOrderByEventNameAsc();
}
