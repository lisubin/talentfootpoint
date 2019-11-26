package com.talentfootpoint.talentfootpoint.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.talentfootpoint.talentfootpoint.entity.Comment;
import com.talentfootpoint.talentfootpoint.mapper.CommentMapper;
import com.talentfootpoint.talentfootpoint.service.ICommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Override
    public CommentMapper getBaseMapper() {
        return super.getBaseMapper();
    }

    @Override
    public boolean saveBatch(Collection<Comment> entityList) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Comment> entityList) {
        return false;
    }

    @Override
    public boolean update(Wrapper<Comment> updateWrapper) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Comment> entityList) {
        return false;
    }

    @Override
    public Comment getOne(Wrapper<Comment> queryWrapper) {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<Comment> list() {
        return null;
    }

    @Override
    public IPage<Comment> page(IPage<Comment> page) {
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
    public List<Object> listObjs(Wrapper<Comment> queryWrapper) {
        return null;
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<Comment> page) {
        return null;
    }

    @Override
    public QueryChainWrapper<Comment> query() {
        return null;
    }

    @Override
    public LambdaQueryChainWrapper<Comment> lambdaQuery() {
        return null;
    }

    @Override
    public UpdateChainWrapper<Comment> update() {
        return null;
    }

    @Override
    public LambdaUpdateChainWrapper<Comment> lambdaUpdate() {
        return null;
    }
}
