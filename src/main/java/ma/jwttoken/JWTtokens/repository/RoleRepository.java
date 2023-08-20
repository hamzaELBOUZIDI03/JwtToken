package ma.jwttoken.JWTtokens.repository;

import ma.jwttoken.JWTtokens.models.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, Integer> {

    Optional<RolesEntity> findByNameRole(String nameRole);

}
