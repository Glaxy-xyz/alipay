package com.business.apply_system.business;

import java.util.List;

public interface IBusinessService {
    Long create(Business business);

    List<Business> get(String phone);

    Long update(Business business);
}
