Ext.define("Attr", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	        type: "int"},
		{name: "attrName",      type: "string"},
		{name: "attrValue",     type: "string"},
		{name: "description", 	type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_attrGrid = Ext.create("Ext.data.Store", {
		model: "Attr",
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/attr/getAttrList"
		},
		autoLoad: true
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var rowEditing = Ext.create("Ext.grid.plugin.RowEditing", {
		saveBtnText: "保存",
        cancelBtnText: "取消",
        listeners: {
        	edit: addOrUpdateAttrHandler
        }
    });
	
	var grid_attr = Ext.create("Ext.grid.Panel", {
        renderTo: "attr",
		store: store_attrGrid,
		width: "100%",
		height: document.documentElement.clientHeight - 127,
		border: false,
        collapsible: false,
        multiSelect: false,
        viewConfig: {
            stripeRows: true,
            enableTextSelection: true
        },
        plugins: [rowEditing],
        columns: [
            {text: "序号", width: 50, align: "center", xtype: "rownumberer"},
            {text: "属性名", flex: 1, align: "center", dataIndex: "attrName", editor: {allowBlank: false, vtype: "basic_chinese"}},
            {text: "属性值", flex: 1, align: "center", dataIndex: "attrValue", editor: {allowBlank: false, vtype: "basic_chinese"}},
            {text: "描述", flex: 2, align: "center", dataIndex: "description", editor: {allowBlank: true, vtype: "basic_chinese"}}
        ],
        tbar: new Ext.Toolbar({
        	height: 30,
			items: [
		        {width: 5,  disabled: true},
		        {width: 55, text: "刷新", handler: refreshAttrGrid, disabled: !hasPermission("attr" + permission.SEL), icon: basePath + "js/lib/ext4.2/icons/refresh.gif"}, "-",
		        {width: 55, text: "创建", handler: createAttr, disabled: !hasPermission("attr" + permission.ADD), icon: basePath + "js/lib/ext4.2/icons/add.gif"}, "-",
		        {width: 55, text: "删除", handler: deleteAttr, disabled: !hasPermission("attr" + permission.DEL), icon: basePath + "js/lib/ext4.2/icons/delete.gif"}
		    ]
        })
    });
	
    /** ------------------------------------- handler ------------------------------------- */
    function refreshAttrGrid(){
    	grid_attr.getSelectionModel().deselectAll();
		store_attrGrid.load();
    }
    
    function createAttr(){
    	rowEditing.cancelEdit();
    	
    	var record = Ext.create("Attr", {
    		attrName: "属性名",
    		attrValue: "属性值",
    		description: "描述"
        });
    	var i = store_attrGrid.getCount();
    	store_attrGrid.insert(i, record);
        rowEditing.startEdit(i, 0);
    }
    
    function deleteAttr(){
    	if (grid_attr.getSelectionModel().hasSelection()) {
    		rowEditing.cancelEdit();
    		var record = grid_attr.getSelectionModel().getSelection()[0];
    		if (record.get("id")) {
    			message.confirm("是否删除记录？", function(){
    				$.post(basePath + "resource/attr/delAttr", {
    					id: record.get("id"),
    				}, function(data){
    					if (data.success) {
    						message.info(data.message);
    						refreshAttrGrid();
    					} else {
    						message.error(data.message);
    					}
    				});
    			});
    		} else {
    			store_attrGrid.remove(record);
    		}
		} else {
			message.info("请先选择数据再操作！");
		}
    }
    
    function addOrUpdateAttrHandler(editor, context, eOpts){
    	var record = context.record, url;
		if (record.get("id")) {
			url = basePath + "resource/attr/updateAttr";
		} else {
			url = basePath + "resource/attr/addAttr";
		}
    	$.post(url, {
			id: record.get("id"),
			attrName: record.get("attrName"),
			attrValue: record.get("attrValue"),
			description: record.get("description")
		}, function(data){
			if (data.success) {
				message.info(data.message);
				refreshAttrGrid();
			} else {
				message.error(data.message);
			}
		});
    }
});
