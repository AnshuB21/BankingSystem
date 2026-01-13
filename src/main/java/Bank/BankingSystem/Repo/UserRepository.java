package Bank.BankingSystem.Repo;

import Bank.BankingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository <User, String>{

    UserDetails findByUsernameIgnoreCase(String username);
}
