package com.pos.hyper;

import com.pos.hyper.model.category.Category;
import com.pos.hyper.model.customer.Customer;
import com.pos.hyper.model.invoice.Invoice;
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


}
