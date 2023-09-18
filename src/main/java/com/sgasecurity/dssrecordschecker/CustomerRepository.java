package com.sgasecurity.dssrecordschecker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findBySystemCustomerNo(String systemCustomerNo);
    Customer findBySessionNo(String sessionNo);
    Customer findTopByOrderByIdDesc();
    Customer findFirstBySystemCustomerNoNotNullOrderByIdDesc();
    List<Customer> findAllBySystemCustomerNoIsNull();
    List<Customer> findBySystemCustomerNoIsNull();

    @Query(value = "SELECT system_customer_no FROM customers WHERE system_customer_no LIKE '%KE%' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastCustomerUniqueID();

    @Query(value = "SELECT system_customer_no FROM customers WHERE system_customer_no LIKE '%TMP%' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastCustomerUniqueTempID();

    @Query(value = "SELECT * FROM customers WHERE system_customer_no LIKE '%TMP%' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Customer getLastCustomerEntityTemp();

}
