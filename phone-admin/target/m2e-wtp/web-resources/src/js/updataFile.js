/*
 * author: luoxu-xu.github.io;
 * date: 2016.07.18;
 * about: 一些jquery插件
 * version: 1.0.0
 */

define(['jquery', 'sparkMd5', 'upyun', 'utils'], function ($, SparkMD5, upyun, utils) {

    // 上传组件
    function UpDataFile(el, options) {
        this.element = el;
        this.options = $.extend({
            url: null, // 接收路径
            style: null, // 默认为方形，small样式类似input
            tokenUrl: '/upload/getToken', // 获取token的url
            imageUrl: 'img', // 图片路径
            imgs: null, // 默认已有的图片 一个储存url的数组
            size: null, // 上传的图片尺寸限制
            multiple: false, // 是否多图
            success: function () {}, // 上传成功的回调函数
            error: function () {} // 上传失败的回调函数
        }, options);

        if (this.element.attr('multiple')) {
            this.options.multiple = true;
        }

        this.formData = new FormData(); // 创建表单数据实例
        this.file = null; // 当前上传的文件对象
        this.fileUrl = null; // 文件路径
        this.photoBox = null; // 图片预览容器
        this.createDom();

        var that = this;

        if (this.options.imgs) {
            if (Array.isArray(this.options.imgs)) {
                this.options.imgs.map(function (i) {
                    that.render(i);
                });
            } else {
                this.options.imgs.split(',').map(function (i) {
                    that.render(i);
                });
            }
        }
        this.events();
    }

    UpDataFile.prototype = {

        // 上传到又拍云
        upData: function (data) {
            var that = this,
                config = {
                    bucket: data.bucket,
                    expiration: data.expiration,
                    signature: data.signature,
                    path: data.path
                };

            var instance = new Sand(config, this.file);
            instance.upload(config.path);
        },

        // 创建上传组件
        createDom: function () {
            this.fileEl = $('<div class="cy-file ' + (this.options.style ? ('cy-file-' + this.options.style) : '') + '"><ul class="cy-file-list"></ul><a class="cy-file-btn" href="javascript:;"><span>上传文件</span></a></div>');
            this.element.after(this.fileEl);
            this.photoBox = this.fileEl.find('.cy-file-list');
        },

        // 渲染图片
        render: function (src) {
            var img = new Image(),
                that = this;

            img.onload = function () {
                var imgEl = $('<li style="background: url(' + src + ') no-repeat center center; background-size: cover;"></li>');
                if (that.options.multiple) {
                    that.photoBox.append(imgEl);
                } else {
                    that.photoBox.html(imgEl);
                }
                var imgVal = that.element.prev().attr('value');
                if (!that.options.multiple) {
                    that.element.prev().attr('value', src);
                } else if(imgVal.length > 1 && that.options.multiple){
                    that.element.prev().attr('value', imgVal + ',' + src);
                }
            };
            img.src = src;
        },

        getToken: function (file, filemd5) {
            var fileSize = file.size,
                fileName = file.name
            fileType = file.name.split('.')[1].toLowerCase(),
                that = this;

            utils.ajax({
                url: this.options.tokenUrl,
                data: {
                    path: this.options.imageUrl,
                    extendName: fileType,
                    fileSize: fileSize,
                    fileMD5: filemd5
                },
                success: function (data) {
                    console.log(data);
                    that.fileUrl = data.obj.accessPrefix.replace(/\/$/, '') + data.obj.path;
                    that.file = file;
                    that.upData(data.obj);
                },
                error: function (data) {

                }
            });
        },

        // 添加文件
        addFile: function () {
            // 不可上传
            if (this.element.attr('disabled')) {
                return;
            }
            this.element.trigger('click');
        },

        // 获取上传文件的路径
        getObjectURL: function(file) {
            var url = null;
            if (window.createObjectURL != undefined) { // basic
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
        },

		// 限制尺寸大小
		formatImgSize: function(file, callback) {
			var that = this;
			var img = new Image();
			img.src = this.getObjectURL(file);
			img.onload = function() {
				// 图片尺寸不对
				if(img.width != that.options.size[0] || img.height != that.options.size[1]) {
					utils.alert({
						text: '您添加的图片尺寸不对，正确的图片尺寸是' + that.options.size[0] + ' x ' + that.options.size[1],
						type: 'warning',
						hideAuto: false
					});
					return;
				}
				callback.call(that);
			}
			img.onerror = function() {
				callback.call(that);
			}
		},

        // 事件处理
        events: function () {
            var that = this;

            this.fileEl.on('click', '.cy-file-btn', this.addFile.bind(this));

            document.addEventListener('uploaded', function (e) {
                that.options.success.call(that, that.fileUrl);
                if (that.options.style) {
                    that.fileUrl = null;
                    return;
                }
                that.render(that.fileUrl);
                that.fileUrl = null;
            });

            this.element.on('change', function () {
				var _files = this.files;
				if(that.options.size && _files.length == 1) {
					that.formatImgSize(_files[0], function() {
	                    createMD5(_files[0], function (data) {
	                        that.getToken(_files[0], data);
	                    });
	                    that.formData.append('updatafile', _files[0]);
					});
					return;
				}
                [].map.call(_files, function (i) {
                    createMD5(i, function (data) {
                        that.getToken(i, data);
                    });
                    that.formData.append('updatafile', i);
                });
            });
        }
    };

    // 封装成jq插件
    $.fn.extend({

        // 上传组件
        upDataFile: function (options) {
            $.each(this, function () {
                if ($(this).data('updatafile') === '1') {
                    return;
                }
                var upDataFile = new UpDataFile($(this), options);
            });
        }
    });


    /* 生成MD5
     * files 文件对象
     *
     */
    function createMD5(files, callback) {
        var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,
            file = files,
            chunkSize = 2097152, // read in chunks of 2MB
            chunks = Math.ceil(file.size / chunkSize),
            currentChunk = 0,
            spark = new SparkMD5.ArrayBuffer(),
            frOnload = function (e) {
                spark.append(e.target.result); // append array buffer
                currentChunk++;
                if (currentChunk < chunks)
                    loadNext();
                else
                    callback.call(spark, spark.end());
            },
            frOnerror = function () {
                throw new Error("\noops, 报错.");
            };

        function loadNext() {
            var fileReader = new FileReader();
            fileReader.onload = frOnload;
            fileReader.onerror = frOnerror;
            var start = currentChunk * chunkSize,
                end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        };

        loadNext();
    }

});
