package Bank.BankingSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column( updatable = false, nullable = false)
    private String accountId;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false, unique = true)
    private long accountNumber;

    @Column(nullable = false, length = 3)
    private String currency; // e.g. USD, INR, EUR

    @Column(length = 10)
    private String code; // bank or branch code

    private String label; // Savings, Current, FD, etc.

    private char symbol; // ₹, $, €

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "account", fetch= FetchType.LAZY)
    private List<Transaction> transaction;

    @ManyToOne
    @JoinColumn(name= "owner_id")
    private User owner;
}

/*
* */
