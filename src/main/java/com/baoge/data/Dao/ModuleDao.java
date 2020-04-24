package com.baoge.data.Dao;

import com.baoge.data.PO.ModulePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModuleDao extends JpaRepository<ModulePO, Integer> {
    List<ModulePO> findAllByOrderByLevelAscOrderAsc();
}
