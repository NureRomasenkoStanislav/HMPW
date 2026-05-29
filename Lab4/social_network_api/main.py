from fastapi import FastAPI, Depends, HTTPException, Query, status
from contextlib import asynccontextmanager
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from sqlalchemy.orm import selectinload
from typing import List, Optional

from core.database import get_db, engine
from models.models import Base, Post, Comment, DBLog
from schemas import schemas
from utils.logger import log_to_db

@asynccontextmanager
async def lifespan(app: FastAPI):
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
    yield

app = FastAPI(title="Social Network API", lifespan=lifespan)

async def get_current_user_id():
    return 1


#РІВЕНЬ 2 & 3: Робота з Постами

@app.post("/posts/", response_model=schemas.PostResponse, status_code=status.HTTP_201_CREATED)
async def create_post(
    post_data: schemas.PostCreate, 
    db: AsyncSession = Depends(get_db), 
    current_user_id: int = Depends(get_current_user_id)
):

    await log_to_db("INFO", f"User {current_user_id} is creating a post.")
    
    new_post = Post(
        title=post_data.title,
        content=post_data.content,
        user_id=current_user_id
    )
    db.add(new_post)
    await db.commit()
    await db.refresh(new_post)
    return new_post


#РІВЕНЬ 3: Пошук, Фільтрація та Оптимізація (Eager Loading)

@app.get("/posts/", response_model=List[schemas.PostResponse])
async def get_posts(
    search: Optional[str] = Query(None, description="Пошук за назвою або текстом"),
    user_id: Optional[int] = Query(None, description="Фільтрація за ID автора"),
    db: AsyncSession = Depends(get_db)
):

    query = select(Post).options(selectinload(Post.comments))
    
    if user_id:
        query = query.where(Post.user_id == user_id)
        
    if search:
        query = query.where(
            (Post.title.ilike(f"%{search}%")) | 
            (Post.content.ilike(f"%{search}%"))
        )
        
    result = await db.execute(query)
    posts = result.scalars().all()
    
    await log_to_db("INFO", f"Fetched {len(posts)} posts with search query: '{search}'")
    return posts


#РІВЕНЬ 2: Роблта з Коментарями

@app.post("/posts/{post_id}/comments/", response_model=schemas.CommentResponse)
async def create_comment(
    post_id: int,
    comment_data: schemas.CommentCreate,
    db: AsyncSession = Depends(get_db),
    current_user_id: int = Depends(get_current_user_id)
):
    post_check = await db.execute(select(Post).where(Post.id == post_id))
    if not post_check.scalar_one_or_none():
        await log_to_db("WARNING", f"User {current_user_id} tried to comment non-existing post {post_id}")
        raise HTTPException(status_code=404, detail="Пост не знайдено")
        
    new_comment = Comment(
        text=comment_data.text,
        user_id=current_user_id,
        post_id=post_id
    )
    db.add(new_comment)
    await db.commit()
    await db.refresh(new_comment)
    
    await log_to_db("INFO", f"User {current_user_id} added comment to post {post_id}")
    return new_comment


#РІВЕНЬ 4: Інтерфейс адміністратора для перегляду логів 

@app.get("/admin/logs/", response_model=List[dict])
async def get_system_logs(
    limit: int = Query(50, ge=1, le=100),
    db: AsyncSession = Depends(get_db)
):
    """Ендпоінт для адміністраторів: перегляд логів системи з бази даних"""
    query = select(DBLog).order_by(DBLog.timestamp.desc()).limit(limit)
    result = await db.execute(query)
    logs = result.scalars().all()
    
    return [
        {"id": log.id, "timestamp": log.timestamp, "level": log.level, "message": log.message} 
        for log in logs
    ]