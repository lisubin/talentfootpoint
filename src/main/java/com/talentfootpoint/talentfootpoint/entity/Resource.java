package com.talentfootpoint.talentfootpoint.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 资源实体
 * @Author: jeecg-boot
 * @Date:   2019-11-26
 * @Version: V1.0
 */
@Data
@TableName("resource")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="resource对象", description="资源实体")
public class Resource {
    
	/**资源编号*/
    @ApiModelProperty(value = "资源编号")
	private String resourceId;
	/**用户编号*/
    @ApiModelProperty(value = "用户编号")
	private String userId;
	/**资源标题*/
    @ApiModelProperty(value = "资源标题")
	private String resourceTitle;
	/**资源内容*/
    @ApiModelProperty(value = "资源内容")
	private Object resourceContent;
	/**资源图片*/
    @ApiModelProperty(value = "资源图片")
	private Object resourceImages;
	/**资源上传时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "资源上传时间")
	private Date resourceUploadTime;
	/**资源类型*/
    @ApiModelProperty(value = "资源类型")
	private Integer resourceType;
	/**资源介绍*/
    @ApiModelProperty(value = "资源介绍")
	private Object resourceIntroduce;
	/**创建用户*/
    @ApiModelProperty(value = "创建用户")
	private String createUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更改时间")
	private Date updateTime;
	/**备注信息*/
    @ApiModelProperty(value = "备注信息")
	private String other;
}
