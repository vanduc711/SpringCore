package com.example.relationaldataaccess.controller;

import com.example.relationaldataaccess.entity.Customer;
import com.example.relationaldataaccess.reponsitory.CustomerReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
    @Autowired
    private CustomerReponsitory customerReponsitory;

    @RequestMapping(value = {"/", "/customer-list"})
    public String listCustomer(Model model) {
        model.addAttribute("listCustomer", customerReponsitory.findAll());
        return "customer-list";
    }

    @RequestMapping(value = "/customer-save")
    public String insertCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-save";
    }

    @RequestMapping("/filter-address-dn")
    public String addressDN(@RequestParam(name = "address", required = false) String address, Model model) {
        List<Customer> customers;

        if (address != null) {
            customers = customerReponsitory.findByAddress(address);
        } else {
            customers = customerReponsitory.findAll();        }

        model.addAttribute("listCustomer", customers);
        return "customer-list";
    }

    @RequestMapping(value = "/customer-view/{id}")
    public String viewCustomer(@PathVariable long id, Model model) {
        Optional<Customer> customer = customerReponsitory.findById(id);
        if(customer.isPresent()) {
            model.addAttribute("customer", customer.get());
        }
        return "customer-view";
    }

    @RequestMapping(value = "/customer-update/{id}")
    public String updateCustomer(@PathVariable long id, Model model) {
        Optional<Customer> customer = customerReponsitory.findById(id);
        if(customer.isPresent()) {
            model.addAttribute("customer", customer.get());
        }
        return "customer-update";
    }

    @RequestMapping(value = "/search")
    public String searchCustomer(@RequestParam("name") String name, Model model) {
        if (name == null || name.trim().isEmpty()) {
            // Xử lý khi tìm kiếm trống, có thể là chuyển hướng hoặc thông báo
        } else {
            List<Customer> customers = customerReponsitory.searchByName(name);
            model.addAttribute("listCustomer", customers);
        }
        return "customer-list";
    }


    @RequestMapping(value = "/saveCustomer")
    public String doSaveCustomer(@ModelAttribute("Customer") Customer customer, Model model) {
        customerReponsitory.save(customer);
        model.addAttribute("listCustomer", customerReponsitory.findAll());
        return "customer-list";

    }

    @RequestMapping(value = "/updateCustomer")
    public String doUpdateCustomer(@ModelAttribute("Customer") Customer customer, Model model) {
        customerReponsitory.save(customer);
        model.addAttribute("listCustomer", customerReponsitory.findAll());
        return "customer-list";
    }

    @RequestMapping(value = "/customerDelete/{id}")
    public String doDeleteCustomer(@PathVariable long id, Model model) {
        customerReponsitory.deleteById(id);
        model.addAttribute("listCustomer", customerReponsitory.findAll());
        return "customer-list";
    }
}


