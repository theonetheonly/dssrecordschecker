package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageTypeService {
    @Autowired
    PackageTypeRepository packageTypeRepository;

    public List<PackageType> getPackageTypes()
    {
        List<PackageType> packageTypes = (List<PackageType>) packageTypeRepository.findAll();
        return packageTypes;
    }

    public PackageType getPackageType(String packageTypeName) {
        return (PackageType)this.packageTypeRepository.findByPackageTypeName(packageTypeName);
    }

    public PackageType savePackageType(PackageType packageType)
    {
        return packageTypeRepository.save(packageType);
    }
}
