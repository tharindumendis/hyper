package com.pos.hyper.model.invoiceStockConsumption;

import com.pos.hyper.repository.InvoiceStockConsumptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceStockConsumptionService {
    private final InvoiceStockConsumptionRepository invoiceStockConsumptionRepository;

    public InvoiceStockConsumptionService(InvoiceStockConsumptionRepository invoiceStockConsumptionRepository) {
        this.invoiceStockConsumptionRepository = invoiceStockConsumptionRepository;
    }

    public InvoiceStockConsumption createISC(InvoiceStockConsumption invoiceStockConsumption) {
        return invoiceStockConsumptionRepository.save(invoiceStockConsumption);
    }
    public List<InvoiceStockConsumption> createAllISCs( List<InvoiceStockConsumption> invoiceStockConsumptions) {
        return invoiceStockConsumptionRepository.saveAll(invoiceStockConsumptions);
    }
    public InvoiceStockConsumption getISCById(Integer id) {
        return invoiceStockConsumptionRepository.findById(id).orElse(null);
    }

    public List<InvoiceStockConsumption> getAllISCs() {
        return invoiceStockConsumptionRepository.findAll();
    }


}
