package com.growup.pms.user.repository;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, QueryDslUserRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
            FROM User u
            WHERE u.profile.nickname = :nickname""")
    Boolean existsByNickname(@Param("nickname") String nickname);

    default User findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    default User findByUsernameOrThrow(String username) {
        return findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    default User findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
