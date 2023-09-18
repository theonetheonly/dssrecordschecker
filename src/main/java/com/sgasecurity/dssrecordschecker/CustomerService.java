package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer getCustomerById(int id) {
        return (Customer)this.customerRepository.findById(id).get();
    }

    public Customer getCustomerBySystemCustomerNo(String systemCustomerNo) {
        return customerRepository.findBySystemCustomerNo(systemCustomerNo);
    }

    public Customer getCustomerBySessionNo(String sessionNo) {
        return (Customer)this.customerRepository.findBySessionNo(sessionNo);
    }

    public Customer getLastCustomer() {
        return (Customer)this.customerRepository.findTopByOrderByIdDesc();
    }

    public Customer getLastCustomerWithSystemCustomerNo() {
        return (Customer)this.customerRepository.findFirstBySystemCustomerNoNotNullOrderByIdDesc();
    }

    public Customer saveCustomer(Customer customer)
    {
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers()
    {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public String generateCustomerNo()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        String uniqueCustomerNo = "SGA-" + randomNumber;
        return uniqueCustomerNo;
    }

    public void updateCustomer(int id, String houseDoorNo, String locality, String townCity, String estateName, String roadStreet, String uniqueCustomerNo) {
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setHouseDoorNo(houseDoorNo);
            customer.setLocality(locality);
            customer.setTownCity(townCity);
            customer.setEstateName(estateName);
            customer.setRoadStreet(roadStreet);
            customer.setSystemCustomerNo(uniqueCustomerNo);
            this.customerRepository.save(customer);
        }
    }

    public void updateSgaTermsStatus(int id, String sgaTermsStatus) {
        Optional<Customer> optionalCustomer = this.customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setSgaTermsStatus(sgaTermsStatus);
            this.customerRepository.save(customer);
        }
    }

    public List<Customer> getCustomersWithNullSystemCustomerNos() {
        return customerRepository.findAllBySystemCustomerNoIsNull();
    }

    public void deleteCustomersWithNullSystemCustomerNo() {
        List<Customer> customers = customerRepository.findBySystemCustomerNoIsNull();
        customerRepository.deleteAll(customers);
    }
}
