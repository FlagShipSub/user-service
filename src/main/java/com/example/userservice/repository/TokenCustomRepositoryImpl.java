package com.example.userservice.repository;

import com.example.userservice.entity.Token;
import com.example.userservice.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenCustomRepositoryImpl implements TokenCustomRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Token> findAllValidTokenByUser(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Token> query = cb.createQuery(Token.class);
        Root<Token> token = query.from(Token.class);
        Join<Token, User> user = token.join("user");

        // Predicates (Conditions)
        Predicate userIdMatch = cb.equal(user.get("id"), userId);
        Predicate notExpired = cb.isFalse(token.get("isExpired"));
        Predicate notRevoked = cb.isFalse(token.get("isRevoked"));
        Predicate validToken = cb.or(notExpired, notRevoked); // (isExpired = false OR isRevoked = false)

        // Combining Predicates and Building the Query
        query.where(cb.and(userIdMatch, validToken));
        query.select(token);

        // Execute and get results
        TypedQuery<Token> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
