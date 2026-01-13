package Bank.BankingSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txId;

    // --- Core Transaction Fields ---
    @Column(nullable = false)
    private Double amount;

    private Double txFee;

    @Column(nullable = false) //It as enum class
    private String type;       // deposit, withdraw, transfer, loan-payment etc.

    @Column(nullable = false)  // It is an enum class
    private String status;     // SUCCESS, FAILED, PENDING

    // --- Relationships ---
    // Transaction Owner (User who initiated the transaction)


    private String sender;


    private String receiver;

    // --- Time fields ---
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name= "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
