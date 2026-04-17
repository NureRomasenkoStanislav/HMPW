from django.shortcuts import render, get_object_or_404, redirect
from django.db.models import Q
from .models import Article, Category, Comment

# Список усіх статей
def article_list(request):
    articles = Article.objects.all().order_by('-pub_date')
    return render(request, 'blog/article_list.html', {'articles': articles})

# Окрема стаття та коментарі до неї
def article_detail(request, pk):
    article = get_object_or_404(Article, pk=pk)
    comments = article.comments.all()
    return render(request, 'blog/article_detail.html', {
        'article': article, 
        'comments': comments
    })
# Пошук (Рівень 4)
def search_articles(request):
    query = request.GET.get('q')  
    results = []
    if query:
        results = Article.objects.filter(
            Q(title__icontains=query) | Q(text__icontains=query)
        )
    return render(request, 'blog/search_results.html', {
        'results': results, 
        'query': query
    })

# Видалення коментаря (Рівень 3)
def delete_comment(request, comment_id):
    comment = get_object_or_404(Comment, id=comment_id)
    article_id = comment.article.id
    comment.delete()
    return redirect('article_detail', pk=article_id)