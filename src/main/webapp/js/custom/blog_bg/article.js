Ext.define("CategoryTree", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",               type: "int"},
        {name: "text",         	   type: "string"},
        {name: "leaf",         	   type: "boolean"},
        {name: "categoryId",       type: "int"},
        {name: "categoryName",     type: "string"},
        {name: "parentCategoryId", type: "int"},
        {name: "serialNum",    	   type: "int"}
    ]
});

Ext.define("Article", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",            type: "int"},
        {name: "title",         type: "string"},
        {name: "categoryName",  type: "string"},
        {name: "createTimeStr", type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_categoryTree = Ext.create("Ext.data.TreeStore", {
        model: "CategoryTree",
        nodeParam: "parentCategoryId",
        proxy: {
            type: "ajax",
            actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
            url: basePath + "resource/category/getCategoryTreeList"
        },
        root: {
        	id: "0",
	        text: "文章类别",
	        categoryId: "0",
	        categoryName: "文章类别",
	        expanded: true
	    },
        autoLoad: true
    });
	
	var store_articleGrid = Ext.create("Ext.data.Store", {
		model: "Article",
		pageSize: 20,
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/article/getArticleListBycategoryId_page",
			extraParams: {
				"categoryId": 0
			},
			reader: {
            	root: "dataGrid",
                totalProperty: "totalCount"
            }
		},
		autoLoad: true
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var tree_category = Ext.create("Ext.tree.Panel", {
        store: store_categoryTree,
        width: 300,
        height: document.documentElement.clientHeight - 125,
        border: false,
        style: {
			borderWidth: "0 2px 0 0",
			borderStyle: "solid"
		},
        rootVisible: true,
        listeners: {
        	itemclick: refreshArticleGrid
        },
        viewConfig: {
        	stripeRows: true,
        	plugins: {
        		ptype: "treeviewdragdrop",
        		containerScroll: true
        	},
        	listeners: {
        		beforedrop: function(node, data, overModel, dropPosition, dropHandlers){
        			var srcModel = data.records[0];
        			if (srcModel.get("leaf") && hasPermission("category" + permission.UPD)) {
        				var parentCategoryId = dropPosition == "append" ? overModel.get("id") : overModel.get("parentCategoryId");
        				$.post(basePath + "resource/category/updateParentCategoryId", {
        					id: srcModel.get("id"),
        					parentCategoryId: parentCategoryId
        				}, function(data){
        					if (data.success) {
        						message.info(data.message);
        					} else {
        						message.error(data.message);
        					}
        				});
        			} else {
        				return false;
        			}
        		},
        		itemcontextmenu: function(thiz, record, item, index, e, eOpts){
        			if (record.get("depth") >= 2) {
        				Ext.getCmp("createCategory").setDisabled(true);
        			} else {
        				Ext.getCmp("createCategory").setDisabled(!hasPermission("category" + permission.ADD));
        			}
        			if (record.get("id") == 0) {
        				Ext.getCmp("updateCategory").setDisabled(true);
        				Ext.getCmp("deleteCategory").setDisabled(true);
        			} else {
        				Ext.getCmp("updateCategory").setDisabled(!hasPermission("category" + permission.UPD));
        				Ext.getCmp("deleteCategory").setDisabled(!hasPermission("category" + permission.DEL));
        			}
        			e.stopEvent();
        			menu_category.showAt(e.getXY());
                    return false;
        		}
        	}
        }
    });
	
	var menu_category = Ext.create("Ext.menu.Menu", {
        items: [
            {id: "createCategory", text: "创建子类别", icon: basePath + "js/lib/ext4.2/icons/add.gif", handler: createCategory},
            {id: "updateCategory", text: "修改类别", icon: basePath + "js/lib/ext4.2/icons/edit_task.png", handler: updateCategory},
            {id: "deleteCategory", text: "删除类别", icon: basePath + "js/lib/ext4.2/icons/delete.gif", handler: deleteCategory}
        ]
    });
	
	Ext.define("panel_addOrUpdate_category", {
		extend: "Ext.form.Panel",
        bodyPadding: 20,
        bodyBorder: false,
        frame: false,
		header: false,
        fieldDefaults: {
            labelAlign: "right",
            labelWidth: 70,
            anchor: "100%"
        },
        items: [
			{id: "addOrUpdate_categoryId", name: "id", xtype: "hidden"},
			{id: "addOrUpdate_parentCategoryId", name: "parentCategoryId", xtype: "hidden"},
			{id: "addOrUpdate_categoryName", name: "categoryName", xtype: "textfield", fieldLabel: "类别名称", allowBlank: false, invalidText: "请输入类别名称！", vtype: "basic_chinese"},
			{id: "addOrUpdate_serialNum", name: "serialNum", xtype: "numberfield", fieldLabel: "顺序", allowBlank: false, invalidText: "请输入顺序！", minValue: 1}
		]
	});
    Ext.define("window_addOrUpdate_category", {
    	extend: "Ext.window.Window",
		layout: "fit",
		width: 500,
		bodyMargin: 10,
		border: false,
		closable: true,
		modal: true,
		plain: true,
		resizable: false,
		items: [],
		buttonAlign: "right",
        buttons: [
            {text: "确定", handler: addOrUpdateCategoryHandler}, "-",
			{text: "取消", handler: function(){this.up("window").close();}}
        ]
	});
    
    var grid_article = Ext.create("Ext.grid.Panel", {
		store: store_articleGrid,
		width: getMainTabWidth() - 302,
		height: document.documentElement.clientHeight - 125,
		border: false,
        collapsible: false,
        multiSelect: false,
        scroll: false,
        viewConfig: {
            stripeRows: true,
            enableTextSelection: true
        },
        columns: [
            {text: "序号", width: 50, align: "center", xtype: "rownumberer"},
            {text: "文章标题", flex: 2, align: "center", dataIndex: "title"},
            {text: "类别名称", flex: 1, align: "center", dataIndex: "categoryName"},
            {text: "创建时间", flex: 2, align: "center", dataIndex: "createTimeStr"}
        ],
        tbar: new Ext.Toolbar({
        	height: 30,
			items: [
		        {width: 5,  disabled: true},
		        {width: 55, text: "修改", handler: updateArticle, disabled: !hasPermission("article" + permission.UPD), icon: basePath + "js/lib/ext4.2/icons/edit_task.png"}, "-",
		        {width: 55, text: "删除", handler: deleteArticle, disabled: !hasPermission("article" + permission.DEL), icon: basePath + "js/lib/ext4.2/icons/delete.gif"}
		    ]
        }),
        bbar: Ext.create("Ext.PagingToolbar", {
        	store: store_articleGrid,
            displayInfo: true,
            displayMsg: "当前显示{0} - {1}条，共 {2} 条记录",
            emptyMsg: "当前没有任何记录"
        })
    });
	
	Ext.create("Ext.form.Panel", {
		renderTo: "article",
		width: "100%",
		height: document.documentElement.clientHeight - 125,
        border: false,
		header: false,
		layout: "column",
        items: [tree_category, grid_article]
	});
	
	/** ------------------------------------- handler ------------------------------------- */
	function refreshCategoryTree(){
		store_categoryTree.load();
		refreshArticleGrid();
    }
	
	function createCategory(){
		if (tree_category.getSelectionModel().hasSelection()) {
			var record = tree_category.getSelectionModel().getSelection()[0];
			
			var panel_addOrUpdate_category = Ext.create("panel_addOrUpdate_category");
			Ext.getCmp("addOrUpdate_parentCategoryId").setValue(record.get("id"));
			
			var window_addOrUpdate_category = Ext.create("window_addOrUpdate_category");
			window_addOrUpdate_category.add(panel_addOrUpdate_category);
			window_addOrUpdate_category.setTitle("创建");
			window_addOrUpdate_category.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function updateCategory(){
		if (tree_category.getSelectionModel().hasSelection()) {
			var record = tree_category.getSelectionModel().getSelection()[0];
			
			var panel_addOrUpdate_category = Ext.create("panel_addOrUpdate_category");
			panel_addOrUpdate_category.getForm().loadRecord(record);
			
			var window_addOrUpdate_category = Ext.create("window_addOrUpdate_category");
			window_addOrUpdate_category.add(panel_addOrUpdate_category);
			window_addOrUpdate_category.setTitle("修改");
			window_addOrUpdate_category.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function deleteCategory(){
		if (tree_category.getSelectionModel().hasSelection()) {
			message.confirm("是否删除记录？", function(){
				var record = tree_category.getSelectionModel().getSelection()[0];
				$.post(basePath + "resource/category/delCategory", {
					id: record.get("id")
				}, function(data){
					if (data.success) {
						message.info(data.message);
						refreshCategoryTree();
					} else {
						message.error(data.message);
					}
				});
			});
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function addOrUpdateCategoryHandler(){
		var categoryName = Ext.getCmp("addOrUpdate_categoryName");
		var serialNum = Ext.getCmp("addOrUpdate_serialNum");
		if (!categoryName.isValid()) {
			message.error(categoryName.invalidText);
		} else if (!serialNum.isValid()) {
			message.error(serialNum.invalidText);
		} else {
			var url;
			if (Ext.getCmp("addOrUpdate_categoryId").getValue()) {
				url = basePath + "resource/category/updateCategory";
			} else {
				url = basePath + "resource/category/addCategory";
			}
			var window_addOrUpdate_category = this.up("window");
			window_addOrUpdate_category.items.items[0].getForm().submit({
				url: url,
				method: "POST",
				success: function(form, action){
					window_addOrUpdate_category.close();
					message.info(action.result.msg);
					refreshCategoryTree();
				},
				failure: function(form, action){
					message.error(action.result.msg);
				}
			});
		}
	}
	
	function refreshArticleGrid(){
		grid_article.getSelectionModel().deselectAll();
		var categoryId = 0;
		if (tree_category.getSelectionModel().hasSelection()) {
			categoryId = tree_category.getSelectionModel().getSelection()[0].get("id");
		}
		store_articleGrid.currentPage = 1;
		store_articleGrid.proxy.extraParams = {"categoryId": categoryId};
		store_articleGrid.load();
	}
	
	function updateArticle(){
		if (grid_article.getSelectionModel().hasSelection()) {
			
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function deleteArticle(){
		if (grid_article.getSelectionModel().hasSelection()) {
			message.confirm("是否删除记录？", function(){
				var record = grid_article.getSelectionModel().getSelection()[0];
				$.post(basePath + "resource/article/delArticle", {
					id: record.get("id")
				}, function(data){
					if (data.success) {
						message.info(data.message);
						refreshArticleGrid();
					} else {
						message.error(data.message);
					}
				});
			});
		} else {
			message.info("请先选择数据再操作！");
		}
	}
});
