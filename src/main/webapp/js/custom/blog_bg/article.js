Ext.define("Category", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",               type: "int"},
        {name: "categoryName",     type: "string"},
        {name: "parentCategoryId", type: "int"}
    ]
});

Ext.define("Article", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",            type: "int"},
        {name: "title",         type: "string"},
        {name: "content",       type: "string"},
        {name: "categoryId",    type: "int"},
        {name: "categoryName",  type: "string"},
        {name: "tags",  		type: "string"},
        {name: "createTimeStr", type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_categoryList = Ext.create("Ext.data.Store", {
		model: "Category",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/category/getCategoryList"
		},
		autoLoad: true,
		listeners: {
    		load: function(thiz, records, successful, eOpts){
    			editArticle();
    		}
    	}
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var width = getMainTabWidth() - 100;
	var height = document.documentElement.clientHeight - 300;
	
	var panel_addOrUpdate_article = Ext.create("Ext.form.Panel", {
		renderTo: "article",
		bodyPadding: 20,
		border: 0,
        fieldDefaults: {
        	labelWidth: 70,
        	anchor: "100%"
        },
        items: [
            {name: "id", xtype: "hidden"},
            {xtype: "container", layout:"column", margin: "0 0 5 0", items: [
				{xtype: "container", width: width - 350, margin: "0 50 0 0", layout: "anchor", items: [
                    {name: "title", xtype: "textfield", fieldLabel: "文章标题", allowBlank: false, invalidText: "请输入文章标题！", vtype: "basic_chinese"}
	            ]},
				{xtype: "container", width: 300, layout: "anchor", items: [
	                {name: "categoryId", xtype: "combobox", fieldLabel: "文章类别", allowBlank: false, invalidText: "请选择文章类别！", store: store_categoryList, forceSelection: true, editable: false, valueField: "id", displayField: "categoryName"}
	            ]}
            ]},
            {xtype: "container", layout:"column", margin: "0 0 5 0", items: [
  				{xtype: "container", width: 500, margin: "0 10 0 0", layout: "anchor", items: [
  	                {name: "tags", xtype: "textfield", fieldLabel: "文章标签", invalidText: "最多添加5个标签，多个标签之间用“,”分隔！", listeners: {change: tagsChangeHandler}}
  	            ]},
  				{xtype: "container", layout: "anchor", items: [
  	                {xtype: "label", text: "（最多添加5个标签，多个标签之间用“,”分隔）", style: {"line-height": "20px"}},
  	                {xtype: "label", text: "", id:"tag_0", margin: "0 0 0 10", style: {"line-height": "20px", "background-color": "#FFFFCC"}},
  	                {xtype: "label", text: "", id:"tag_1", margin: "0 0 0 10", style: {"line-height": "20px", "background-color": "#FFFFCC"}},
  	                {xtype: "label", text: "", id:"tag_2", margin: "0 0 0 10", style: {"line-height": "20px", "background-color": "#FFFFCC"}},
  	                {xtype: "label", text: "", id:"tag_3", margin: "0 0 0 10", style: {"line-height": "20px", "background-color": "#FFFFCC"}},
  	                {xtype: "label", text: "", id:"tag_4", margin: "0 0 0 10", style: {"line-height": "20px", "background-color": "#FFFFCC"}}
  	            ]}
  	        ]},
			{name: "content", xtype: "ueditor", fieldLabel: "文章内容", width: width, height: height},
			{xtype: "container", layout: {type: "hbox", pack: "center", align: "middle"}, items: [
	            {xtype: "button", width: 80, height: 27, text: "完成", margin: "0 150 0 0", disabled: !hasPermission("article" + permission.ADD), handler: addOrUpdateArticleHandler},
	            {xtype: "button", width: 80, height: 27, text: "取消", handler: cancelHandler}
            ]}
        ]
	});
	
	/** ------------------------------------- handler ------------------------------------- */
	function tagsChangeHandler(thiz, newValue, oldValue, eOpts){
		newValue = newValue.replace(/，+/g, ",");
		var tags = Ext.Array.unique(newValue.split(",")), tagsLen = tags.length;
		if (tagsLen > 5) {
			message.error(thiz.invalidText);
			panel_addOrUpdate_article.getForm().findField("tags").setValue(newValue.substring(0, newValue.lastIndexOf(",")));
		} else {
			for (var i = 0; i < 5; i++) {
				Ext.getCmp("tag_" + i).setText("");
			}
			for (var i = 0; i < tagsLen; i++) {
				Ext.getCmp("tag_" + i).setText(tags[i]);
			}
		}
	}
	
	function addOrUpdateArticleHandler(){
		var form = panel_addOrUpdate_article.getForm();
		var title = form.findField("title");
		var categoryId = form.findField("categoryId");
		if (!title.isValid()) {
			message.error(title.invalidText);
		} else if (!categoryId.isValid()) {
			message.error(categoryId.invalidText);
		} else {
			var url;
			if (form.findField("id").getValue()) {
				url = basePath + "resource/article/updateArticle";
			} else {
				url = basePath + "resource/article/addArticle";
			}
			form.submit({
				url: url,
				method: "POST",
				success: function(form, action){
					message.info(action.result.msg, function(){
						removeCurrentTab();
					});
				},
				failure: function(form, action){
					message.error(action.result.msg);
				}
			});
		}
	}
	
	function cancelHandler(){
		message.confirm("确定要离开当前页面？", function(){
			removeCurrentTab();
		});
	}
	
	function editArticle(){
		if (index.articleId) {
			$.post(basePath + "resource/article/getArticle", {
				id: index.articleId
			}, function(data){
				delete index.articleId;
				if (data) {
					var record = Ext.create("Article", {
						id: data.id,
						title: data.title,
						content: data.content,
						categoryId: data.categoryId,
						categoryName: data.categoryName,
						tags: data.tags,
						createTimeStr: data.createTimeStr
					});
					panel_addOrUpdate_article.getForm().reset();
					panel_addOrUpdate_article.getForm().loadRecord(record);
				}
			});
		}
	}
});
