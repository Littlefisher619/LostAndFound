var path = require('path')
var htmlWebpackPlugin = require('html-webpack-plugin')
var webpack = require('webpack');

module.exports = {
  entry: path.join(__dirname, './src/main.js'), // 入口文件
  output: { 
    path: path.join(__dirname, './dist'), // 输出路径
    filename: 'bundle.js' // 指定输出文件的名称
  },
  externals: {
    'BMap': 'BMap',
    'BMap_Symbol_SHAPE_POINT': 'BMap_Symbol_SHAPE_POINT'
  },
  plugins: [ // 所有webpack  插件的配置节点
    new htmlWebpackPlugin({
      template: path.join(__dirname, './src/index.html'), // 指定模板文件路径
      filename: 'index.html' // 设置生成的内存页面的名称
    }),
　　new webpack.ProvidePlugin({
　　　　 $: "jquery",
		jQuery: "jquery",
		jquery: "jquery",
		"window.jQuery": "jquery"
　　})
  ],
  devServer: {
	  disableHostCheck: true,
  },
  module: { // 配置所有第三方loader 模块的
    rules: [ // 第三方模块的匹配规则
      { test: /\.css$/, use: ['style-loader', 'css-loader'] }, // 处理 CSS 文件的 loader
      { test: /\.less$/, use: ['style-loader', 'css-loader', 'less-loader'] }, // 处理 less 文件的 loader
      { test: /\.scss$/, use: ['style-loader', 'css-loader', 'sass-loader'] }, // 处理 scss 文件的 loader
      { test: /\.(jpg|png|gif|bmp|jpeg)$/, use: 'url-loader' }, // 处理 图片路径的 loader
      { test: /\.(ttf|eot|svg|woff|woff2)$/, use: 'url-loader' }, // 处理 字体文件的 loader 
      { test: /\.js$/, use: 'babel-loader', exclude: /node_modules/ }, // 配置 Babel 来转换高级的ES语法
      { test: /\.vue$/, use: 'vue-loader' } // 处理 .vue 文件的 loader
    ]
  },
}