package com.pos.hyper;

import com.pos.hyper.model.invoice_item.InvoiceItemMapper;
import com.pos.hyper.model.invoice_item.InvoiceItemService;
import com.pos.hyper.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
			GRNRepository grnRepository,
			GrnItemRepository grnItemRepository,
			InvoiceItemService invoiceItemService, InvoiceItemMapper invoiceItemMapper) {
 		return args -> {

//			 Customer customer = new Customer();
//			 customer.setName("Cash");
//			 customer.setPhone("1234567890");
//			 customer.setAddress("no Address");
//			 customer.setEmail("nomail@mail.com");
//			 customer = customerRepository.save(customer);
////
//			 Category category = new Category();
//			 category.setName("None");
//			 categoryRepository.save(category);
//
//			 Invoice invoice = new Invoice();
//			 invoice.setCustomer(customer);
//			 invoice.setTotal(0.0);
//			 invoiceRepository.save(invoice);



//			 Supplier supplier = new Supplier();
//			 supplier.setName("unknown");
//			 supplier.setPhone("1234567890");
//			 supplier.setAddress("noAddress");
//			 supplier.setEmail("nomail@mail.com");
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




//			 GrnItem grnItem = new GrnItem();
//			 grnItem.setProduct(product);
//			 grnItem.setQuantity(10.0);
//			 grnItem.setUnitCost(100.0);
//			 grnItem.setInventory(inventory);
//			 grnItem.setDiscount(0);
//			 grnItem.setUnitCost(100.0);
//			 grnItem.setAmount(1000.0);
//			 grnRepository.save(grnItem);
//
//			 List<InvoiceItemDto> invoiceItems = new ArrayList<>();
//			 InvoiceItem inOrder = new InvoiceItem();
//			 inOrder.setInvoice(invoice);
//			 inOrder.setProduct(product);
//			 inOrder.setQuantity(1.0);
//			 inOrder.setUnitPrice(100.0);
//			 inOrder.setDiscount(0);
//			 inOrder.setCostPrice(100.0);
//			 inOrder.setAmount(100.0);
//			 invoiceItems.add(inOrderMapper.toInOrderDto(inOrder));
//
//			 InvoiceItem inOrder1 = new InvoiceItem();
//			 inOrder1.setInvoice(invoice);
//			 inOrder1.setProduct(product);
//			 inOrder1.setQuantity(2.0);
//			 inOrder1.setUnitPrice(100.0);
//			 inOrder1.setDiscount(0);
//			 inOrder1.setCostPrice(100.0);
//			 inOrder1.setAmount(200.0);
//			 invoiceItems.add(inOrderMapper.toInOrderDto(inOrder1));
//
//			 inOrderService.createInOrders(invoiceItems);
//

 			System.out.println("######### set sample data set ###########");
 		};
 	}




}
