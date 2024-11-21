from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import cx_Oracle
import os

import os
os.environ['PATH'] = r"C:\oracle\instantclient-basic-windows.x64-19.25.0.0.0dbru\instantclient_19_25" + ";" + os.environ['PATH']


app = FastAPI()

# Define the data model using Pydantic
class Item(BaseModel):
    email: str
    senha: str

class ConsumoResponse(BaseModel):
    nome_item: str
    consumo_hora: float
    valor: float    

# Oracle DB configuration - replace with your actual database details
dsn = cx_Oracle.makedsn("oracle.fiap.com.br", "1521", service_name="orcl")
db_username = "rm95927"
db_password = "040504"

# Example: Insert data into the table
def insert_data(item: Item):
    with cx_Oracle.connect(db_username, db_password, dsn) as connection:
        with connection.cursor() as cursor:
            try:
                cursor.execute("""
                    INSERT INTO users (email, senha) VALUES (:email, :senha)
                """, [item.email, item.senha])
                connection.commit()
            except cx_Oracle.DatabaseError as e:
                raise HTTPException(status_code=500, detail=f"Database Error: {str(e)}")

@app.post("/items/")
async def create_item(item: Item):
    insert_data(item)
    return item

@app.get("/")
async def read_root():
    return {"message": "Welcome to the FastAPI with Oracle DB"}

@app.get("/check-user/")
async def check_user(email: str):
    with cx_Oracle.connect(db_username, db_password, dsn) as connection:
        with connection.cursor() as cursor:
            cursor.execute("SELECT COUNT(*) FROM users WHERE email = :email", [email])
            count = cursor.fetchone()[0]
            return count > 0

@app.get("/consumo/")
async def check_item_consumo(nome_item: str):
    with cx_Oracle.connect(db_username, db_password, dsn) as connection:
        with connection.cursor() as cursor:
            # Query to get consumption data from the database
            cursor.execute("""
                SELECT nome_item, consumo_hora, valor 
                FROM eletro 
                WHERE nome_item = :nome_item
            """, [nome_item])
            
            # Check if the item exists and return data
            result = cursor.fetchone()
            
            if result:
                nome_item, consumo_hora, valor = result
                return ConsumoResponse(
                    nome_item=nome_item,
                    consumo_hora=consumo_hora,
                    valor=valor
                )
            else:
                raise HTTPException(status_code=404, detail="Item não encontrado")

@app.get("/eletrodomesticos/")
async def get_eletrodomesticos():
    with cx_Oracle.connect(db_username, db_password, dsn) as connection:
        with connection.cursor() as cursor:
            cursor.execute("SELECT nome_item FROM eletro")
            result = cursor.fetchall()
            return [row[0] for row in result]                

#Optional: Define the database connection/disconnection logic
@app.on_event("startup")
async def startup_event():
    os.environ['TNS_ADMIN'] = '/path/to/your/wallet' # if you are using Oracle Autonomous Cloud Database
    # Other startup code goes here

@app.on_event("shutdown")
async def shutdown_event():
    # Perform any cleanup operations here
    pass

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, 
                host="localhost", 
                port=8000,
                ssl_keyfile="key.pem",  
                ssl_certfile="certificate.pem"
                )