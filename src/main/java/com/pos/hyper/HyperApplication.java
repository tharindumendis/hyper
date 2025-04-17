package com.pos.hyper;

import com.pos.hyper.model.Role;
import com.pos.hyper.model.Unitt;
import com.pos.hyper.model.category.Category;
import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.grn.Grn;
import com.pos.hyper.model.inOrder.InOrder;
import com.pos.hyper.model.inOrder.InOrderDto;
import com.pos.hyper.model.inOrder.InOrderMapper;
import com.pos.hyper.model.inOrder.InOrderService;
import com.pos.hyper.model.inventory.Inventory;
import com.pos.hyper.model.invoice.Invoice;
import com.pos.hyper.model.product.Product;
import com.pos.hyper.model.supplier.Supplier;
import com.pos.hyper.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HyperApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyperApplication.class, args);
	}

	@Bean
 	public CommandLineRunner runner(
			InvoiceRepository invoiceRepository,
			CustomerRepository customerRepository,
			CategoryRepository categoryRepository,
			ProductRepository productRepository,
			SupplierRepository supplierRepository,
			InventoryRepository inventoryRepository,
			GrnRepository grnRepository,
			InOrderService inOrderService, InOrderMapper inOrderMapper) {
 		return args -> {

//			 Customer customer = new Customer();
//			 customer.setName("John");
//			 customer.setPhone("1234567890");
//			 customer.setAddress("123 Main St");
//			 customer.setEmail("john@example.com");
//			 customer = customerRepository.save(customer);
//
//			 Category category = new Category();
//			 category.setName("Electronics");
//			 categoryRepository.save(category);
//
//			 Invoice invoice = new Invoice();
//			 invoice.setCustomer(customer);
//			 invoice.setTotal(0.0);
//			 invoiceRepository.save(invoice);
//
//			 Supplier supplier = new Supplier();
//			 supplier.setName("Acme Electronics");
//			 supplier.setPhone("5550001234");
//			 supplier.setAddress("123 Supplier St");
//			 supplier.setEmail("supplier@example.com");
//			 supplierRepository.save(supplier);
//
//			 Product product = new Product();
//			 product.setName("Laptop");
//			 product.setDescription("High-performance laptop");
//			 product.setPrice(1000.0);
//			 product.setCategory(category);
//			 product.setCost(900.0);
//			 product.setQuantity(0.0);
//			 product.setUnit(Unitt.pcs);
//			 product.setBarcode("1234567890");
//			 productRepository.save(product);
//
//			 Inventory inventory = new Inventory();
//			 inventory.setTotal(0.0);
//			 inventory.setSupplier(supplier);
//			 inventoryRepository.save(inventory);




//			 Grn grn = new Grn();
//			 grn.setProduct(product);
//			 grn.setQuantity(10.0);
//			 grn.setUnitCost(100.0);
//			 grn.setInventory(inventory);
//			 grn.setDiscount(0);
//			 grn.setUnitCost(100.0);
//			 grn.setAmount(1000.0);
//			 grnRepository.save(grn);
//
//			 List<InOrderDto> inOrders = new ArrayList<>();
//			 InOrder inOrder = new InOrder();
//			 inOrder.setInvoice(invoice);
//			 inOrder.setProduct(product);
//			 inOrder.setQuantity(1.0);
//			 inOrder.setUnitPrice(100.0);
//			 inOrder.setDiscount(0);
//			 inOrder.setCostPrice(100.0);
//			 inOrder.setAmount(100.0);
//			 inOrders.add(inOrderMapper.toInOrderDto(inOrder));
//
//			 InOrder inOrder1 = new InOrder();
//			 inOrder1.setInvoice(invoice);
//			 inOrder1.setProduct(product);
//			 inOrder1.setQuantity(2.0);
//			 inOrder1.setUnitPrice(100.0);
//			 inOrder1.setDiscount(0);
//			 inOrder1.setCostPrice(100.0);
//			 inOrder1.setAmount(200.0);
//			 inOrders.add(inOrderMapper.toInOrderDto(inOrder1));
//
//			 inOrderService.createInOrders(inOrders);
//

 			System.out.println("######### set sample data set ###########");
 		};
 	}




}
