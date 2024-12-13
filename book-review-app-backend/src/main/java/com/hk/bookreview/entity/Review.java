package com.hk.bookreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "book_title", nullable = false)
    private String bookTitle;
    @Column(nullable = false)
    private String Author;
    @Column(nullable = false)
    private BigDecimal rating;
    @Column(length = 1000, nullable = false)
    private String review;
    @Column(name = "date_added", nullable = false)
    private Date dateAdded;
}
