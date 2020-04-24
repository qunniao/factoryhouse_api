package com.baoge.data.Dao;

import com.baoge.data.PO.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SystemConfigDao extends JpaRepository<SystemConfig, Integer> {
    SystemConfig queryByKeyOrderByOrderAsc(String key);
    List<SystemConfig> findAllByOrderByOrderAsc();

    @Modifying
    @Transactional
    @Query(value = "update SystemConfig set value=:value where key=:key")
    int updateSystemConfigKeyByValue(String key,String value);
}
