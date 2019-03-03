from django.db import models

# Create your models here.


class Admin (models.Model):#管理员表
    username = models.CharField(max_length=20,unique=True)#用户名唯一
    password = models.CharField(max_length=20)

    def __unicode__(self):
        return '%d: %s' % (self.pk, self.username)

class Cargolist(models.Model):#快递列表
    sender = models.CharField(max_length=20)  # 用户名唯一
    receiver = models.CharField(max_length=20)
    phone = models.IntegerField()
    address = models.CharField(max_length=50)
    cargoname = models.CharField(max_length=20)
    postmanname = models.CharField(max_length=20)
    cargonum = models.CharField(max_length=40)
    #pngname = models.CharField(max_length=30)
    def __unicode__(self):
            return '%d: %s' % (self.pk, self.username)

class Usertable(models.Model):
    name = models.CharField(max_length=20,unique=True)#用户名唯一
    password = models.CharField(max_length=20)
    phonenum = models.IntegerField()
    def __unicode__(self):
        return '%d: %s' % (self.pk, self.username)