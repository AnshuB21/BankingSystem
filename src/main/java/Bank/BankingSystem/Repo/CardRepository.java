package Bank.BankingSystem.Repo;

import Bank.BankingSystem.entity.Card;
import Bank.BankingSystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {
}
