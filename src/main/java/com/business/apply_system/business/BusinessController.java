package com.business.apply_system.business;

import com.business.apply_system.common.result.RestResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private IBusinessService businessService;


    @PostMapping("/create")
    public RestResult<Long> create(@RequestBody Business business){
        return RestResult.getSuccessResult(businessService.create(business));
    }

    @GetMapping("/get")
    public RestResult<List<Business>> get(@RequestParam String phone){
        return RestResult.getSuccessResult(businessService.get(phone));
    }

    @PatchMapping("/update")
    public RestResult<Long> update(@RequestBody Business business){
        return RestResult.getSuccessResult(businessService.update(business));
    }
}
