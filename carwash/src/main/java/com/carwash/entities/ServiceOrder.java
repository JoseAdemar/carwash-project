package com.carwash.entities;

import com.carwash.entities.enumerations.WashStatusEnum;
import com.carwash.entities.enumerations.WashTypeEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_order")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    private Customer customer;

    @Column(name = "date")
    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WashStatusEnum washStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WashTypeEnum washType;

    @NotNull
    private BigDecimal bigDecimal;
}
