$(function(){
	var params = window.location.search.substr(1);
	var articleId = params.match(new RegExp("(^|&)id=([^&]*)(&|$)"));
	
	$(".brand").click(function(){
		window.location.href = "../blog/index.html";
	});
	
	$.post("../resource/blog/getCategoryList", function(data){
		var nav = "<li class='nav_link' categoryId='0'><a href='javascript:void(0);'>首页</a></li>";
		if (data) {
			for (var i = 0; i < data.length; i++) {
				var childRen = data[i].childRenCategory;
				if (childRen.length > 0) {
					nav += "<li class='dropdown'>";
					nav += "<a href='javascript:void(0);' class='dropdown-toggle' data-toggle='dropdown'>" + data[i].categoryName + "</a>";
					nav += "<ul class='dropdown-menu'>";
					for (var j = 0; j < childRen.length; j++) {
						nav += "<li class='nav_link' categoryId='" + childRen[j].id + "'><a href='javascript:void(0);'>" + childRen[j].categoryName + "</a></li>";
					}
					nav += "</ul>";
					nav += "</li>";
				} else {
					nav += "<li class='nav_link' categoryId='" + data[i].id + "'><a href='javascript:void(0);'>" + data[i].categoryName + "</a></li>";
				}
			}
		}
		$(".nav").html(nav);
		$(".dropdown-toggle").dropdownHover({delay: 200});
		
		$(".nav_link").click(function(){
			if (!$(this).hasClass("active")) {
				window.location.href = "../blog/index.html?categoryId=" + $(this).attr("categoryId");
			}
		});
	});
	
	$.post("../resource/blog/getAttrList", function(data){
		var attr = "<blockquote>";
		if (data) {
			var attrs = {};
			for (var i = 0; i < data.length; i++) {
				attrs[data[i].attrName] = data[i].attrValue;
			}
			if (attrs.nickname) attr += "<p>昵称：" + attrs.nickname + "</p>";
			if (attrs.qq) attr += "<p>QQ：" + attrs.qq + "</p>";
			if (attrs.github) attr += "<p>GitHub：<a href='https://github.com/" + attrs.github + "/' target='_blank'>" + attrs.github + "</a></p>";
			if (attrs.email) attr += "<p>邮箱：<a href='mailto:" + attrs.email + "'>" + attrs.email + "</a></p>";
		}
		attr += "</blockquote>";
		$("#attr").html(attr);
	});
	
	$("#search_button").click(function(){
		var searchText = $.trim($("#search_text").val());
		if (searchText) {
			window.location.href = "../blog/index.html?title=" + encodeURIComponent(searchText);
		}
		$("#search_text").val("");
	});
	
	$.post("../resource/blog/getTags", function(data){
		var tags = "<table class='table table-bordered'>";
		tags += "<tr class='success'><td>我的标签</td></tr>";
		if (data) {
			for (var url, i = 0; i < data.length; i++) {
				url = "../blog/index.html?tag=" + encodeURIComponent(data[i].tagName);
				tags += "<tr><td><a href='" + url + "'>" + data[i].tagName + "(" + data[i].articleCount + ")</a></td></tr>";
			}
		}
		tags += "</table>";
		$("#tags").html(tags);
	});
	
	$.post("../resource/blog/getArticleListByReadCount", function(data){
		var read = "<table class='table table-bordered'>";
		read += "<tr class='success'><td>阅读排行</td></tr>";
		if (data) {
			for (var url, i = 0; i < data.length; i++) {
				url = "../blog/content.html?id=" + data[i].id;
				read += "<tr><td><a href='" + url + "'>" + data[i].title + "(" + data[i].readCount + ")</a></td></tr>";
			}
		}
		read += "</table>";
		$("#read").html(read);
	});
	
	if (articleId) {
		$.post("../resource/blog/getArticleById", {
			id: articleId[2]
		}, function(data){
			var postList = "";
			if (data) {
				postList += "<div class='post_item'>";
				postList += "<h2 class='post_item_title'>" + data.title + "</a></h2>";
				postList += "<div class='post_item_content'>" + data.content + "</div>";
				if (data.prevId) postList += "<p class='post_item_prev_next'>« 上一篇：<a href='../blog/content.html?id=" + data.prevId + "'>" + data.prevTitle + "</a></p>";
				if (data.nextId) postList += "<p class='post_item_prev_next'>» 下一篇：<a href='../blog/content.html?id=" + data.nextId + "'>" + data.nextTitle + "</a></p>";
				postList += "<p class='text-right post_item_meta'>posted @ " + data.createTimeStr + " 阅读(" + data.readCount + ") 评论(" + data.commentCount + ")</p>";
				postList += "<p class='text-right post_item_tags'>";
				if (data.tags) {
					var tags = data.tags.split(",");
					for (var i = 0; i < tags.length; i++) {
						postList += "<span class='label label-success'>" + tags[i] + "</span>";
					}
				} else {
					postList += "<span class='label label-success'>" + data.categoryName + "</span>";
				}
				postList += "</p>";
				postList += "</div>";
			} else {
				postList += "<div class='post_item'>找不到该博文...</div>";
			}
			$("#post_list").html(postList);
		});
		
		showComment(articleId[2]);
	}
	
	$(".close").click(function(){
		$(".alert").hide();
	});
	
	$("#comment_button").click(function(){
		var commentName = $.trim($("#comment_name").val());
		var commentContent = $.trim($("#comment_content").val());
		if (commentName == "" || /<([^>]*)>/.test(commentName)) {
			showAlert("请输入您的姓名（昵称）！");
		} else if (commentContent == "" || /<([^>]*)>/.test(commentContent)) {
			showAlert("请输入您的评论内容！");
		} else {
			$.post("../resource/blog/addOrUpdateComment", {
				name: commentName,
				content: commentContent,
				articleId: articleId[2]
			}, function(data){
				if (data.success) {
					$("#comment_name").val("");
					$("#comment_content").val("");
					showComment(articleId[2]);
				} else {
					showAlert("评论失败，服务器异常");
				}
			});
		}
	});
	
	// 动态加载SyntaxHighlighter
	SyntaxHighlighter.autoloader.apply(null, path(
	"applescript            @shBrushAppleScript.js",
	"actionscript3 as3      @shBrushAS3.js",
	"bash shell             @shBrushBash.js",
	"coldfusion cf          @shBrushColdFusion.js",
	"cpp c                  @shBrushCpp.js",
	"c# c-sharp csharp      @shBrushCSharp.js",
	"css                    @shBrushCss.js",
	"delphi pascal          @shBrushDelphi.js",
	"diff patch pas         @shBrushDiff.js",
	"erl erlang             @shBrushErlang.js",
	"groovy                 @shBrushGroovy.js",
	"java                   @shBrushJava.js",
	"jfx javafx             @shBrushJavaFX.js",
	"js jscript javascript  @shBrushJScript.js",
	"perl pl                @shBrushPerl.js",
	"php                    @shBrushPhp.js",
	"text plain             @shBrushPlain.js",
	"ps powershell          @shBrushPowerShell.js",
	"py python              @shBrushPython.js",
	"ruby rails ror rb      @shBrushRuby.js",
	"sass scss              @shBrushSass.js",
	"scala                  @shBrushScala.js",
	"sql                    @shBrushSql.js",
	"vb vbnet               @shBrushVb.js",
	"xml xhtml xslt html    @shBrushXml.js"
	));
	SyntaxHighlighter.all();
});

function path(){
	var args = arguments, result = [];
	for (var i = 0; i < args.length; i++) {
		result.push(args[i].replace("@", "../js/lib/SyntaxHighlighter/"));
	}
	return result;
}

function showComment(articleId){
	$.post("../resource/blog/getCommentList", {
		articleId: articleId
	}, function(data){
		var commentList = "";
		if (data && data.length > 0) {
			commentList += "<h3 class='comment_title'>评论列表</h3>";
			for (var i = 0; i < data.length; i++) {
				commentList += "<div class='comment_item'>";
				commentList += "<p class='comment_name'>#" + (i + 1) + "楼<span class='comment_time'>" + data[i].createTimeStr + "</span>" + data[i].name + "</p>";
				commentList += "<p class='comment_content'>" + data[i].content + "</p>";
				commentList += "</div>";
			}
		}
		$("#comment_list").html(commentList);
	});
}

function showAlert(content){
	$(".alert span").html(content);
	$(".alert").show();
	window.setTimeout(function(){
		$(".alert").hide("normal");
	}, 4000);
}
