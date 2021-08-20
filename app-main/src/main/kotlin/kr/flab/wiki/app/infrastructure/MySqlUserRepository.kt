package kr.flab.wiki.app.infrastructure

import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.repository.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import javax.inject.Inject

@Repository
class MySqlUserRepository(
    @Inject private val jdbcTemplate: JdbcTemplate
) : UserRepository {
    /**
     * Maybe operation: 검색 실패를 예외 처리하지 않음
     */

    override fun findByUserName(userName: String): User? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM ${User.name} WHERE ${MySqlUserRepository.userName} = ?",
            mapper,
            userName
        )
    }

    override fun findByEmail(email: String): User? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM ${User.name} WHERE $emailAddress = ?",
            mapper,
            email
        )
    }

    override fun save(user: User): User {
        jdbcTemplate.update(
            """INSERT INTO ${User.name}
                    (
                    $userName,
                    $emailAddress,
                    $registeredAt,
                    $lastActiveAt
                    ) values (?,?,?,?)""",
            user.userName,
            user.emailAddress,
            user.registeredAt,
            user.lastActiveAt
        )
        return user
    }

    override fun findUserByEmailAndPassword(email: String, password: String): User? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM ${User.name} WHERE $emailAddress = ? AND $password = ?",
            mapper,
            email,
            password
        )
    }

    companion object {
        val userName = User::userName::name.get()
        val emailAddress = User::emailAddress::name.get()
        val registeredAt = User::registeredAt::name.get()
        val lastActiveAt = User::lastActiveAt::name.get()
        val mapper = RowMapper { rs: ResultSet, _: Int ->
            User.newInstance(
                rs.getString(userName),
                rs.getString(emailAddress),
                rs.getTimestamp(registeredAt).toLocalDateTime(),
                rs.getTimestamp(lastActiveAt).toLocalDateTime()
            )
        }

        fun getUserMapper(): RowMapper<User> {
            return mapper
        }
    }
}
