package com.business.apply_system.business;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business")
public class Business {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 电话
     */
    private String phone;

    /**
     * 状态  1 未支付 2已支付 3 已处理
     */
    private Integer status;

    private String statusName;

    /**
     * 价格
     */
    private Long price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyDate;

    private Integer isDel;

    private final static ConcurrentMap<Integer, String> STATUS_MAP = new ConcurrentHashMap<>();


    static {
        String[] types = "未支付,已支付,已处理"
                .split(",");
        for (int i = 0; i < types.length; i++) {
            STATUS_MAP.put(i + 1, types[i]);
        }
    }

    public void applyStatus() {

        if (ObjectUtil.isNotEmpty(this.status)) {
            this.statusName =STATUS_MAP.get(this.getStatus());
        }

    }
}
