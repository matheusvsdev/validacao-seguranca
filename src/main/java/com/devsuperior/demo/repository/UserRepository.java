package com.devsuperior.demo.repository;

import com.devsuperior.demo.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.devsuperior.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = """
				SELECT users.email AS username, users.password, role.id AS roleId, role.authority
				FROM users
				INNER JOIN tb_user_role ON users.id = tb_user_role.user_id
				INNER JOIN role ON role.id = tb_user_role.role_id
				WHERE users.email = :email
			""")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
	Optional<User> findByEmail(String email);
}
