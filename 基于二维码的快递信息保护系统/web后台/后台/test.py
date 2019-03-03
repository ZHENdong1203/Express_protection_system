from MyQR import myqr
import os

word = 'https://beeeeee.herokuapp.com'

version, level, qr_name = myqr.run(
    word,  # 必要参数是二维码的内容，是一个str，其他参数可选
    version=10,  # int,1~40，边长
    level='H',  # str,'L','M','Q','H'，就错等级
    picture=None,  # 图片path，用于制作艺术二维码，建议选择正方形的照片
    contrast=1.0,  # 对比度
    brightness=1.0,  # 亮度
    save_name=None,  # 输出文件名。默认：输入图片文件名_qrcode.png
    save_dir="F:\python代码\Django\Second\static"  # 输出文件存储目录
)