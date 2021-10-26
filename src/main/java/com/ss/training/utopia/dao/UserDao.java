package com.ss.training.utopia.dao;

import com.ss.training.utopia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {

}
