package com.sgasecurity.dssrecordschecker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findBySystemCustomerNoAndInvoicingDate(String systemCustomerNo, LocalDate invoicingDate);
    Invoice findBySystemCustomerNo(String systemCustomerNo);
    Invoice findByInvoiceRefNo(String invoiceRefNo);
}
