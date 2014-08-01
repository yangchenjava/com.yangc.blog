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
	}
});
