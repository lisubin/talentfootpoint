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
@TableName("comment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="comment对象", description="资源实体")
public class Comment {
    
	/**评论编号*/
	@ApiModelProperty(value = "评论编号")
	private String commentId;
	/**资源编号*/
    @ApiModelProperty(value = "资源编号")
	private String resourceId;
	/**用户编号*/
    @ApiModelProperty(value = "用户编号")
	private String userId;
	/**评论标题*/
    @ApiModelProperty(value = "评论标题")
	private String commentTitle;
	/**评论内容*/
    @ApiModelProperty(value = "评论内容")
	private Object commentContent;
	/**评论时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "评论时间")
	private Date commentTime;
	/**评论类型*/
    @ApiModelProperty(value = "评论类型")
	private Integer commentType;
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
	/**回复编号*/
    @ApiModelProperty(value = "回复编号")
	private String replyId;
}
