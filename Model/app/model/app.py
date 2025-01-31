from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import numpy as np
import joblib
import time

model = joblib.load('random_forest_model.pkl')

app = FastAPI()

class PredictionInput(BaseModel):
    AccX: float
    AccY: float
    AccZ: float
    GyroX: float
    GyroY: float
    GyroZ: float


label_mapping = {1: "AGGRESSIVE", 2: "NORMAL", 3: "SLOW"}

@app.post("/predict")
async def predict(data: PredictionInput):
    try:
        AccX, AccY, AccZ = data.AccX, data.AccY, data.AccZ
        GyroX, GyroY, GyroZ = data.GyroX, data.GyroY, data.GyroZ

        AccMagnitude = (AccX**2 + AccY**2 + AccZ**2)**0.5
        GyroMagnitude = (GyroX**2 + GyroY**2 + GyroZ**2)**0.5

        Timestamp = time.time()

        features = np.array([
                    AccX, AccY, AccZ, GyroX, GyroY, GyroZ, Timestamp, 
                    GyroMagnitude, AccMagnitude
                ]).reshape(1, -1)     
                   
        prediction_encoded = model.predict(features)[0]
        print(prediction_encoded)

        label_mapping = {1: "AGGRESSIVE", 2: "NORMAL", 3: "SLOW"}
        prediction_label = label_mapping.get(prediction_encoded, "Unknown")
        
        return {"prediction": prediction_label}

    
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))
