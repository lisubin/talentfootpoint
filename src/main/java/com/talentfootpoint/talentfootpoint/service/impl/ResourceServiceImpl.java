package com.talentfootpoint.talentfootpoint.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.talentfootpoint.talentfootpoint.entity.Resource;
import com.talentfootpoint.talentfootpoint.mapper.ResourceMapper;
import com.talentfootpoint.talentfootpoint.service.IResourceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Description: 资源实体
 * @Author: jeecg-boot
 * @Date:   2019-11-26
 * @Version: V1.0
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Override
    public boolean saveBatch(Collection<Resource> entityList) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Resource> entityList) {
        return false;
    }

    @Override
    public boolean update(Wrapper<Resource> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Resource> entityList) {
        return false;
    }

    @Override
    public Resource getOne(Wrapper<Resource> queryWrapper) {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<Resource> list() {
        return null;
    }

    @Override
    public IPage<Resource> page(IPage<Resource> page) {
        return null;
    }

    @Override
    public List<Map<String, Object>> listMaps() {
        return null;
    }

    @Override
    public List<Object> listObjs() {
        return null;
    }

    @Override
    public <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public List<Object> listObjs(Wrapper<Resource> queryWrapper) {
        return null;
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<Resource> page) {
        return null;
    }

    @Override
    public QueryChainWrapper<Resource> query() {
        return null;
    }

    @Override
    public LambdaQueryChainWrapper<Resource> lambdaQuery() {
        return null;
    }

    @Override
    public UpdateChainWrapper<Resource> update() {
        return null;
    }

    @Override
    public LambdaUpdateChainWrapper<Resource> lambdaUpdate() {
        return null;
    }
}
