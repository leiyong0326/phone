define(['jquery'], function ($) {

    var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
        return typeof obj;
    } : function (obj) {
        return obj && typeof Symbol === "function" && obj.constructor === Symbol ? "symbol" : typeof obj;
    };

    var _slicedToArray = function () {
        function sliceIterator(arr, i) {
            var _arr = [];
            var _n = true;
            var _d = false;
            var _e = undefined;
            try {
                for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) {
                    _arr.push(_s.value);
                    if (i && _arr.length === i) break;
                }
            } catch (err) {
                _d = true;
                _e = err;
            } finally {
                try {
                    if (!_n && _i["return"]) _i["return"]();
                } finally {
                    if (_d) throw _e;
                }
            }
            return _arr;
        }
        return function (arr, i) {
            if (Array.isArray(arr)) {
                return arr;
            } else if (Symbol.iterator in Object(arr)) {
                return sliceIterator(arr, i);
            } else {
                throw new TypeError("Invalid attempt to destructure non-iterable instance");
            }
        };
    }();

    var createClass = function () {
        function defineProperties(target, props) {
            for (var i = 0; i < props.length; i++) {
                var descriptor = props[i];
                descriptor.enumerable = descriptor.enumerable || false;
                descriptor.configurable = true;
                if ("value" in descriptor) descriptor.writable = true;
                Object.defineProperty(target, descriptor.key, descriptor);
            }
        }
        return function (Constructor, protoProps, staticProps) {
            if (protoProps) defineProperties(Constructor.prototype, protoProps);
            if (staticProps) defineProperties(Constructor, staticProps);
            return Constructor;
        };
    }();

    function classCallCheck(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
            throw new TypeError("Cannot call a class as a function");
        }
    }

    function possibleConstructorReturn(self, call) {
        if (!self) {
            throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
        }
        return call && (typeof call === "object" || typeof call === "function") ? call : self;
    }

    function inherits(subClass, superClass) {
        if (typeof superClass !== "function" && superClass !== null) {
            throw new TypeError("Super expression must either be null or a function, not " + typeof superClass);
        }
        subClass.prototype = Object.create(superClass && superClass.prototype, {
            constructor: {
                value: subClass,
                enumerable: false,
                writable: true,
                configurable: true
            }
        });
        if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass;
    }

    // 页面渲染
    function Temp(options) {
        classCallCheck(this, Temp);
        var that = this;
        this.options = $.extend({
            id: null, // 模板id
            container: null, // 可选，真实的html的容器, 默认就是id的值
            data: {} // 模板数据
        }, options);

        if (!this.options.container) {
            this.options.container = this.options.id;
        }

        this.element = null; // 当前元素
        this.refIds = {}; // ref id集
        this.refs = {}; // 通过ref绑定的元素集
        this.tempElement = document.querySelector(this.options.id);
        this.tempHTML = this.tempElement.innerHTML;
        this.contentElement = document.querySelector('[data-temp="' + this.options.container.substring(1) + '"]');
        this.tempElement.parentNode.removeChild(this.tempElement);
        this.data = this.options.data;

        if (typeof this.getInitialState == 'function') {
            this.getInitialState();
        }
        this.render();
    }

    // 模板渲染
    createClass(Temp, [{
        key: 'render',
        value: function render() {
            if (!this.data || this.data == null || Object.keys(this.data).length < 1) {
                // 没有数据则不渲染
                return;
            }

            this.docfrag = document.createDocumentFragment();
            this.element = document.createElement('div');
            this.element.innerHTML = this.view(this.tempHTML, this.data);

            for (var i = 0, len = this.element.childNodes.length; i < len; i++) {
                this.docfrag.appendChild(this.element.childNodes[i].cloneNode(true));
            }

            // 页面渲染之前
            this.bindRef();
            if (typeof this.componentWillMount == 'function') {
                this.componentWillMount();
            }
            this.contentElement.innerHTML = '';
            this.contentElement.appendChild(this.docfrag);

            if (typeof this.componentDidMount == 'function') {
                this.componentDidMount();
            }
        }

        /* 字符串替换
         * str 需要替换的字符串
         * data 用来替换的数据
         * index 当前元素在列表中的索引值
         */

    }, {
        key: 'toReplace',
        value: function toReplace(str, data, index) {
            var strReg = /(\$\{v-for:([^\{]+)\}\s*>([^\$]*[^\{]*[^v]*[^-]*[^f]*)<(\$\{end\}))|(\$\{([^\{]+)\})/g;
            var that = this;

            return str.replace(strReg, function () {
                for (var _len = arguments.length, tokens = Array(_len), _key = 0; _key < _len; _key++) {
                    tokens[_key] = arguments[_key];
                }

                var variables = void 0;

                if (tokens[6] === undefined) {
                    var _ret = function () {
                        // 进入for循环
                        var _tokens$2$split = tokens[2].split(' ');

                        var _tokens$2$split2 = _slicedToArray(_tokens$2$split, 3);

                        var a = _tokens$2$split2[0];
                        var symbol = _tokens$2$split2[1];
                        var o = _tokens$2$split2[2];

                        var obj = that.eachAttr(o.split('.'), data);
                        var childStr = '';
                        var childData = {};

                        if (Array.isArray(obj)) {
                            //  如果是数组，则遍历数组
                            obj.map(function (item, index) {
                                childData[a] = item;
                                childStr += that.view(tokens[3], childData, index);
                            });
                        }
                        return {
                            v: '>' + childStr + '<'
                        };
                    }();

                    if ((typeof _ret === 'undefined' ? 'undefined' : _typeof(_ret)) === "object") return _ret.v;
                } else {
                    // 进入正常模式
                    variables = tokens[6].replace(/\s/g, '').split('.');
                }

                // 是否语句
                if (/^v-.+/ig.test(tokens[6])) {
                    var _tokens$6$split = tokens[6].split(':');

                    var _tokens$6$split2 = _slicedToArray(_tokens$6$split, 2);

                    var attr = _tokens$6$split2[0];
                    var val = _tokens$6$split2[1];

                    var _ret2 = function () {
                        switch (attr) {
                        case 'v-index':
                            // 返回索引值
                            if(val == 'cn' && index < 10) {
                                return {
                                    v: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'][index]
                                }
                            }
                            return {
                                v: index
                            };
                            break;
                        case 'v-show':
                            // 是否隐藏，需要默认显示
                            return {
                                v: that.eachAttr(val.split('.'), data) ? '' : 'style="display:none !important;"'
                            };
                            break;
                        case 'v-check':
                            // 是否选中
                            return {
                                v: that.eachAttr(val.split('.'), data) ? 'checked' : ''
                            };
                            break;
                        case 'v-on':
                            // 事件绑定
                            var _val$split = val.split('=');

                            var _val$split2 = _slicedToArray(_val$split, 2);

                            var eventName = _val$split2[0];
                            var eventFn = _val$split2[1];

                            var _event = that[eventFn.replace(/"|\s/g, '')];

                            if (typeof _event === 'function') {
                                var _ret3 = function () {
                                    var _datasetId = Math.random();

                                    document.addEventListener(eventName, function (event) {
                                        if (event.target.dataset.vId == _datasetId) {
                                            _event.call(that, event.target);
                                        }
                                    }, false);

                                    return {
                                        v: {
                                            v: 'data-v-id="' + _datasetId + '"'
                                        }
                                    };
                                }();

                                if ((typeof _ret3 === 'undefined' ? 'undefined' : _typeof(_ret3)) === "object") return _ret3.v;
                            }
                            break;
                        case 'v-ref':
                            // 绑定元素refId
                            var _refId = Math.random();
                            that.refIds[val] = _refId;
                            return {
                                v: 'data-ref-id="' + _refId + '"'
                            };
                            break;
                        }
                    }();

                    if ((typeof _ret2 === 'undefined' ? 'undefined' : _typeof(_ret2)) === "object") return _ret2.v;
                }

                return that.eachAttr(variables, data);
            });
        }

        // 绑定refId
    }, {
        key: 'bindRef',
        value: function bindRef() {
            // 在循环中使用，只会取最后一个
            for (var attr in this.refIds) {
                var val = '[data-ref-id="' + this.refIds[attr] + '"]';
                this.refs[attr] = this.docfrag.querySelector(val);
                this.refs[attr].removeAttribute('data-ref-id');
            }
        }

        // 遍历对象的属性

    }, {
        key: 'eachAttr',
        value: function eachAttr(attrs, data) {
            var i = 0,
                len = attrs.length,
                obj = data,
                attr = null;

            for (; i < len; ++i) {
                attr = /(.+)\[(\d)\]$/.exec(attrs[i]);
                if (attr && obj.hasOwnProperty(attr[1])) {
                    obj = obj[attr[1]][attr[2]];
                } else {
                    obj = obj[attrs[i]];
                }
                if (obj === undefined || obj === null) {
                    return '';
                }
            }
            return obj;
        }

        // 模板替换

    }, {
        key: 'view',
        value: function view(str, data) {
            for (var _len2 = arguments.length, indexs = Array(_len2 > 2 ? _len2 - 2 : 0), _key2 = 2; _key2 < _len2; _key2++) {
                indexs[_key2 - 2] = arguments[_key2];
            }

            if (typeof str != 'string') {
                throw new Error('第一个参数必须是字符串');
            }

            var index = indexs[0] ? indexs[0] : 0;
            var obj = arguments.length < 2 || arguments[1] == null ? {} : data;

            var code = '';
            var that = this;

            if (Array.isArray(obj)) {
                data.map(function (item, index) {
                    code += that.toReplace(str, item, index);
                });
            } else {
                code += that.toReplace(str, obj, index);
            }

            return code;
        }
    }]);

    /*
     * fn 构造函数名称
     * props 在fn构造函数上新增的原型属性
     */
    function extendsFn(props) {
        inherits(Fn, Temp);

        function Fn(options) {
            classCallCheck(this, Fn);
            return possibleConstructorReturn(this, Object.getPrototypeOf(Fn).call(this, options));
        }

        var _props = [];

        for(prop in props) {
            _props.push({
                key: prop,
                value: props[prop]
            });
        }

        createClass(Fn, _props);

        return Fn;
    }

    return extendsFn;
});
