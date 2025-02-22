# Aggressive Driving Detection Project

## Overview
This project is a microservices-based application designed to detect aggressive driving behaviors using machine learning and predictive analysis. The system comprises multiple microservices that interact to provide a seamless experience, including a Prediction Service powered by a FastAPI-based machine learning model.

## Prerequisites
Before starting the project, ensure the following:

1. **Python Environment:** Install Python 3.8 or higher.
2. **Maven:** Install Maven for managing Java dependencies.
3. **Java Development Kit (JDK):** Install JDK 17 or higher.
4. **PostgreSQL:** Install and configure PostgreSQL as the database.

## Setting Up the Machine Learning Model
The Prediction Service requires a pre-trained machine learning model to function.

### Steps:
1. Navigate to the directory containing the FastAPI project.
2. Install the required Python libraries:
   ```bash
   pip install -r requirements.txt
   ```
3. Start the FastAPI server by running the following command:
   ```bash
   python -m uvicorn app:app --reload
   ```
4. Ensure the server is running on `http://localhost:8000` (default).

## Future Enhancements
- Integrate a user-friendly frontend interface.
- Expand the microservices with more advanced features.
- Add support for additional machine learning models.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

## License
This project is licensed under [MIT License](LICENSE).

