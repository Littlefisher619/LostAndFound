from aip import AipOcr

""" 你的 APPID AK SK """
APP_ID = '000000'
API_KEY = '*******************'
SECRET_KEY = '*******************'

client = AipOcr(APP_ID, API_KEY, SECRET_KEY)

def get_file_content(filePath):
    with open(filePath, 'rb') as fp:
        return fp.read()
def ocr(img):
#image = get_file_content('example.png')
	return client.basicGeneral(img)
