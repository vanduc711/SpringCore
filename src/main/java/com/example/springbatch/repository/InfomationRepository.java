package com.example.springbatch.repository;

import com.example.springbatch.model.Infomation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfomationRepository extends JpaRepository<Infomation, Integer>{
}
