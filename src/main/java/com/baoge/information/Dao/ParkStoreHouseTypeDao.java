package com.baoge.information.Dao;

import com.baoge.information.PO.ParkStoreHouseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkStoreHouseTypeDao extends JpaRepository<ParkStoreHouseType, Integer> {
    ParkStoreHouseType queryByPid(int pid);
}
