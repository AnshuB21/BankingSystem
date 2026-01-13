package Bank.BankingSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    @Column(nullable = false, unique = true, length = 20)
    private long cardNumber;

    @Column(nullable = false)
    private Double balance;

    // Card issuer: VISA / MASTERCARD / AMEX / DISCOVER etc.
    @CreationTimestamp
    private LocalDate iss;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Expiry date MM/YY or YYYY-MM
    @Column(nullable = false)
    private String exp;

    @Column(nullable = false, length = 3)
    private String cvv;

    @Column(nullable = false, length = 4)
    private String pin;

    @Column(nullable = false)
    private String billingAddress;

    @OneToOne
    @JoinColumn (name= "owner_id")
    private User owner;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "card", fetch= FetchType.LAZY)
    private List<Transaction> transaction;
}
