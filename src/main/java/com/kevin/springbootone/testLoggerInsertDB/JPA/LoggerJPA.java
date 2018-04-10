package com.kevin.springbootone.testLoggerInsertDB.JPA;

import com.kevin.springbootone.testLoggerInsertDB.entity.LoggerInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface LoggerJPA  extends JpaRepository<LoggerInfoEntity, Long>, JpaSpecificationExecutor<LoggerInfoEntity>,
        Serializable {


}
