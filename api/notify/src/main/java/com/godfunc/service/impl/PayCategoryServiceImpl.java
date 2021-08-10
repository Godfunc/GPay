package com.godfunc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.godfunc.entity.PayCategory;
import com.godfunc.mapper.PayCategoryMapper;
import com.godfunc.service.PayCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Godfunc
 * @email godfunc@outlook.com
 */
@Service
@RequiredArgsConstructor
public class PayCategoryServiceImpl extends ServiceImpl<PayCategoryMapper, PayCategory> implements PayCategoryService {

}
