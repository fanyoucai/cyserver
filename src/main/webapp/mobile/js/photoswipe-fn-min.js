var initPhotoSwipeFromDOM=function(a){var h,i,j,b=function(a){var e,f,h,i,j,b=a.childNodes,c=b.length,d=[];for(i=0;c>i;i++)e=b[i],1===e.nodeType&&(f=e.children[0],h={src:f.getAttribute("href")},j=new Image,j.src=h.src,h.w=j.width,h.h=j.height,document.querySelectorAll(".board-list").length>0?h.title=e.parentNode.previousSibling.previousSibling.children[2].innerHTML:document.querySelectorAll(".board-detail").length>0&&(h.title=e.parentNode.previousSibling.children[2].innerHTML),f.children.length>0&&(h.msrc=f.children[0].style.backgroundImage.slice(4,-1)),h.el=e,d.push(h));return d},c=function k(a,b){return a&&(b(a)?a:k(a.parentNode,b))},d=function(a){var b,d,j,e,g,h,i,k;if(a=a||window.event,a.preventDefault?a.preventDefault():a.returnValue=!1,b=a.target||a.srcElement,d=c(b,function(a){return a.tagName&&"LI"===a.tagName.toUpperCase()})){for(e=d.parentNode,g=d.parentNode.childNodes,h=g.length,i=0,k=0;h>k;k++)if(1===g[k].nodeType){if(g[k]===d){j=i;break}i++}return j>=0&&f(j,e),!1}},e=function(){var c,d,e,a=window.location.hash.substring(1),b={};if(a.length<5)return b;for(c=a.split("&"),d=0;d<c.length;d++)c[d]&&(e=c[d].split("="),e.length<2||(b[e[0]]=e[1]));return b.gid&&(b.gid=parseInt(b.gid,10)),b},f=function(a,c,d,e){var g,j,f=document.querySelectorAll(".pswp")[0],i=b(c),h={galleryUID:c.getAttribute("data-pswp-uid"),getThumbBoundsFn:function(a){var b=i[a].el.getElementsByTagName("span")[0],c=window.pageYOffset||document.documentElement.scrollTop,d=b.getBoundingClientRect();return{x:d.left,y:d.top+c,w:d.width}}};if(e)if(h.galleryPIDs){for(j=0;j<i.length;j++)if(i[j].pid==a){h.index=j;break}}else h.index=parseInt(a,10)-1;else h.index=parseInt(a,10);isNaN(h.index)||(d&&(h.showAnimationDuration=0),g=new PhotoSwipe(f,PhotoSwipeUI_Default,i,h),g.init())},g=document.querySelectorAll(a);for(h=0,i=g.length;i>h;h++)g[h].setAttribute("data-pswp-uid",h+1),g[h].onclick=d;j=e(),j.pid&&j.gid&&f(j.pid,g[j.gid-1],!0,!0)};