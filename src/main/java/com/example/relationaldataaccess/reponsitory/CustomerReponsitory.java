package com.example.relationaldataaccess.reponsitory;

import com.example.relationaldataaccess.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//sử dụng @Query để truy vấn chính xác dữ liệu
public interface CustomerReponsitory extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM Customer WHERE address = :address", nativeQuery = true)
    List<Customer> findByAddress(String address);

    @Query(value = "SELECT * FROM Customer WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    List<Customer> searchByName(@Param("name") String name);

}
