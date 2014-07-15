package com.yangc.blog.resource;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.ResultBean;
import com.yangc.blog.bean.oracle.TBlogAttr;
import com.yangc.blog.service.AttrService;
import com.yangc.exception.WebApplicationException;
import com.yangc.system.bean.oracle.Permission;

@Controller
@RequestMapping("/attr")
public class AttrResource {

	private static final Logger logger = Logger.getLogger(AttrResource.class);

	@Autowired
	private AttrService attrService;

	/**
	 * @功能: 查询所有属性信息
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "getAttrList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("attr:" + Permission.SEL)
	public List<TBlogAttr> getAttrList() {
		logger.info("getAttrList");
		return this.attrService.getAttrList();
	}

	/**
	 * @功能: 添加属性
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "addAttr", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("attr:" + Permission.ADD)
	public ResultBean addAttr(String attrName, String attrValue, String description) {
		logger.info("addAttr - attrName=" + attrName + ", attrValue=" + attrValue + ", description=" + description);
		try {
			this.attrService.addOrUpdateAttr(null, attrName, attrValue, description);
			return new ResultBean(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改属性
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "updateAttr", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("attr:" + Permission.UPD)
	public ResultBean updateAttr(Long id, String attrName, String attrValue, String description) {
		logger.info("updateAttr - id=" + id + ", attrName=" + attrName + ", attrValue=" + attrValue + ", description=" + description);
		try {
			this.attrService.addOrUpdateAttr(id, attrName, attrValue, description);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 删除属性
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午3:02:44
	 * @return
	 */
	@RequestMapping(value = "delAttr", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("attr:" + Permission.DEL)
	public ResultBean delAttr(Long id) {
		logger.info("delAttr - id=" + id);
		try {
			this.attrService.delAttr(id);
			return new ResultBean(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
