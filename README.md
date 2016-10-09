# AndroidMultiChannelBuildTool-Java
安卓多渠道打包工具java版本

## 说明

- 复制ChannelUtil至项目下，通过getUmengChannel方法获取渠道生成原始apk包
- 复制apk至工程目录下
- 修改MultiChannelBuildToolMain中sourceApkName和原始apk名称一致
- /info/channel.txt 里面设置渠道号
- 运行即可生成至output目录下

## 补充

ApkExtChannelUtls文件是通过渠道写入apk文件末尾然后从末尾读取实现


## 备注

python脚本形式参考[ANDROID多渠道快速打包实践](http://www.jianshu.com/p/d59fa66c3312)
