package com.orderfulfillment.repository;

import com.orderfulfillment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}