package com.pos.hyper.initializer;

import com.pos.hyper.exception.CustomExceptionHandler;
import com.pos.hyper.model.Role;
import com.pos.hyper.model.category.Category;
import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.supplier.Supplier;
import com.pos.hyper.model.user.User;
import com.pos.hyper.repository.CategoryRepository;
import com.pos.hyper.repository.CustomerRepository;
import com.pos.hyper.repository.SupplierRepository;
import com.pos.hyper.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseSeeder {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;


    public DatabaseSeeder(CategoryRepository categoryRepository, CustomerRepository customerRepository, UserRepository userRepository, SupplierRepository supplierRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
    }
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedDatabase() {
        seedAdminUser();
        seedCategories();
        seedSuppliers();
        seedCustomers();
        System.out.println("Database seeding completed!");
    }



    private void seedAdminUser() {
        if (userRepository.findById(1).isEmpty()) {
            User admin = new User();
            admin.setActive(true);
            admin.setUsername("Admin");
            admin.setPassword("admin");
            admin.setRole(Role.ADMIN);
            admin.setEmail("admin@hyper.com");
            admin.setPhone("0000000000");
            userRepository.save(admin);
            System.out.println("Admin user initialized.");
        }
    }
    private void seedCategories() {
        if (categoryRepository.findById(1).isEmpty()) {
            Category category = new Category();
            category.setName("None");
            categoryRepository.save(category);
            System.out.println("Category initialized.");
        }
    }
    private void seedSuppliers() {
        if (supplierRepository.findById(1).isEmpty()) {
            Supplier supplier = new Supplier();
            supplier.setName("unknown");
            supplier.setPhone("1234567890");
            supplier.setAddress("noAddress");
            supplier.setEmail("nomail@mail.com");
            supplierRepository.save(supplier);
            System.out.println("Supplier initialized.");
        }
    }
    private void seedCustomers() {
        if (customerRepository.findById(1).isEmpty()) {
            Customer customer = new Customer();
            customer.setName("Cash");
            customer.setPhone("1234567890");
            customer.setAddress("noAddress");
            customer.setEmail("nomail@mail.com");
            customerRepository.save(customer);
            System.out.println("Customer initialized.");
        }
    }

}
