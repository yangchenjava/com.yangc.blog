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
import com.yangc.blog.bean.oracle.CategoryTree;
import com.yangc.blog.bean.oracle.TBlogCategory;
import com.yangc.blog.service.CategoryService;
import com.yangc.exception.WebApplicationException;
import com.yangc.system.bean.oracle.Permission;

@Controller
@RequestMapping("/category")
public class CategoryResource {

	private static final Logger logger = Logger.getLogger(CategoryResource.class);

	@Autowired
	private CategoryService categoryService;

	/**
	 * @功能: 根据父id查询子类别
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "getCategoryTreeList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("category:" + Permission.SEL)
	public List<CategoryTree> getCategoryTreeList(Long parentCategoryId) {
		logger.info("getCategoryTreeList - parentCategoryId=" + parentCategoryId);
		return this.categoryService.getCategoryTreeListByParentCategoryId(parentCategoryId);
	}

	/**
	 * @功能: 查询所有类别(用于下拉)
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "getCategoryList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("category:" + Permission.SEL)
	public List<TBlogCategory> getCategoryList() {
		logger.info("getCategoryList");
		return this.categoryService.getCategoryListSameLevel();
	}

	/**
	 * @功能: 添加类别
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "addCategory", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("category:" + Permission.ADD)
	public ResultBean addCategory(String categoryName, Long serialNum, Long parentCategoryId) {
		logger.info("addCategory - categoryName=" + categoryName + ", serialNum=" + serialNum + ", parentCategoryId=" + parentCategoryId);
		try {
			this.categoryService.addOrUpdateCategory(null, categoryName, serialNum, parentCategoryId);
			return new ResultBean(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改类别
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "updateCategory", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("category:" + Permission.UPD)
	public ResultBean updateCategory(Long id, String categoryName, Long serialNum, Long parentCategoryId) {
		logger.info("updateCategory - id=" + id + ", categoryName=" + categoryName + ", serialNum=" + serialNum + ", parentCategoryId=" + parentCategoryId);
		try {
			this.categoryService.addOrUpdateCategory(id, categoryName, serialNum, parentCategoryId);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改所属父类别
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "updateParentCategoryId", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("category:" + Permission.UPD)
	public ResultBean updateParentCategoryId(Long id, Long parentCategoryId) {
		logger.info("updateParentCategoryId - id=" + id + ", parentCategoryId=" + parentCategoryId);
		try {
			this.categoryService.updateParentCategoryId(id, parentCategoryId);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 删除类别
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "delCategory", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("category:" + Permission.DEL)
	public ResultBean delCategory(Long id) {
		logger.info("delCategory - id=" + id);
		ResultBean resultBean = new ResultBean();
		try {
			this.categoryService.delCategory(id);
			resultBean.setSuccess(true);
			resultBean.setMessage("删除成功");
			return resultBean;
		} catch (IllegalStateException e) {
			resultBean.setSuccess(false);
			resultBean.setMessage(e.getMessage());
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
