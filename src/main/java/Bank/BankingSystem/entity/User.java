package Bank.BankingSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//https://youtu.be/hSzCjqZs_uE?si=Od869ddGB6DPJWq9
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    private String firstname;

    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private Date dob;

    private Long tel;

    private String tag;         // can be used like "@anshu123" or internal identifier

    private String password;

    private String gender;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


  private List<String> roles;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private Card card;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "owner", fetch= FetchType.LAZY)
    private List<Transaction> transaction;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "owner", fetch= FetchType.LAZY)
    private List<Account> Account;

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities (){
        return roles.stream().map(SimpleGrantedAuthority :: new).collect(Collectors.toList());
    }


}