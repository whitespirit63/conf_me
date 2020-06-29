import base64


import uuid
import os.path
import numpy as np
import json
import face_recognition
import argparse
import pickle
import cv2
from quart import Quart, request, Response, jsonify

def faceDetect(img):
    data = pickle.loads(open("encodings.pickle", "rb").read())
    rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    faces = face_recognition.face_locations(rgb, model="cnn")
    encodings = face_recognition.face_encodings(rgb, faces)
    ids = []

    for encoding in encodings:
        found = face_recognition.compare_faces(data["encodings"], encoding)
        id = ''
        if True in found:
            index = [i for (i, f) in enumerate(found) if f]
            counts = {}
            for i in index:
                id = data["ids"][i]
                counts[id] = counts.get(id, 0) + 1
            id = max(counts, key=counts.get)

        if id != '':
            ids.append(id)
    return ids

app = Quart(__name__)


@app.route('/upload', methods=['POST'])
async def upload():
    d = await request.data
    npimg = np.frombuffer(base64.b64decode(d), dtype=np.uint8);
    image = cv2.imdecode(npimg, 1)
    ids = faceDetect(image)
    print(ids)
    return jsonify(ids)


app.run(host='localhost', port=5000)