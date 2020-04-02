package com.atguigu.gmall.index.service;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.index.feign.GmallPmsClient;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexService {

    @Autowired
    private GmallPmsClient gmallPmsClient;

    public List<CategoryEntity> queryLvl1Categories() {

        Resp<List<CategoryEntity>> listResp = this.gmallPmsClient.queryCategoriesByPidOrLevel(1, null);

        return listResp.getData();
    }

    public List<CategoryVO> querySubCategories(Long pid) {
        // 1. 判断缓存中有没有
//        String cateJson = this.redisTemplate.opsForValue().get(KEY_PREFIX + pid);
        // 2. 有，直接返回
//        if (!StringUtils.isEmpty(cateJson)) {
//            return JSON.parseArray(cateJson, CategoryVO.class);
//        }

//        RLock lock = this.redissonClient.getLock("lock" + pid);
//        lock.lock();

        // 1. 判断缓存中有没有
//        String cateJson2 = this.redisTemplate.opsForValue().get(KEY_PREFIX + pid);
        // 2. 有，直接返回
//        if (!StringUtils.isEmpty(cateJson2)) {
//            lock.unlock();
//            return JSON.parseArray(cateJson2, CategoryVO.class);
//        }

        // 查询数据库
        Resp<List<CategoryVO>> listResp = gmallPmsClient.querySubCategories(pid);
        List<CategoryVO> categoryVOS = listResp.getData();
        // 3. 查询完成后放入缓存
//        this.redisTemplate.opsForValue().set(KEY_PREFIX + pid, JSON.toJSONString(categoryVOS), 7 + new Random().nextInt(5), TimeUnit.DAYS);

//        lock.unlock();

        return listResp.getData();
    }



}
