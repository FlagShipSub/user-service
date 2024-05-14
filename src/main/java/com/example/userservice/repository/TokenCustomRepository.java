package com.example.userservice.repository;


import com.example.userservice.entity.Token;

import java.util.List;

public interface TokenCustomRepository {
    List<Token> findAllValidTokenByUser(Long userId);
}
