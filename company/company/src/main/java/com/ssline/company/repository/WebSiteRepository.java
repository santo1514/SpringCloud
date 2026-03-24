package com.ssline.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssline.company.entity.WebSite;

@Repository
public interface WebSiteRepository extends JpaRepository<WebSite, Long> {
    
}
