package com.vecanhac.ddd.domain.location;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations")
@Data
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(name = "venue_name", nullable = false)
    private String venueName;

    @Column(nullable = false, length = 500)
    private String address;

    private Float lat;

    private Float lng;
}