package com.diyetisyen.customer.service;


import com.diyetisyen.customer.entity.Customer;
import com.diyetisyen.customer.repository.CustomerRepository;

import java.util.List;

public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }

}
