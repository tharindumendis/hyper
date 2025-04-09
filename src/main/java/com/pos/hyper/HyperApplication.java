package com.pos.hyper;

import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HyperApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyperApplication.class, args);
	}

//	@Bean
// 	public CommandLineRunner runner(
//			 InvoiceRepository invoiceRepository
//	) {
// 		return args -> {
//
//			 Invoice invoice = new Invoice();
//			 Customer customer = new Customer();
//			 customer.setId(1);
//			 invoice.setCustomer(customer);
//			 invoice.setTotal(10.0);
//			 invoice = invoiceRepository.save(invoice);
// 			// Add your data entry logic here
// 			System.out.println("Data entry initialized..."+invoice.getId());
// 		};
// 	}

//		@Bean
// 	public CommandLineRunner runner(
//			 CustomerRepository customerRepository
//	) {
// 		return args -> {
//
//			Customer customer = new Customer();
//			customer.setName("John Doe");
//			customer.setAddress("123 Main St");
//			customer.setPhone("123-456-7890");
//			customer.setEmail("john.doe@example.com");
//			customer = customerRepository.save(customer);
// 			System.out.println("Data entry initialized..."+customer.getId());
// 		};
// 	}



}
