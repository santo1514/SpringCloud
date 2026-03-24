package com.ssline.msreport.repository;

import java.util.Optional;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ssline.msreport.bean.LoadBalancerConfiguration;
import com.ssline.msreport.model.Company;

@FeignClient(name="company")
@LoadBalancerClient(name="company", configuration = LoadBalancerConfiguration.class)
public interface ICompanyRepository {

    @GetMapping("/company-crud/company/{name}")
    Optional<Company> findByName(@PathVariable String name);

    @PostMapping("/company-crud/company/")
    Optional<Company> postByName(@RequestBody Company company);

    @DeleteMapping("/company-crud/company/{name}")
    void deleteByName(@PathVariable String name);
}
