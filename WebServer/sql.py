#!/usr/bin/python 
# -*- coding: utf-8 -*-
import sqlite3
import time

class SQL:
	def __init__(self):
		self.conn=sqlite3.connect('sql.db',check_same_thread=False)
		self.cursor=self.conn.cursor()

	def format(self,dataset):
		res=[]
		for i in dataset:
			res.append({'realname':i[1],'obj':i[2],'objdesc':i[3],'locationdesc':i[4],'telephone':i[5],'timedesc':i[6],'timestamp':i[7],'latlngx':i[8],'latlngy':i[9]})
		return res

	def publishLostData(self,datatuple):
		self.cursor.execute("INSERT INTO lostpub (realname,obj,objdesc,locationdesc,telephone,timedesc,timestamp,latlngx,latlngy,uuid) \
      VALUES ('%s', '%s', '%s', '%s', '%s', %d, %d ,%f, %f ,'%s')" % datatuple);
		self.commit()

	def publishFoundData(self,datatuple):
		self.cursor.execute("INSERT INTO foundpub (realname,obj,objdesc,locationdesc,telephone,timedesc,timestamp,latlngx,latlngy,uuid) \
      VALUES ('%s', '%s', '%s', '%s', '%s', %d, %d ,%f, %f ,'%s')" % datatuple);
		self.commit()

	def updateLostData(self,datatuple):
		self.cursor.execute("UPDATE lostpub SET realname='%s' ,obj='%s' ,objdesc='%s' ,locationdesc='%s' ,telephone='%s' ,timedesc=%d ,timestamp=%d,latlngx=%f,latlngy=%f WHERE uuid='%s'" % datatuple)
		self.commit()

	def updateFoundData(self,datatuple):
		self.cursor.execute("UPDATE foundpub SET realname='%s' ,obj='%s' ,objdesc='%s' ,locationdesc='%s' ,telephone='%s' ,timedesc=%d,timestamp=%d,latlngx=%f,latlngy=%f WHERE uuid='%s'" % datatuple)
		self.commit()

	def removeLostData(self,datatuple):
		self.cursor.execute("DELETE FROM lostpub WHERE uuid='%s'" % datatuple)
		self.commit()

	def removeFoundData(self,datatuple):
		self.cursor.execute("DELETE FROM foundpub WHERE uuid='%s'" % datatuple)
		self.commit()

	def getAllFoundData(self):
		dataset=self.cursor.execute("SELECT * FROM foundpub")
		return self.format(dataset)

	def getAllLostData(self):
		dataset=self.cursor.execute("SELECT * FROM lostpub")
		return self.format(dataset)

	def searchLostData(self,key,value):
		dataset=self.cursor.execute("SELECT * FROM lostpub WHERE %s LIKE '%%%s%%'" % (key,value))
		return self.format(dataset)

	def searchFoundData(self,key,value):
		dataset=self.cursor.execute("SELECT * FROM foundpub WHERE %s LIKE '%%%s%%'" % (key,value))
		return self.format(dataset)

	def searchLostDataByTimerange(self,timefrom,timeto):
		dataset=self.cursor.execute("SELECT * FROM lostpub WHERE timedesc>=%d AND timedesc<=%d" % (timefrom,timeto))
		return self.format(dataset)

	def searchFoundDataByTimerange(self,timefrom,timeto):
		dataset=self.cursor.execute("SELECT * FROM foundpub WHERE timedesc>=%d AND timedesc<=%d" % (timefrom,timeto))
		return self.format(dataset)

	def getLostDataByUUID(self,_uuid):
		dataset=self.cursor.execute("SELECT * FROM lostpub WHERE uuid='%s'" % (_uuid))
		return self.format(dataset)

	def getFoundDataByUUID(self,_uuid):
		dataset=self.cursor.execute("SELECT * FROM foundpub WHERE uuid='%s'" % (_uuid))
		return self.format(dataset)

	def commit(self):
		self.conn.commit()

	def close(self):
		self.conn.close()