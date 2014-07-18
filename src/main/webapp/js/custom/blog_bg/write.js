Ext.define("Category", {
    extend: "Ext.data.Model",
    fields: [
        {name: "id",               type: "int"},
        {name: "categoryName",     type: "string"},
        {name: "parentCategoryId", type: "int"}
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
		autoLoad: true
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var width = getMainTabWidth() - 100;
	var height = document.documentElement.clientHeight - 300;
	
	var panel_addOrUpdate_article = Ext.create("Ext.form.Panel", {
		renderTo: "write",
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
  	                {name: "tag", xtype: "textfield", fieldLabel: "文章标签"}
  	            ]},
  				{xtype: "container", layout: "anchor", items: [
  	                {xtype: "label", text: "（最多添加5个标签，多个标签之间用英文“,”分隔）", style: {"line-height": "20px"}}
  	            ]}
  	        ]},
			{name: "content", xtype: "ueditor", fieldLabel: "文章内容", width: width, height: height},
			{xtype: "container", layout: {type: "hbox", pack: "center", align: "middle"}, items: [
	            {xtype: "button", width: 80, height: 27, text: "完成", margin: "0 150 0 0", handler: addOrUpdateArticleHandler},
	            {xtype: "button", width: 80, height: 27, text: "取消", handler: cancelHandler}
            ]}
        ]
	});
	
	/** ------------------------------------- handler ------------------------------------- */
	function addOrUpdateArticleHandler(){
		var values = panel_addOrUpdate_article.getForm().getValues();
		console.log(values);
	}
	
	function cancelHandler(){
		
	}
});
