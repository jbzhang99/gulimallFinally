package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrDao attrDao;

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageCatelogBaseAttrs(QueryCondition queryCondition, Long catId,Integer attrType) {

        //1、获取分页条件
        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(queryCondition);

        //2、构造查询条件
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catId)
                .eq("attr_type",attrType);

        IPage<AttrEntity> data = this.page(page, wrapper);
        return new PageVo(data);

    }

    //<tx:annotation-driven/>
    @Transactional
    @Override
    public void saveAttrAndRelation(AttrSaveVo attr) {
        //1、先把属性保存在属性表中，
        AttrEntity attrEntity = new AttrEntity();
        //对拷属性值
        BeanUtils.copyProperties(attr,attrEntity);
        attrDao.insert(attrEntity);

        //2、在建立关联关系
        Long attrId = attrEntity.getAttrId();
        Long attrGroupId = attr.getAttrGroupId();
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrId);
        relationEntity.setAttrGroupId(attrGroupId);
        relationDao.insert(relationEntity);
        //int i = 10/0;
    }

}