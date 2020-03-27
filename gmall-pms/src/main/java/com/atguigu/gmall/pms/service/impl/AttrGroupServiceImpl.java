package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    /**
     * 根据分类ID查询SPU分组信息(分页）
     * @param condition
     * @param catId
     * @return
     */
    @Override
    public PageVo queryGroupByPage(QueryCondition condition,Long catId) {
        QueryWrapper<AttrGroupEntity>  queryWrapper =  new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(condition),
                queryWrapper
        );

        return new PageVo(page);
    }

    @Override
    public GroupVO queryGroupWithAttrByGid(Long gid) {
        GroupVO groupVO = new GroupVO();
        // 查询group
        AttrGroupEntity groupEntity = this.getById(gid);
        BeanUtils.copyProperties(groupEntity,groupVO);
        // 根据查询关联关系,并可以获取attrIds
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("attr_group_id",gid);
        List<AttrAttrgroupRelationEntity> list = attrAttrgroupRelationDao.selectList(queryWrapper);
        // 根据attrids 查询规格参数
        if(CollectionUtils.isEmpty(list)){
            return groupVO;
        }
        groupVO.setRelations(list);
        List<Long> attrIds = list.stream().map(entity->entity.getAttrId()).collect(Collectors.toList());
        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
        groupVO.setAttrEntities(attrEntities);
        return groupVO;
    }

}