package com.business.apply_system.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BusinessServiceImpl implements IBusinessService{

    @Resource
    private BusinessMapper businessMapper;

    @Override
    public Long create(Business business) {
        business.applyStatus();
        businessMapper.insert(business);
        return business.getId();
    }

    @Override
    public List<Business> get(String phone) {
        QueryWrapper<Business> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Business::getPhone,phone);
        wrapper.lambda().eq(Business::getIsDel,0);
        return businessMapper.selectList(wrapper);
    }

    @Override
    public Long update(Business business) {
        business.applyStatus();
        businessMapper.updateById(business);
        return business.getId();
    }
}
