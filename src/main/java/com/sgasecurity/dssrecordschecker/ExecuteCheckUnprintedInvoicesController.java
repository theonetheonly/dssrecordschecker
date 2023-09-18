package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ExecuteCheckUnprintedInvoicesController {
    Invoice invoice = null;
    @Autowired
    InvoiceService invoiceService;

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getunprintedinvoices")
    public String getUnprintedInvoices() {

        List<Invoice> invoiceList = invoiceService.getAllInvoices();

        System.out.println("Invoice list found: " + invoiceList.size());

        List<String> unprintedInvoices = new ArrayList<>();

        int counter = 0;

        if (invoiceList.size() > 0) {
            for (Invoice theInvoice: invoiceList) {
              if(theInvoice.getIsPrinted() != null || theInvoice.getIsPrinted() != ""){
                if (theInvoice.getIsPrinted().equals("PENDING")) {
                    String invoiceRefNo = theInvoice.getInvoiceRefNo();
                    System.out.println("Adding unprinted invoice with ref no. "+invoiceRefNo+"...");

                    if(checkInvoiceValidity(invoiceRefNo)){
                        unprintedInvoices.add(invoiceRefNo);
                    }

                    counter++;
                }
              }
            }
            System.out.println("Returned "+counter+" unprinted invoices");
            return unprintedInvoices.toString();
        } else {
            System.out.println("No invoices found...");
            return null;
        }
    }

    public boolean checkInvoiceValidity(String invoiceRefNo){
        if (invoiceRefNo.matches("^I.*\\d$")) {
            return true;
        } else {
            return false;
        }
    }
}
