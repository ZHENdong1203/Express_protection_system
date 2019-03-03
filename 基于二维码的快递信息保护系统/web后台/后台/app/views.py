from django.shortcuts import render
from django.http import HttpResponse, StreamingHttpResponse
from .models import *
import uuid
import json
import qrcode
import image
from MyQR import myqr
from .form import CargolistForm
# Create your views here.

def postmanregister(req):#快递员注册
    name = req.GET['name']
    password = req.GET['password']
    phonenum = req.GET['phonenum']
    u = Usertable.objects.filter(name=name)
    if u.exists():
        return HttpResponse("已经注册过了")
    else:
        user = Usertable(name=name,password=password,phonenum=phonenum)
        user.save()
        return HttpResponse("注册成功")


def postmanlogin(req):#快递员登陆
    name = req.GET['name']
    password = req.GET['password']
    u = Usertable.objects.filter(name=name)
    if u.exists() :
        if u[0].password == password:
            return HttpResponse('登陆成功')
        else:
            return HttpResponse('密码错误')
    else:
        return HttpResponse("无用户")

def addadminuser(req):#管理员注册
    name = req.GET['name']
    password = req.GET['password']
    a = Admin(username=name,password=password)
    a.save()
    return HttpResponse("true")

def adminlogin(req):#管理员登陆
    username = req.GET['name']
    password = req.GET['password']
    u = Admin.objects.filter(username=username)
    if u.exists() :
        if u[0].password == password:
            #显示所有cargo
            u = Cargolist.objects.all()
            return render(req, 'test.html',{'u': u})
        else:
            return HttpResponse('密码错误')
    else:
        return HttpResponse("无用户")

def addcargo(req):#添加快递件,只有管理员可以添加
        sender = req.GET['sender']
        receiver = req.GET['receiver']
        phone = req.GET['phone']
        address = req.GET['address']
        cargoname = req.GET['cargoname']
        postmanname = req.GET['postmanname']
        cargonum = uuid.uuid1()
        # img = qrcode.make(cargonum)
        # img.save(str(cargonum) + '.png')
        version, level, qr_name = myqr.run(
            str(cargonum),  # 必要参数是二维码的内容，是一个str，其他参数可选
            version=5,  # int,1~40，边长
            level='H',  # str,'L','M','Q','H'，就错等级
            picture=None,  # 图片path，用于制作艺术二维码，建议选择正方形的照片
            contrast=1.0,  # 对比度
            brightness=1.0,  # 亮度
            save_name= str(cargonum) + '.png',  # 输出文件名。默认：输入图片文件名_qrcode.png
            save_dir="F:\python代码\Django\Second\static"  # 输出文件存储目录
        )

        c = Cargolist(sender=sender, receiver=receiver, phone=phone, address=address, cargoname=cargoname,
                          postmanname=postmanname, cargonum=str(cargonum) + '.png')
        c.save()
        u = Cargolist.objects.all()
        return render(req, 'test.html', {'u': u})


def querycargo(req):
    #由客户端发送唯一uuid cargo，uuid在扫码时获取
    uuid = req.GET['uuid']
    name = req.GET['name']
    c = Cargolist.objects.filter(cargonum=uuid)
    if c.exists():
        if c[0].postmanname == name:
            #return json 数据
            resp = {}
            x = []
            d = {}
            d['sender'] = c[0].sender
            d['receiver'] = c[0].receiver
            d['phone'] = c[0].phone
            d['address'] = c[0].address
            d['cargoname'] = c[0].cargoname
            d['cargonum'] = c[0].cargonum
            x.append(d)
            resp["body"] = x
            return HttpResponse(json.dumps(resp), content_type="application/json")
        else:
            return HttpResponse("你没有权限查看此信息")
    else:
        return HttpResponse("没有此快递")



def home(req):
    return render(req,'home.html')

def test(req):
    u = Cargolist.objects.all()
    return render(req, 'test.html', {'u': u})