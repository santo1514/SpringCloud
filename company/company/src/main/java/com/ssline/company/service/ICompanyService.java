package com.ssline.company.service;

import com.ssline.company.entity.Company;

public interface ICompanyService {
    Company create(Company company);
    Company readByName(String name);
    Company update(Company company, String name);
    void delete(String name);
}
