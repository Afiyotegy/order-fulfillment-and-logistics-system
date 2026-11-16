package com.orderfulfillment.repository;

import com.orderfulfillment.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}