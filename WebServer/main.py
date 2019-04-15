#!/usr/bin/python 
# -*- coding: utf-8 -*-
from flask import Flask
from flask import make_response, jsonify
from flask import request
from flask_cors import *
import sql
import json
import time
import uuid
app = Flask(__name__)
CORS(app,)
sql=sql.SQL()

@app.route('/lost/publish',methods=['POST'])
def publishLostData():
	try:
		dataset=json.loads(request.get_data())
		_uuid=str(uuid.uuid1())
		sql.publishLostData((dataset['realname'],dataset['obj'],dataset['objdesc'],dataset['locationdesc'],dataset['telephone'],dataset['timedesc'],int(time.time()),dataset['latlngx'],dataset['latlngy'],_uuid))
		response = make_response(jsonify({'status': 'success','uuid':_uuid}))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify({'status': 'error','uuid':''})),500
	

@app.route('/found/publish',methods=['POST'])
def publishFoundData():
	try:
		dataset=json.loads(request.get_data())
		_uuid=str(uuid.uuid1())
		sql.publishFoundData((dataset['realname'],dataset['obj'],dataset['objdesc'],dataset['locationdesc'],dataset['telephone'],dataset['timedesc'],int(time.time()),dataset['latlngx'],dataset['latlngy'],_uuid))
		response = make_response(jsonify({'status': 'success','uuid':_uuid}))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify({'status': 'error','uuid':''})),500
	

@app.route('/lost/all')
def getAllLostData():
	try:
		response = make_response(jsonify(sql.getAllLostData()))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500

@app.route('/found/all')
def getAllFoundData():
	try:
		response = make_response(jsonify(sql.getAllFoundData()))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500



@app.route('/lost/getbyuuid',methods=['POST'])
def getLostDataByUUID():
	try:
		dataset=json.loads(request.get_data())
		response = make_response(jsonify(sql.getLostDataByUUID(dataset['uuid'])))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500

@app.route('/found/getbyuuid',methods=['POST'])
def getFoundDataByUUID():
	try:
		dataset=json.loads(request.get_data())
		response = make_response(jsonify(sql.getFoundDataByUUID(dataset['uuid'])))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500



@app.route('/lost/search',methods=['POST'])
def searchLostData():
	try:
		dataset=json.loads(request.get_data())
		response = make_response(jsonify(sql.searchLostData(dataset['key'],dataset['value'])))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500
	
@app.route('/found/search',methods=['POST'])
def searchFoundData():
	try:
		dataset=json.loads(request.get_data())
		response = make_response(jsonify(sql.searchFoundData(dataset['key'],dataset['value'])))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500

@app.route('/lost/search/timerange',methods=['POST'])
def searchLostDataByTimerange():
	try:
		dataset=json.loads(request.get_data())
		response = make_response(jsonify(sql.searchLostDataByTimerange(dataset['timefrom'],dataset['timeto'])))
		return response,200
		
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500
	

@app.route('/found/search/timerange',methods=['POST'])
def searchFoundDataByTimerange():
	try:
		dataset=json.loads(request.get_data())
		response = make_response(jsonify(sql.searchFoundDataByTimerange(dataset['timefrom'],dataset['timeto'])))
		return response,200
		
	except Exception as e:
		print(repr(e))
		return make_response(jsonify([])),500

@app.route('/lost/remove',methods=['POST'])
def removeLostData():
	try:
		dataset=json.loads(request.get_data())
		sql.removeLostData(dataset['uuid'])
		return make_response(jsonify({'status': 'success','uuid':dataset['uuid']})),200
		
	except Exception as e:
		print(repr(e))
		return make_response(jsonify({'status': 'error','uuid':''})),500


@app.route('/found/remove',methods=['POST'])
def removeFoundData():
	try:
		dataset=json.loads(request.get_data())
		sql.removeFoundData(dataset['uuid'])
		return make_response(jsonify({'status': 'success','uuid':dataset['uuid']})),200
		
	except Exception as e:
		print(repr(e))
		return make_response(jsonify({'status': 'error','uuid':''})),500

@app.route('/lost/update',methods=['POST'])
def updateLostData():
	#try:
		dataset=json.loads(request.get_data())
		sql.updateLostData((dataset['realname'],dataset['obj'],dataset['objdesc'],dataset['locationdesc'],dataset['telephone'],dataset['timedesc'],int(time.time()),dataset['latlngx'],dataset['latlngy'],dataset['uuid']))
		response = make_response(jsonify({'status': 'success','uuid':dataset['uuid']}))
		return response,200
	#except Exception as e:
		print(repr(e))
		return make_response(jsonify({'status': 'error','uuid':''})),500
	
@app.route('/found/update',methods=['POST'])
def updateFoundData():
	try:
		dataset=json.loads(request.get_data())
		sql.updateFoundData((dataset['realname'],dataset['obj'],dataset['objdesc'],dataset['locationdesc'],dataset['telephone'],dataset['timedesc'],int(time.time()),dataset['latlngx'],dataset['latlngy'],dataset['uuid']))
		response = make_response(jsonify({'status': 'success','uuid':dataset['uuid']}))
		return response,200
	except Exception as e:
		print(repr(e))
		return make_response(jsonify({'status': 'error','uuid':''})),500

@app.route('/img/upload/<uuid>',methods=['POST'])
def uploadImgae(uuid):
	f = request.files['file']
	basepath = os.path.dirname(__file__)
	upload_path = os.path.join(basepath, 'uploads',secure_filename(uuid+'.png'))
	f.save(upload_path)

if __name__ == '__main__':
	app.run(host='0.0.0.0',port=5000)