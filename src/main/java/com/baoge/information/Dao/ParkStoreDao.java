package com.baoge.information.Dao;

import com.baoge.information.PO.ParkStorePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParkStoreDao extends JpaRepository<ParkStorePO, Integer>, JpaSpecificationExecutor<ParkStorePO> {

    ParkStorePO queryByPid(int pid);
}
