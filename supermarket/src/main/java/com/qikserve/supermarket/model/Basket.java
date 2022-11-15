package com.qikserve.supermarket.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="baskets")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="basketProducts")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String status;

    @JsonManagedReference
    @OneToMany(mappedBy = "pk.basket")
    @Valid
    private List<BasketProduct> basketProducts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "expected_totals_ID")
    private ExpectedTotals expectedTotals;

}
