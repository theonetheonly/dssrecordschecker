package com.sgasecurity.dssrecordschecker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallationSiteRepository extends JpaRepository<InstallationSite, Long> {
    InstallationSite findBySystemCustomerNo(String systemCustomerNo);
    InstallationSite findByUniqueSiteId(String uniqueSiteId);
    InstallationSite findByCustomerId(long customerId);
    InstallationSite findById(long id);
    long countByPackageTypeName(String packageTypeName);
}
