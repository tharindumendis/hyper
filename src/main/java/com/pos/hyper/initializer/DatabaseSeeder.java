package com.pos.hyper.initializer;

import com.pos.hyper.model.org.Organization;
import com.pos.hyper.model.Role;
import com.pos.hyper.model.category.Category;
import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.supplier.Supplier;
import com.pos.hyper.model.user.User;
import com.pos.hyper.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DatabaseSeeder {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final OrgRepository orgRepository;


    public DatabaseSeeder(CategoryRepository categoryRepository, CustomerRepository customerRepository, UserRepository userRepository, SupplierRepository supplierRepository, OrgRepository orgRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.orgRepository = orgRepository;
    }
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedDatabase() {
        seedAdminUser();
        seedCategories();
        seedSuppliers();
        seedCustomers();
        seedOrg();
        System.out.println("Database seeding completed!");
    }



    private void seedAdminUser() {
        if (userRepository.findById(1).isEmpty()) {
            User admin = new User();
            admin.setActive(true);
            admin.setUsername("admin");
            admin.setPassword("$2a$10$fOY5mkXzk6VwZzITMqQmoO36uogFFq067EGTKEvIvxfL0rcFPg2eW");
            admin.setRole(Role.ADMIN);
            admin.setEmail("admin@hyper.com");
            admin.setPhone("0000000000");
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("Admin user initialized.");
        }
    }
    private void seedCategories() {
        if (categoryRepository.findById(1).isEmpty()) {
            Category category = new Category();
            category.setName("None");
            category.setCreatedAt(LocalDateTime.now());
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
            customer.setCreatedAt(LocalDateTime.now());
            customerRepository.save(customer);
            System.out.println("Customer initialized.");
        }
    }
    private void seedOrg() {
        if (orgRepository.findById(1).isEmpty()) {
            Organization org = new Organization();
            org.setName("My Store");
            org.setEmail("store@email.com");
            org.setPhone("1234567890");
            org.setAddress("noAddress");
            org.setEmployeeCount(3);
            org.setIsActive(true);
            org.setCreatedAt(LocalDateTime.now());
            orgRepository.save(org);
            System.out.println("Org initialized.");
        }
    }
}
