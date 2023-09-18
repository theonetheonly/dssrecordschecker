package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallationSiteService {
    @Autowired
    InstallationSiteRepository installationSiteRepository;

    public InstallationSite getInstallationSiteBySystemCustomerNo(String systemCustomerNo) {
        return (InstallationSite)this.installationSiteRepository.findBySystemCustomerNo(systemCustomerNo);
    }

    public InstallationSite getInstallationSiteByCustomerId(long customerId) {
        return (InstallationSite)this.installationSiteRepository.findByCustomerId(customerId);
    }

    public InstallationSite getInstallationSiteByUniqueSiteId(String uniqueSiteId) {
        return installationSiteRepository.findByUniqueSiteId(uniqueSiteId);
    }

    public InstallationSite saveInstallationSite(InstallationSite installationSite)
    {
        return installationSiteRepository.save(installationSite);
    }

    public InstallationSite getInstallationSiteById(long id) {
        return installationSiteRepository.findById(id);
    }

    public List<InstallationSite> getAllInstallationSites() {
        return (List<InstallationSite>) this.installationSiteRepository.findAll();
    }
}
