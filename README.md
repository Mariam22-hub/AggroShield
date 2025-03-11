# AggroShield 🚗🛡️  
**Aggressive Driving Detection & Reporting System**  

AggroShield is a full-stack application built using **Spring Boot** for detecting aggressive driving behavior in vehicles. It leverages **machine learning** through **FastAPI** to classify driving patterns and provides real-time alerts, comprehensive reports, and an interactive dashboard to monitor driving behavior.  

## 🚀 Features  

- **Aggressive Driving Detection**: Uses an ML classifier to analyze vehicle data and detect aggressive driving events such as hard braking, rapid acceleration, and sharp turns.  
- **Real-Time Alerts**: Sends immediate notifications when aggressive driving behavior is detected.  
- **Dashboard**: A user-friendly interface displaying detected events, vehicle analytics, and historical trends.  
- **Weekly & Monthly Reports**: Generates detailed reports summarizing driving behavior over time.  
- **FastAPI Integration**: Uses a **FastAPI-based microservice** to serve the ML model efficiently.  
- **Full-Stack Implementation**: Powered by **Spring Boot (Backend)** and a modern **frontend framework (Angular)**.  

## 🛠️ Tech Stack  

- **Backend**: Spring Boot, Spring Data JPA, REST APIs  
- **Frontend**: React / Angular / Vue (Choose based on your stack)  
- **Database**: PostgreSQL / MySQL  
- **Machine Learning (Microservice)**:  
  - **FastAPI** (for serving the ML model)  
  - Python (Scikit-learn / TensorFlow)  
  - Uvicorn (for high-performance ASGI server)  
- **Messaging & Alerts**: WebSockets, Kafka / RabbitMQ (if real-time streaming is needed)  

## 📊 System Workflow  

1. **Data Collection**: Captures vehicle telemetry data (speed, acceleration, braking patterns).  
2. **ML-Based Classification (FastAPI)**:  
   - The backend sends driving data to the FastAPI microservice.  
   - The FastAPI service runs the ML model and classifies the behavior as normal or aggressive.  
   - The response is sent back to the Spring Boot backend.  
3. **Alert Generation**: Sends notifications when an aggressive event is detected.  
4. **Storage & Reporting**: Logs events in the database and generates weekly/monthly reports.  
5. **Dashboard Visualization**: Displays real-time insights, trends, and analytics.  

