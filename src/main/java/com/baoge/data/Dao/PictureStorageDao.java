package com.baoge.data.Dao;

import com.baoge.data.PO.PictureStoragePO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureStorageDao extends JpaRepository<PictureStoragePO, Integer> {

    List<PictureStoragePO> findAllByPrimaryid(int Primaryid);

    List<PictureStoragePO> findAllByType(int type);
}
