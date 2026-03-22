package com.cognizant.billing.dao.jdbc;

import com.cognizant.billing.dao.UserDao;
import com.cognizant.billing.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbc;

    public JdbcUserDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<User> RM = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            LocalDateTime created = rs.getTimestamp("created_at").toLocalDateTime();
            var updatedTs = rs.getTimestamp("updated_at");
            LocalDateTime updated = (updatedTs != null) ? updatedTs.toLocalDateTime() : null;
            return new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    created,
                    updated
            );
        }
    };

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) user.setId(key.longValue());
        return user;
    }

    @Override
    public int update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        return jdbc.update(sql, user.getName(), user.getEmail(), user.getId());
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, name, email, created_at, updated_at FROM users WHERE id = ?";
        var list = jdbc.query(sql, RM, id);
        return list.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, name, email, created_at, updated_at FROM users WHERE email = ?";
        var list = jdbc.query(sql, RM, email);
        return list.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, name, email, created_at, updated_at FROM users ORDER BY id";
        return jdbc.query(sql, RM);
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbc.update(sql, id) > 0;
    }
}