package com.baoge.information.Dao;

import com.baoge.information.PO.EncyclopediaPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EncyclopediaDao extends JpaRepository<EncyclopediaPO, Integer>, JpaSpecificationExecutor<EncyclopediaPO> {
    EncyclopediaPO queryByElid(int elid);
}
