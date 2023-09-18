package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class ExecutePrintedInvoiceUpdateController {
    @Autowired
    InvoiceService invoiceService;

    @CrossOrigin
    @ResponseBody
    @GetMapping("/updateprintedinvoice")
    public void updatePrintedInvoice(@RequestParam("invoice_ref_no") String invoice_ref_no){
        try {
            Invoice invoice = invoiceService.getInvoiceByInvoiceRefNo(invoice_ref_no);
            if (!Objects.isNull(invoice)) {
                invoice.setIsPrinted("PRINTED");
                System.out.println("Updating invoice with ref no " + invoice_ref_no + " print status to PRINTED...\n");
                invoiceService.saveInvoice(invoice);
            } else {
                System.out.println("Invoice record with invoice ref no " + invoice_ref_no + " not found...\n");
            }
        } catch (Exception e){
            System.out.println("Error occurred fetching invoice record with invoice ref no " + invoice_ref_no + "...\n"+e.toString()+"\n");
        }
    }
}
