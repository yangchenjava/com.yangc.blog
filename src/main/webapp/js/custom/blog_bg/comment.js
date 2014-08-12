Ext.define("Comment", {
    extend: "Ext.data.Model",
    fields: [
		{name: "id",   	        type: "int"},
		{name: "name",      	type: "string"},
		{name: "content",       type: "string"},
		{name: "articleId",     type: "int"},
		{name: "articleTitle",  type: "string"},
		{name: "ipAddress",     type: "string"},
		{name: "createTimeStr", type: "string"}
    ]
});

Ext.onReady(function(){
	/** ------------------------------------- store ------------------------------------- */
	var store_commentGrid = Ext.create("Ext.data.Store", {
		model: "Comment",
		pageSize: 20,
		proxy: {
			type: "ajax",
			actionMethods: {
				create: "POST", read: "POST", update: "POST", destroy: "POST"
			},
			url: basePath + "resource/comment/getCommentList_page",
			reader: {
            	root: "dataGrid",
                totalProperty: "totalCount"
            }
		},
		autoLoad: true
	});
	
	/** ------------------------------------- view ------------------------------------- */
	var grid_comment = Ext.create("Ext.grid.Panel", {
        renderTo: "comment",
		store: store_commentGrid,
		width: "100%",
		height: document.documentElement.clientHeight - 125,
		border: false,
        collapsible: false,
        multiSelect: false,
        scroll: false,
        selModel: Ext.create("Ext.selection.CheckboxModel", {
//        	mode: "SIMPLE"
        	mode: "MULTI"
        }),
        viewConfig: {
            stripeRows: true,
            enableTextSelection: true
        },
        columns: [
            {text: "序号", width: 50, align: "center", xtype: "rownumberer"},
            {text: "评论人", flex: 1, align: "center", dataIndex: "name"},
            {text: "评论文章", flex: 2, align: "center", dataIndex: "articleTitle"},
            {text: "IP地址", flex: 1, align: "center", dataIndex: "ipAddress"},
            {text: "评论时间", flex: 1, align: "center", dataIndex: "createTimeStr"}
        ],
        tbar: new Ext.Toolbar({
        	height: 30,
			items: [
		        {width: 5,  disabled: true},
		        {width: 55, text: "查看", handler: infoComment, disabled: !hasPermission("comment" + permission.SEL), icon: basePath + "js/lib/ext4.2/icons/info.png"}, "-",
		        {width: 55, text: "删除", handler: deleteComment, disabled: !hasPermission("comment" + permission.DEL), icon: basePath + "js/lib/ext4.2/icons/delete.gif"}
		    ]
        }),
        bbar: Ext.create("Ext.PagingToolbar", {
        	store: store_commentGrid,
            displayInfo: true,
            displayMsg: "当前显示{0} - {1}条，共 {2} 条记录",
            emptyMsg: "当前没有任何记录"
        })
    });
	
	Ext.define("panel_info_comment", {
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
			{name: "articleTitle", xtype: "textfield", fieldLabel: "评论文章", readOnly: true},
			{xtype: "container", layout:"column", items: [
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {name: "ipAddress", xtype: "textfield", fieldLabel: "IP地址", readOnly: true}
                ]},
                {xtype: "container", columnWidth:.5, layout: "anchor", items: [
                    {name: "createTimeStr", xtype: "textfield", fieldLabel: "评论时间", readOnly: true}
                ]}
            ]},
			{name: "name", xtype: "textfield", fieldLabel: "评论人", readOnly: true},
			{name: "content", xtype: "textarea", fieldLabel: "评论内容", readOnly: true}
		]
	});
    Ext.define("window_info_comment", {
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
			{text: "关闭", handler: function(){this.up("window").close();}}
        ]
	});
    
    /** ------------------------------------- handler ------------------------------------- */
    function refreshCommentGrid(){
    	grid_comment.getSelectionModel().deselectAll();
    	store_commentGrid.currentPage = 1;
    	store_commentGrid.load();
    }
    
	function infoComment(){
		if (grid_comment.getSelectionModel().hasSelection()) {
			var record = grid_comment.getSelectionModel().getSelection()[0];
			
			var panel_info_comment = Ext.create("panel_info_comment");
			panel_info_comment.getForm().loadRecord(record);
			
			var window_info_comment = Ext.create("window_info_comment");
			window_info_comment.add(panel_info_comment);
			window_info_comment.setTitle("详细信息");
			window_info_comment.show();
		} else {
			message.info("请先选择数据再操作！");
		}
	}
	
	function deleteComment(){
		if (grid_comment.getSelectionModel().hasSelection()) {
			message.confirm("是否删除记录？", function(){
				var records = grid_comment.getSelectionModel().getSelection(), commentIds = [];
				for (var i = 0; i < records.length; i++) {
					commentIds.push(records[i].get("id"));
				}
				
				$.post(basePath + "resource/comment/delComments", {
					commentIds: commentIds.toString()
				}, function(data){
					if (data.success) {
						message.info(data.message);
						refreshCommentGrid();
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
