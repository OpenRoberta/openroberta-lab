candle firmware.wxs java.wxs example.wxs
light -out setup.msi -ext WixUIExtension -cultures:de-DE firmware.wixobj java.wixobj example.wixobj -b ./firmware-1.2 -b ./java
@pause