package com.inn.restaurant.dao;

import com.inn.restaurant.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BillDao extends JpaRepository<Bill,Integer> {
    List<Bill> getAllBills();

    List<Bill> getByUserName(@RequestParam("username") String username);
}
