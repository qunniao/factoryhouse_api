package com.baoge.data.Dao;


import com.baoge.data.PO.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductListDao extends JpaRepository<ProductList, Integer> , JpaSpecificationExecutor<ProductList> {
    ProductList queryByPlid(int plid);
}
