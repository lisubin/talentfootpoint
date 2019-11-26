package com.talentfootpoint.talentfootpoint.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.talentfootpoint.talentfootpoint.entity.Resource;
import com.talentfootpoint.talentfootpoint.service.IResourceService;

import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 资源实体
 * @Author: jeecg-boot
 * @Date:   2019-11-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="资源实体")
@RestController
@RequestMapping("/com.talentfootpoint.talentfootpoint/resource")
public class ResourceController {
	@Autowired
	private IResourceService resourceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param resource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="资源实体-分页列表查询", notes="资源实体-分页列表查询")
	@GetMapping(value = "/list")
	public IPage<Resource> queryPageList(Resource resource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<Resource> page = new Page<Resource>(pageNo, pageSize);
		IPage<Resource> pageList = resourceService.page(page, null);
		return pageList;
	}
	
	/**
	 * 添加
	 *
	 * @param resource
	 * @return
	 */
	@ApiOperation(value="资源实体-添加", notes="资源实体-添加")
	@PostMapping(value = "/add")
	public String add(@RequestBody Resource resource) {
		resourceService.save(resource);
		return "添加成功！";
	}
	
	/**
	 * 编辑
	 *
	 * @param resource
	 * @return
	 */
	@ApiOperation(value="资源实体-编辑", notes="资源实体-编辑")
	@PutMapping(value = "/edit")
	public String edit(@RequestBody Resource resource) {
		resourceService.updateById(resource);
		return "编辑成功!";
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="资源实体-通过id删除", notes="资源实体-通过id删除")
	@DeleteMapping(value = "/delete")
	public String delete(@RequestParam(name="id",required=true) String id) {
		resourceService.removeById(id);
		return "删除成功!";
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */

	@ApiOperation(value="资源实体-批量删除", notes="资源实体-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public String deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.resourceService.removeByIds(Arrays.asList(ids.split(",")));
		return "批量删除成功！";
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */

	@ApiOperation(value="资源实体-通过id查询", notes="资源实体-通过id查询")
	@GetMapping(value = "/queryById")
	public Resource queryById(@RequestParam(name="id",required=true) String id) {
		Resource resource = resourceService.getById(id);
		return resource;
	}

}
