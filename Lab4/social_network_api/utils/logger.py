from models.models import DBLog
from core.database import AsyncSessionLocal

async def log_to_db(level: str, message: str):
    async with AsyncSessionLocal() as session:
        async with session.begin():
            log_entry = DBLog(level=level, message=message)
            session.add(log_entry)