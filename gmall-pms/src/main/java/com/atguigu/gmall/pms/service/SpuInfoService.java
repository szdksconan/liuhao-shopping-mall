package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.SpuInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.io.FileNotFoundException;


/**
 * spu信息
 *
 * @author liuhao
 * @email szdksconan@outlook.com
 * @date 2020-03-25 17:28:42
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo querySpuPage(QueryCondition queryCondition,Long catId);

    void bigSave(SpuInfoVO spuInfoVO) throws FileNotFoundException;
}

