package Bank.BankingSystem.Repo;

import Bank.BankingSystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository <Account, String> {
}
